package com.proasecal.licman.controller;

import javax.persistence.EntityManager;

import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;

import com.proasecal.licman.dao.ClienteDAO;
import com.proasecal.licman.vo.ClienteVO;

public class ClienteFormController extends FormController {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ClienteFormController.class);
	private ClienteVO _cliente;

	public ClienteFormController(ClienteVO theProduct) {
		_cliente = theProduct;
	}

	@SuppressWarnings("rawtypes")
	public Response loadData(Class valueObjectClass) {
		EntityManager em = null;
		VOResponse response = null;
		try {
			LOGGER.debug("Consulta de cliente en la base de datos");
			ClienteDAO dao = new ClienteDAO();
			ClienteVO c = dao.findCliente(_cliente.getId());
			response = new VOResponse(c);
			LOGGER.debug("Operaci\u00F3n exitosa!: " + c);
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

	public Response insertRecord(ValueObject newPersistentObject)
			throws Exception {
		EntityManager em = null;
		VOResponse response = null;
		try {
			ClienteVO c = (ClienteVO) newPersistentObject;
			LOGGER.debug("Agregando cliente: " + c);
			ClienteDAO dao = new ClienteDAO();
			dao.save(c);
			response = new VOResponse(c);
			_cliente = c;
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
			ClienteVO c = (ClienteVO) persistentObject;
			LOGGER.debug("Actualizando cliente: " + c);
			ClienteDAO dao = new ClienteDAO();
			dao.update(c);
			response = new VOResponse(c);
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