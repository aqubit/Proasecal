package com.proasecal.plugin.svcs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.test.FixedSecureRandom;

import com.proasecal.plugin.exceptions.PluginException;
import com.proasecal.plugin.exceptions.ProductoNoRegistradoException;
import com.proasecal.plugin.gui.DlgError;
import com.proasecal.plugin.gui.DlgRegistro;

public class PluginSecurityManager {

	private static byte[] _barrRandom = null;
	private static byte[] _barrStoredRandom = null;
	private static int _reintentos = ConfigManager.NUMERO_REINTENTOS;
	private static Hex _hexEncoder = new Hex();

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public PluginSecurityManager() {
	}

	public static void validarRegistro(final String strproducto) {
		PluginSecurityManager plugin = new PluginSecurityManager();
		try {
			plugin.validarRegistro();
		} catch (PluginException e) {
			DlgError frame = new DlgError(e, strproducto);
			frame.setVisible(true);
		} catch (ProductoNoRegistradoException e) {
			DlgRegistro frame = new DlgRegistro(strproducto);
			frame.setVisible(true);
		}
	}

	public String cifrarTexto(byte[] barrPlainText) throws PluginException {
		String result = null;
		try {
			Key key = load3DESKey();
			byte[] barrRandom = createRandomString(ConfigManager.RANDOM_LENGTH * 2);
			byte[] barrWholeText = new byte[barrRandom.length
					+ barrPlainText.length];
			System.arraycopy(barrRandom, 0, barrWholeText, 0, barrRandom.length);
			System.arraycopy(barrPlainText, 0, barrWholeText,
					barrRandom.length, barrPlainText.length);
			String strRandom = Hex.encodeHexString(barrRandom);
			IvParameterSpec iv = new IvParameterSpec(strRandom.substring(2, 10)
					.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] encryptedText = cipher.doFinal(barrWholeText);
			result = strRandom + Hex.encodeHexString(encryptedText);
			result = result.toUpperCase();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PluginException("error.code.diecisiete");
		}
		return result;
	}

	public byte[] descifrarTexto(String strCipheredText) throws PluginException {
		byte[] result = null;
		try {
			Key key = load3DESKey();
			strCipheredText = strCipheredText.toLowerCase();
			byte[] barrCipheredText = strCipheredText.substring(
					ConfigManager.RANDOM_ENCODED_LENGTH * 2).getBytes();
			String strRandom = strCipheredText.substring(0,
					ConfigManager.RANDOM_ENCODED_LENGTH * 2);
			IvParameterSpec iv = new IvParameterSpec(strRandom.substring(2, 10)
					.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] barrPlainWholeText = cipher.doFinal(_hexEncoder
					.decode(barrCipheredText));
			int iDataLength = barrPlainWholeText.length
					- ConfigManager.RANDOM_LENGTH * 2;
			result = new byte[iDataLength];
			byte[] barrMacRandom = new byte[ConfigManager.RANDOM_LENGTH * 2];
			System.arraycopy(barrPlainWholeText, 0, barrMacRandom, 0,
					barrMacRandom.length);
			System.arraycopy(barrPlainWholeText, barrMacRandom.length, result,
					0, iDataLength);
			String strMacRandom = Hex.encodeHexString(barrMacRandom);
			if (!strRandom.equals(strMacRandom)) {
				throw new PluginException("error.code.diecisiete");
			}
		} catch (Exception ex) {
			throw new PluginException("error.code.diecisiete");
		}
		return result;
	}

