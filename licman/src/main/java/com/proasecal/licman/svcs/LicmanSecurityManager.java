package com.proasecal.licman.svcs;

import java.security.Key;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Hex;

import com.proasecal.licman.exceptions.DESedeEncryptionException;
import com.proasecal.licman.exceptions.FechaIncorrectaException;
import com.proasecal.licman.exceptions.NumeroLicenciaException;
import com.proasecal.licman.exceptions.PluginKeyGenerationException;
import com.proasecal.licman.vo.LicenciaVO;

public class LicmanSecurityManager {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(LicmanSecurityManager.class);

	private static Locale _localeColombia = new Locale("es_CO");

	public String generatePluginKey() throws PluginKeyGenerationException {
		StringBuffer pluginKey = new StringBuffer();
		try {
			PrivateKey privateKey = ConfigManager.getPrivateKey();
			Cipher cipher = Cipher.getInstance("RSA", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptedKey = cipher.doFinal(ConfigManager.getDESedeKey()
					.getEncoded());
			Signature instance = Signature.getInstance("SHA1withRSA", "BC");
			instance.initSign(privateKey);
			instance.update(ConfigManager.getDESedeKey().getEncoded());
			byte[] signature = instance.sign();
			pluginKey.append(Hex.encodeHexString(encryptedKey));
			pluginKey.append(Hex.encodeHexString(ConfigManager.getPublicKey()
					.getEncoded()));
			pluginKey.append(Hex.encodeHexString(signature));
		} catch (Exception ex) {
			throw new PluginKeyGenerationException(
					ConfigManager.getString("error.text.keyencryption"), ex);
		}
		return pluginKey.toString();
	}

	public String encrypt3DES(String text) throws Exception {
		Key desedeKey = ConfigManager.getDESedeKey();
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, desedeKey);
		byte[] encryptedText = cipher.doFinal(text.getBytes("UTF8"));
		return Hex.encodeHexString(encryptedText).toUpperCase();
	}

