package com.proasecal.licman.controller;

import javax.persistence.EntityManager;

import org.openswing.swing.form.client.Form;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;

import com.proasecal.licman.dao.LicenciaDAO;
import com.proasecal.licman.exceptions.DESedeEncryptionException;
import com.proasecal.licman.exceptions.FechaIncorrectaException;
import com.proasecal.licman.exceptions.NumeroLicenciaException;
import com.proasecal.licman.svcs.LicmanSecurityManager;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.LicenciaVO;
import com.proasecal.licman.vo.PatrocinadorVO;

public class LicenciaFormController extends FormController {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(LicenciaFormController.class);
	private LicenciaVO _licencia;

	public LicenciaFormController(LicenciaVO licencia) {
		_licencia = licencia;
	}

	public boolean beforeSaveDataInEdit(Form form) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Response loadData(Class valueObjectClass) {
		EntityManager em = null;
		VOResponse response = null;
		try {
			LOGGER.debug("Consulta de licencia en la base de datos");
			LicenciaDAO dao = new LicenciaDAO();
			LicenciaVO c = dao.findLicencia(_licencia.getId());
			response = new VOResponse(c);
			LOGGER.debug("Operaci\u00F3n exitosa!: " + c);
			return response;
		} catch (Exception ex) {
			LOGGER.error(ex, ex.getCause());
			return new ErrorResponse(ex.getMessage());
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public Response insertRecord(ValueObject newPersistentObject)
			throws Exception {
		EntityManager em = null;
		VOResponse response = null;
		try {
			LicenciaVO c = (LicenciaVO) newPersistentObject;
			validarLicencia(c, null);
			LOGGER.debug("Agregando licencia: " + c);
			LicenciaDAO dao = new LicenciaDAO();
			PatrocinadorVO p = c.getPatrocinadorVO();
			if (p != null && p.getId() == null) {
				c.setPatrocinadorVO(null);
			}
			c.setFecha_vencimiento(LicmanUtils.removeTime(c
					.getFecha_vencimiento()));
			dao.save(c);
			response = new VOResponse(c);
			_licencia = c;
			LOGGER.debug("Operaci\u00F3n exitosa!");
			return response;
		} catch (Exception ex) {
			LOGGER.error(ex, ex.getCause());
			return new ErrorResponse(ex.getMessage());
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	public Response updateRecord(ValueObject oldPersistentObject,
			ValueObject persistentObject) throws Exception {
		EntityManager em = null;
		VOResponse response = null;
		try {
			LicenciaVO c = (LicenciaVO) persistentObject;
			String strClaveActivacion = c.getClave_activacion();
			validarLicencia(c, strClaveActivacion.charAt(strClaveActivacion.length()-1));
			LOGGER.debug("Actualizando licencia: " + c);
			LicenciaDAO dao = new LicenciaDAO();
			PatrocinadorVO p = c.getPatrocinadorVO();
			if (p != null && p.getId() == null) {
				c.setPatrocinadorVO(null);
			}
			c.setFecha_vencimiento(LicmanUtils.removeTime(c
					.getFecha_vencimiento()));
			dao.update(c);
			response = new VOResponse(c);
			LOGGER.debug("Operaci\u00F3n exitosa!");
			return response;
		} catch (Exception ex) {
			LOGGER.error(ex, ex.getCause());
			return new ErrorResponse(ex.getMessage());
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}

	private void validarLicencia(LicenciaVO vo, Character lastDigit) throws NumeroLicenciaException,
			DESedeEncryptionException, FechaIncorrectaException {
		LicmanSecurityManager secManager = new LicmanSecurityManager();
		String strRandom = secManager.validarNumeroLicencia(vo
				.getNumero_licencia());
		String actKey = secManager.generarClaveActivacion(strRandom,
				vo.getFecha_vencimiento(), lastDigit);
		vo.setClave_activacion(actKey);
	}

}