package com.proasecal.licman.controller;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.tree.java.OpenSwingTreeNode;

import com.proasecal.licman.dao.ProductoDAO;
import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.vo.ProductoVO;

public class ProductoLookupController extends LookupController {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ProductoLookupController.class);

	@SuppressWarnings("static-access")
	public ProductoLookupController() {
		this.setLookupDataLocator(new LookupDataLocator() {

			public Response validateCode(String nombre) {
				try {
					ProductoDAO dao = new ProductoDAO();
					List<ProductoVO> lstProductos = dao.findProductosActivos(nombre);
					if (lstProductos.size() > 0) {
						return new VOListResponse(lstProductos, false,
								lstProductos.size());
					} else {
						return new ErrorResponse(ConfigManager
								.getString("error.nombre.producto"));
					}
				} catch (DatabaseException ex) {
					LOGGER.error(ex);
					return new ErrorResponse(ex.getMessage());
				}
			}

			@SuppressWarnings("rawtypes")
			public Response loadData(int action, int startIndex,
					Map filteredColumns, ArrayList currentSortedColumns,
					ArrayList currentSortedVersusColumns, Class valueObjectType) {
				try {
					ProductoDAO dao = new ProductoDAO();
					List<ProductoVO> lstProductos = dao.findProductosActivos("");
					if (lstProductos.size() > 0) {
						return new VOListResponse(lstProductos, false,
								lstProductos.size());
					} else {
						return new ErrorResponse(ConfigManager
								.getString("error.empty.table"));
					}
				} catch (DatabaseException ex) {
					LOGGER.error(ex);
					return new ErrorResponse(ex.getMessage());
				}
			}

			public Response getTreeModel(JTree tree) {
				return new VOResponse(new DefaultTreeModel(
						new OpenSwingTreeNode()));
			}
		});
		this.setFrameTitle(ConfigManager.getString("DlgLicencia.seleccionar.producto.text"));
		this.setLookupValueObjectClassName("com.proasecal.licman.vo.ProductoVO");
		this.addLookup2ParentLink("nombreVersion", "productoVO.nombreVersion");
		this.addLookup2ParentLink("id", "productoVO.id");
		this.setAllColumnVisible(false);
		this.setVisibleColumn("nombre", true);
		this.setVisibleColumn("version", true);
		this.setVisibleColumn("descripcion", true);
		this.setFramePreferedSize(new Dimension(350, 500));
		this.setCodeSelectionWindow(super.GRID_AND_PANEL_FRAME);
	}
}