	public String validarNumeroLicencia(String nrolicencia)
			throws NumeroLicenciaException {
		String ivs;
		StringBuffer bufRandom = new StringBuffer();
		StringBuffer bufCiphered = new StringBuffer();
		nrolicencia = nrolicencia.toLowerCase();
		for (int i = 0; i < nrolicencia.length(); i += 5) {
			bufRandom.append(nrolicencia.substring(i, i + 2));
			bufCiphered.append(nrolicencia.substring(i + 2, i + 5));
		}
		ivs = bufRandom.substring(2);
		LOGGER.debug("ivs " + ivs);
		LOGGER.debug("random " + bufRandom);
		LOGGER.debug("ciphered " + bufCiphered);
		Key desedeKey = ConfigManager.getDESedeKey();
		try {
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, desedeKey, iv);
			Hex enc = new Hex();
			byte[] bCiphered = cipher.doFinal(enc.decode(bufRandom.toString()
					.getBytes()));
			String strServerCiphered = Hex.encodeHexString(bCiphered)
					.substring(1);
			LOGGER.debug("ciphered server " + strServerCiphered);
			if (!strServerCiphered.equals(bufCiphered.toString())) {
				throw new NumeroLicenciaException(
						ConfigManager.getString("error.text.nrolicencia"));
			}
		} catch (Exception ex) {
			throw new NumeroLicenciaException(
					ConfigManager.getString("error.text.nrolicencia"), ex);

		}
		return bufRandom.toString();
	}

	public String generarClaveActivacion(String strRandom,
			Date fechaVencimiento, Character lastDigit)
			throws DESedeEncryptionException, FechaIncorrectaException {

		StringBuffer bufActkey = new StringBuffer();
		Date hoy = getToday();
		// Fecha de vencimiento incorrecta. Debe ser > hoy
		LOGGER.debug("Generando clave de activaci\u00F3n");
		if (fechaVencimiento.before(hoy)) {
			LOGGER.debug("Fecha de vencimiento incorrecta");
			throw new FechaIncorrectaException(
					ConfigManager.getString("error.text.fecha"));
		}
		// Fechas XOR random
		byte[] barrRandom = strRandom.getBytes();
		byte[] barrFechas = getBinaryDates(hoy, fechaVencimiento);
		LOGGER.debug("Fechas: "
				+ String.valueOf(Hex.encodeHexString(barrFechas)));
		for (int i = 0; i < barrFechas.length; i++) {
			barrFechas[i] ^= barrRandom[i];
		}
		LOGGER.debug("XOR fechas ^ random: "
				+ String.valueOf(Hex.encodeHexString(barrFechas)));
		// MAC
		byte[] barrMac = new byte[4];
		System.arraycopy(barrFechas, 0, barrMac, 0, 4);
		LOGGER.debug("MAC: " + String.valueOf(Hex.encodeHexString(barrMac)));
		Key desedeKey = ConfigManager.getDESedeKey();
		try {
			LOGGER.debug("Cifrando clave de activaci\u00F3n");
			String ivs = strRandom.substring(2);
			LOGGER.debug("ivs " + ivs);
			IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, desedeKey, iv);
			byte[] cipheredActKey = cipher.doFinal(barrFechas);
			bufActkey.append(Hex.encodeHexString(cipheredActKey).toUpperCase());
			bufActkey.append(Hex.encodeHexString(barrMac).toUpperCase());
			// Padding para completar 25 caracteres e indicar tipo de licencia
			// F->nueva | 0->renovación
			if (lastDigit == null) {
				bufActkey.append('F');
			} else {
				bufActkey.append(lastDigit);
			}
			LOGGER.debug("Clave de activaci\u00F3n: " + bufActkey.toString());
		} catch (Exception ex) {
			throw new DESedeEncryptionException(
					ConfigManager.getString("error.text.3desencrypt"), ex);

		}
		return bufActkey.toString();
	}

	public List<LicenciaVO> renovarLicencias(Set<LicenciaVO> lstLicencias,
			Date fechaVencimiento) throws DESedeEncryptionException,
			FechaIncorrectaException {

		List<LicenciaVO> lstNuevasLicencias = new ArrayList<LicenciaVO>();
		Date hoy = getToday();
		// Fecha de vencimiento incorrecta. Debe ser > hoy
		LOGGER.debug("Generando clave de activaci\u00F3n");
		if (fechaVencimiento.before(hoy)) {
			LOGGER.debug("Fecha de vencimiento incorrecta");
			throw new FechaIncorrectaException(
					ConfigManager.getString("error.text.fecha"));
		}
		try {
			for (LicenciaVO licenciaAnterior : lstLicencias) {
				StringBuffer bufActkey = new StringBuffer();
				LicenciaVO nuevaLicencia = licenciaAnterior.clonar();
				// Random anterior
				String strNroLicenciaAnterior = licenciaAnterior
						.getNumero_licencia();
				int iLongitudCampo = ConfigManager
						.getLongitudxCampoClaveActivacion();
				String[] arrNroLicenciaAnterior = strNroLicenciaAnterior
						.split("(?<=\\G.{" + iLongitudCampo + "})");
				StringBuffer bfrRandom = new StringBuffer();
				for (String campo : arrNroLicenciaAnterior) {
					bfrRandom.append(campo.substring(0, 2));
				}
				String strRandom = bfrRandom.toString().toLowerCase();
				byte[] barrRandom = strRandom.getBytes();
				LOGGER.debug("Random: " + strRandom);
				// Fechas XOR random
				byte[] barrFechas = getBinaryDates(hoy, fechaVencimiento);
				LOGGER.debug("Fechas: "
						+ String.valueOf(Hex.encodeHexString(barrFechas)));
				for (int i = 0; i < barrFechas.length; i++) {
					barrFechas[i] ^= barrRandom[i];
				}
				LOGGER.debug("XOR fechas ^ random: "
						+ String.valueOf(Hex.encodeHexString(barrFechas)));
				// MAC
				byte[] barrMac = new byte[4];
				System.arraycopy(barrFechas, 0, barrMac, 0, 4);
				LOGGER.debug("MAC: "
						+ String.valueOf(Hex.encodeHexString(barrMac)));
				Key desedeKey = ConfigManager.getDESedeKey();
				LOGGER.debug("Cifrando clave de activaci\u00F3n");
				String ivs = strRandom.substring(2);
				LOGGER.debug("ivs " + ivs);
				IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
				Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding",
						"BC");
				cipher.init(Cipher.ENCRYPT_MODE, desedeKey, iv);
				byte[] cipheredActKey = cipher.doFinal(barrFechas);
				bufActkey.append(Hex.encodeHexString(cipheredActKey)
						.toUpperCase());
				bufActkey.append(Hex.encodeHexString(barrMac).toUpperCase());
				// Padding para completar 25 caracteres e indicar tipo de
				// licencia
				// F->nueva | 0->renovaciónn
				bufActkey.append('0');
				LOGGER.debug("Clave de activaci\u00F3n: "
						+ bufActkey.toString());
				nuevaLicencia.setClave_activacion(bufActkey.toString());
				nuevaLicencia.setFecha_vencimiento(fechaVencimiento);
				nuevaLicencia.setPadreVO(licenciaAnterior);
				licenciaAnterior.setHijoVO(nuevaLicencia);
				lstNuevasLicencias.add(nuevaLicencia);
			}
		} catch (Exception ex) {
			throw new DESedeEncryptionException(
					ConfigManager.getString("error.text.3desencrypt"), ex);

		}
		return lstNuevasLicencias;
	}

	// Helpers ****************************************************************

	private byte[] getBinaryDates(Date date1, Date date2) {
		byte[] result = new byte[6];
		Calendar cal = Calendar.getInstance(_localeColombia);
		cal.setTime(date1);
		int day1 = cal.get(Calendar.DATE);
		int month1 = cal.get(Calendar.MONTH);
		int year1 = cal.get(Calendar.YEAR);
		cal.setTime(date2);
		int day2 = cal.get(Calendar.DATE);
		int month2 = cal.get(Calendar.MONTH);
		int year2 = cal.get(Calendar.YEAR);
		result[0] = (byte) (day1 & 0xFF);
		result[1] = (byte) (month1 & 0xFF);
		result[2] = (byte) ((year1 % 100) & 0xFF);
		result[3] = (byte) (day2 & 0xFF);
		result[4] = (byte) (month2 & 0xFF);
		result[5] = (byte) ((year2 % 100) & 0xFF);
		return result;
	}

	private Date getToday() {
		Calendar cal = Calendar.getInstance(_localeColombia);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
