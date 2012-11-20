package com.proasecal.licman.controller;

import javax.persistence.EntityManager;

import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;

import com.proasecal.licman.dao.PatrocinadorDAO;
import com.proasecal.licman.vo.PatrocinadorVO;

public class PatrocinadorFormController extends FormController {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(PatrocinadorFormController.class);
	private PatrocinadorVO _patrocinador;

	public PatrocinadorFormController(PatrocinadorVO p) {
		_patrocinador = p;
	}

	@SuppressWarnings("rawtypes")
	public Response loadData(Class valueObjectClass) {
		EntityManager em = null;
		VOResponse response = null;
		try {
			LOGGER.debug("Consulta de patrocinador en la base de datos");
			PatrocinadorDAO dao = new PatrocinadorDAO();
			PatrocinadorVO p = dao.findPatrocinador(_patrocinador.getId());
			response = new VOResponse(p);
			LOGGER.debug("Operaci\u00F3n exitosa!: " + p);
			return response;
		} catch (Exception ex) {
			LOGGER.error(ex);
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
			PatrocinadorVO p = (PatrocinadorVO) newPersistentObject;
			LOGGER.debug("Agregando patrocinador: " + p);
			PatrocinadorDAO dao = new PatrocinadorDAO();
			dao.save(p);
			response = new VOResponse(p);
			_patrocinador = p;
			LOGGER.debug("Operaci\u00F3n exitosa!");
			return response;
		} catch (Exception ex) {
			LOGGER.error(ex,ex.getCause());
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
			PatrocinadorVO p = (PatrocinadorVO) persistentObject;
			LOGGER.debug("Actualizando patrocinador: " + p);
			PatrocinadorDAO dao = new PatrocinadorDAO();
			dao.update(p);
			response = new VOResponse(p);
			LOGGER.debug("Operaci\u00F3n exitosa!");
			return response;
		} catch (Exception ex) {
			LOGGER.error(ex,ex.getCause());
			return new ErrorResponse(ex.getMessage());
		} finally {
			if (em != null) {
				LOGGER.debug("EntityManager closed: " + em);
				em.close();
			}
		}
	}
}