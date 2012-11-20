package com.proasecal.licman.controller;

import javax.persistence.EntityManager;

import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;

import com.proasecal.licman.dao.ProductoDAO;
import com.proasecal.licman.vo.ProductoVO;

public class ProductoFormController extends FormController {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ProductoFormController.class);
	private ProductoVO _producto;

	public ProductoFormController(ProductoVO theProduct) {
		_producto = theProduct;
	}

	@SuppressWarnings("rawtypes")
	public Response loadData(Class valueObjectClass) {
		EntityManager em = null;
		VOResponse response = null;
		try {
			LOGGER.debug("Consulta de producto en la base de datos");
			ProductoDAO dao = new ProductoDAO();
			ProductoVO p = dao.findProducto(_producto.getId());
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
			ProductoVO p = (ProductoVO) newPersistentObject;
			LOGGER.debug("Agregando producto: " + p);
			ProductoDAO dao = new ProductoDAO();
			dao.save(p);
			response = new VOResponse(p);
			_producto = p;
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
			ProductoVO p = (ProductoVO) persistentObject;
			LOGGER.debug("Actualizando producto: " + p);
			ProductoDAO dao = new ProductoDAO();
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