	private void validarRegistro() throws ProductoNoRegistradoException,
			PluginException 
	{
		if (!isWindows()) {
			throw new PluginException("error.code.ocho", false);
		}
		try {
			validarSancionxReintentos();
			String currentWinKey = getWindowsProductId();
			MessageDigest sha1 = MessageDigest.getInstance("SHA1", "BC");
			byte[] digestWinKey = sha1.digest(currentWinKey.getBytes());
			String hexDigestWinKey = Hex.encodeHexString(digestWinKey);
			Preferences p = Preferences.userRoot();
			String strContent = p.get(hexDigestWinKey, "-");
			if (strContent.equals("-")) {
				throw new ProductoNoRegistradoException();
			}
			Key tdeskey = load3DESKey();
			String strRandom = strContent.substring(0,
					ConfigManager.RANDOM_ENCODED_LENGTH);
			IvParameterSpec iv = new IvParameterSpec(strRandom.substring(2)
					.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, tdeskey, iv);
			_barrStoredRandom = _hexEncoder.decode(strRandom.getBytes());
			byte[] encryptedData = strContent.substring(
					ConfigManager.RANDOM_ENCODED_LENGTH).getBytes();
			encryptedData = _hexEncoder.decode(encryptedData);
			byte[] plainData = cipher.doFinal(encryptedData);
			ByteArrayInputStream bis = new ByteArrayInputStream(plainData);
			ObjectInputStream objIn = new ObjectInputStream(bis);
			Date latestDate = (Date) objIn.readObject();
			Date fechaVencimiento = (Date) objIn.readObject();
			String winkey = (String) objIn.readObject();
			objIn.close();
			// Es la fecha del sistema anterior a la última fecha de ejecución
			Calendar cal = Calendar.getInstance(ConfigManager.PLUGIN_LOCALE);
			Date hoy = cal.getTime();
			if (latestDate.after(hoy)) {
				throw new PluginException("error.code.once", false);
			}
			// Quitar la hora para comparar la fecha de vencimiento
			cal.setTime(hoy);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			hoy = cal.getTime();
			if (fechaVencimiento.compareTo(hoy) < 0) {
				throw new PluginException("error.code.seis", _reintentos--);
			}
			// Comparar el product key de windows
			if (!winkey.equals(currentWinKey)) {
				throw new PluginException("error.code.quince");
			}
			// Actualizar la licencia
			_barrRandom = _barrStoredRandom;
			guardarLicencia(fechaVencimiento, currentWinKey);
		} catch (ProductoNoRegistradoException e) {
			throw e;
		} catch (PluginException e) {
			throw e;
		} catch (Exception e) {
			throw new PluginException("error.code.tres");
		}
	}

