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

import com.proasecal.licman.dao.ClienteDAO;
import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.vo.ClienteVO;

public class ClienteLookupController extends LookupController {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ClienteLookupController.class);

	@SuppressWarnings("static-access")
	public ClienteLookupController() {
		this.setLookupDataLocator(new LookupDataLocator() {

			public Response validateCode(String code) {
				try {
					ClienteDAO dao = new ClienteDAO();
					List<ClienteVO> lstClientes = dao.findClientesActivosByCode(code);
					if (lstClientes.size() > 0) {
						return new VOListResponse(lstClientes, false,
								lstClientes.size());
					} else {
						return new ErrorResponse(ConfigManager
								.getString("error.nombre.cliente"));
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
					ClienteDAO dao = new ClienteDAO();
					List<ClienteVO> lstClientes = dao.findClientesActivosByCode("");
					if (lstClientes.size() > 0) {
						return new VOListResponse(lstClientes, false,
								lstClientes.size());
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
		this.setLookupValueObjectClassName("com.proasecal.licman.vo.ClienteVO");
		this.addLookup2ParentLink("nombre_laboratorio", "clienteVO.nombre_laboratorio");
		this.addLookup2ParentLink("id", "clienteVO.id");
		this.setAllColumnVisible(false);
		this.setVisibleColumn("codigo_laboratorio", true);
		this.setVisibleColumn("nombre_laboratorio", true);
		this.setFramePreferedSize(new Dimension(350, 500));
		this.setCodeSelectionWindow(super.GRID_AND_PANEL_FRAME);
	}
}