	public void validarClaveActivacion(String strClaveAct)
			throws PluginException {
		try {
			if (_reintentos == 0) {
				guardarSancionxReintentos();
				throw new PluginException("error.code.diez", false, _reintentos);
			}
			Key key = load3DESKey();
			String strRandom = null;
			// clave x renovación
			char lastChar = strClaveAct.charAt(strClaveAct.length() - 1);
			if (_barrStoredRandom != null && lastChar == '0') {
				strRandom = Hex.encodeHexString(_barrStoredRandom);
				// nueva clave
			} else {
				strRandom = Hex.encodeHexString(_barrRandom);
			}
			IvParameterSpec iv = new IvParameterSpec(strRandom.substring(2)
					.getBytes());
			byte[] barrClaveAct = strClaveAct.getBytes();
			byte[] barrCiphered = new byte[16];
			byte[] barrMacEncoded = new byte[8];
			System.arraycopy(barrClaveAct, 0, barrCiphered, 0, 16);
			System.arraycopy(barrClaveAct, 16, barrMacEncoded, 0, 8);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] barrFechas = cipher
					.doFinal(_hexEncoder.decode(barrCiphered));
			byte[] barrMac = _hexEncoder.decode(barrMacEncoded);
			byte[] barrEncodedRandom = strRandom.getBytes();
			for (int i = 0; i < barrMac.length; i++) {
				barrMac[i] ^= barrEncodedRandom[i];
			}
			for (int i = 0; i < barrFechas.length; i++) {
				barrFechas[i] ^= barrEncodedRandom[i];
			}
			// validar MAC
			for (int i = 0; i < barrMac.length; i++) {
				if (barrMac[i] != barrFechas[i]) {
					throw new PluginException("error.code.uno", _reintentos--);
				}
			}
			// validar fecha del sistema
			Date fechaVencimiento = validarFechas(barrFechas);
			// guardar la licencia
			String currentWinKey = getWindowsProductId();
			guardarLicencia(fechaVencimiento, currentWinKey);
		} catch (PluginException e) {
			throw e;
		} catch (Exception e) {
			throw new PluginException("error.code.uno", _reintentos--);
		}
	}

	public String[] generarNumeroLicencia() throws PluginException {
		String[] result = null;
		StringBuffer buf = new StringBuffer();
		try {
			Key key = load3DESKey();
			_barrRandom = createRandomString(ConfigManager.RANDOM_LENGTH);
			String strRandom = Hex.encodeHexString(_barrRandom);
			IvParameterSpec iv = new IvParameterSpec(strRandom.substring(2)
					.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] encryptedText = cipher.doFinal(_barrRandom);
			String strEncryptedRnd = Hex.encodeHexString(encryptedText)
					.substring(1);
			for (int i = 0, j = 0, k = 0; i < ConfigManager.FIELD_LENGTH; i++, j += 2, k += 3) {
				buf.append(strRandom.substring(j, j + 2));
				buf.append(strEncryptedRnd.substring(k, k + 3));
			}
			result = buf.toString().toUpperCase()
					.split("(?<=\\G.{" + ConfigManager.FIELD_LENGTH + "})");
		} catch (Exception ex) {
			throw new PluginException("error.code.tres", false);
		}
		return result;
	}

	// Helpers ****************************************************************

	private void validarSancionxReintentos() throws PluginException {
		try {
			Preferences p = Preferences.userRoot();
			MessageDigest sha1 = MessageDigest.getInstance("SHA1", "BC");
			byte[] digestReintentosKey = sha1
					.digest(ConfigManager.REINTENTOS_KEY);
			String hexDigestReintentos = Hex
					.encodeHexString(digestReintentosKey);
			String encryptedData = p.get(hexDigestReintentos, "-");
			if (!encryptedData.equals("-")) {
				Key tdeskey = load3DESKey();
				Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding",
						"BC");
				cipher.init(Cipher.DECRYPT_MODE, tdeskey);
				byte[] plainData = cipher.doFinal(_hexEncoder
						.decode(encryptedData.getBytes()));
				ByteArrayInputStream bis = new ByteArrayInputStream(plainData);
				ObjectInputStream objIn = new ObjectInputStream(bis);
				Date dtSancion = (Date) objIn.readObject();
				objIn.close();
				Calendar cal = Calendar
						.getInstance(ConfigManager.PLUGIN_LOCALE);
				Date now = cal.getTime();
				if (dtSancion.compareTo(now) > 0) { // sigue vigente la sanción
					throw new PluginException("error.code.diez", false);
				} else {
					p.remove(hexDigestReintentos);
					p.flush();
				}
			}
		} catch (PluginException e) {
			throw e;
		} catch (Exception e) {
			throw new PluginException("error.code.dieciseis", false);
		}
	}

	private void guardarSancionxReintentos() throws PluginException {
		try {
			Key tdeskey = load3DESKey();
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, tdeskey);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(bos);
			Calendar cal = Calendar.getInstance(ConfigManager.PLUGIN_LOCALE);
			cal.add(Calendar.HOUR, 1);
			objOut.writeObject(cal.getTime());
			objOut.writeObject(ConfigManager.REINTENTOS_KEY);
			objOut.close();
			byte[] plainData = bos.toByteArray();
			byte[] encryptedData = cipher.doFinal(plainData);
			MessageDigest sha1 = MessageDigest.getInstance("SHA1", "BC");
			byte[] digestReintentosKey = sha1
					.digest(ConfigManager.REINTENTOS_KEY);
			String hexDigestReintentos = Hex
					.encodeHexString(digestReintentosKey);
			Preferences p = Preferences.userRoot();
			p.put(hexDigestReintentos, Hex.encodeHexString(encryptedData));
			p.flush();
		} catch (Exception e) {
			throw new PluginException("error.code.dieciseis");
		}
	}

	private void guardarLicencia(Date fechaVencimiento, String winkey)
			throws PluginException {
		try {
			Key tdeskey = load3DESKey();
			String strRandom = Hex.encodeHexString(_barrRandom);
			IvParameterSpec iv = new IvParameterSpec(strRandom.substring(2)
					.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, tdeskey, iv);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(bos);
			Calendar cal = Calendar.getInstance(ConfigManager.PLUGIN_LOCALE);
			objOut.writeObject(cal.getTime());
			objOut.writeObject(fechaVencimiento);
			objOut.writeObject(winkey);
			objOut.close();
			byte[] plainData = bos.toByteArray();
			byte[] encryptedData = cipher.doFinal(plainData);
			MessageDigest sha1 = MessageDigest.getInstance("SHA1", "BC");
			byte[] digestWinKey = sha1.digest(winkey.getBytes());
			String hexDigestWinKey = Hex.encodeHexString(digestWinKey);
			Preferences p = Preferences.userRoot();
			p.put(hexDigestWinKey,
					Hex.encodeHexString(_barrRandom)
							+ Hex.encodeHexString(encryptedData));
			p.flush();
		} catch (Exception e) {
			throw new PluginException("error.code.dieciseis");
		}
	}

	private Key load3DESKey() throws PluginException {
		SecretKey secretKey = null;
		InputStream keyStream = PluginSecurityManager.class
				.getResourceAsStream("/images/icon.ico");
		if (keyStream == null) {
			throw new PluginException("error.code.cinco", false);
		}
		try {
			BufferedInputStream bufferedInput = new BufferedInputStream(
					keyStream);
			StringBuffer keyBuffer = new StringBuffer();
			byte[] buffer = new byte[512];
			int bytesRead = 0;
			while ((bytesRead = bufferedInput.read(buffer)) != -1) {
				keyBuffer.append(new String(buffer, 0, bytesRead));
			}
			String key1 = keyBuffer.substring(0, 256);
			String key2 = keyBuffer.substring(256, 580);
			String key3 = keyBuffer.substring(580);
			Cipher cipher = Cipher.getInstance("RSA", "BC");
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(
					Hex.decodeHex(key2.toCharArray()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			byte[] tdesKey = cipher.doFinal(Hex.decodeHex(key1.toCharArray()));
			DESedeKeySpec desKeySpec = new DESedeKeySpec(tdesKey);
			SecretKeyFactory keyFactory2 = SecretKeyFactory.getInstance(
					"DESede", "BC");
			secretKey = keyFactory2.generateSecret(desKeySpec);
			Signature signature = Signature.getInstance("SHA1withRSA", "BC");
			signature.initVerify(pubKey);
			signature.update(secretKey.getEncoded());
			byte[] bkey3 = Hex.decodeHex(key3.toCharArray());
			if (!signature.verify(bkey3)) {
				throw new PluginException("error.code.trece", false);
			}
		} catch (PluginException e) {
			throw e;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new PluginException("error.code.cuatro", false);
		}
		return secretKey;
	}

	private byte[] createRandomString(int iRandomLength) throws PluginException {
		byte[] result = new byte[iRandomLength];
		try {
			SecureRandom secureRandom = FixedSecureRandom
					.getInstance("SHA1PRNG");
			secureRandom.nextBytes(result);
		} catch (Exception e) {
			throw new PluginException("error.code.siete", false);
		}
		return result;
	}

	private static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);

	}

	private static String getWinProductId() throws Exception {
		String result = "";
		File file = File.createTempFile("winprod", ".vbs");
		file.deleteOnExit();
		FileWriter fw = new java.io.FileWriter(file);

		String vbs = "On Error Resume Next \n"
				+ "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
				+ "If Err.Number <> 0 Then \n"
				+ "    Wscript.Echo \"nowmi\" \n" + "    WScript.Quit \n"
				+ "End If \n" + "Set colItems = objWMIService.ExecQuery _ \n"
				+ "   (\"Select * from Win32_OperatingSystem\") \n"
				+ "For Each objItem in colItems \n"
				+ "    Wscript.Echo objItem.SerialNumber \n"
				+ "    exit for \n" + "Next \n";
		fw.write(vbs);
		fw.close();
		Process p = Runtime.getRuntime().exec(
				"cscript //NoLogo " + file.getPath());
		BufferedReader input = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = input.readLine()) != null) {
			result += line;
		}
		input.close();
		return result.trim();
	}

	private String getWindowsProductId() throws PluginException {
		String productId = null;
		if (!isWindows()) {
			throw new PluginException("error.code.ocho", false);
		}
		try {
			productId = getWinProductId();
			if (productId.equals("nowmi")) {
				throw new PluginException("error.code.nueve", false);
			}
			if (productId.length() != ConfigManager.WIN_KEY_SIZE) {
				throw new PluginException("error.code.catorce", false);
			}
		} catch (Exception e) {
			throw new PluginException("error.code.catorce", false);
		}
		return productId.replaceAll("-", "");
	}

	private Date validarFechas(byte[] barrFechas) throws PluginException {
		Calendar cal = Calendar.getInstance(ConfigManager.PLUGIN_LOCALE);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// Leer data del arreglo
		int day1 = barrFechas[0];
		int month1 = barrFechas[1];
		int year1 = barrFechas[2];
		int day2 = barrFechas[3];
		int month2 = barrFechas[4];
		int year2 = barrFechas[5];
		// Crear calendario con la fecha del servidor
		Calendar calServer = Calendar.getInstance(ConfigManager.PLUGIN_LOCALE);
		calServer.set(Calendar.DATE, day1);
		calServer.set(Calendar.MONTH, month1);
		calServer.set(Calendar.YEAR, 2000 + year1);
		calServer.set(Calendar.HOUR_OF_DAY, 0);
		calServer.set(Calendar.MINUTE, 0);
		calServer.set(Calendar.SECOND, 0);
		calServer.set(Calendar.MILLISECOND, 0);
		Date fechaSistema = cal.getTime();
		Date fechaServer = calServer.getTime();
		if (fechaSistema.compareTo(fechaServer) != 0) {
			throw new PluginException("error.code.doce", _reintentos--);
		}
		// Fecha de vencimiento de la licencia
		calServer.set(Calendar.DATE, day2);
		calServer.set(Calendar.MONTH, month2);
		calServer.set(Calendar.YEAR, 2000 + year2);
		if (fechaSistema.compareTo(fechaServer) > 0) {
			throw new PluginException("error.code.dos", _reintentos--);
		}
		return calServer.getTime();
	}
}