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

import com.proasecal.licman.dao.PatrocinadorDAO;
import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.vo.PatrocinadorVO;

public class PatrocinadorLookupController extends LookupController {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(PatrocinadorLookupController.class);

	@SuppressWarnings("static-access")
	public PatrocinadorLookupController() {
		this.setLookupDataLocator(new LookupDataLocator() {

			public Response validateCode(String nombre) {
				try {
					PatrocinadorDAO dao = new PatrocinadorDAO();
					List<PatrocinadorVO> lstPatrocinadores = dao.findPatrocinadorActivos(nombre);
					if (lstPatrocinadores.size() > 0) {
						return new VOListResponse(lstPatrocinadores, false,
								lstPatrocinadores.size());
					} else {
						return new ErrorResponse(ConfigManager
								.getString("error.nombre.patrocinador"));
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
					PatrocinadorDAO dao = new PatrocinadorDAO();
					List<PatrocinadorVO> lstPatrocinadores = dao.findPatrocinadorActivos("");
					if (lstPatrocinadores.size() > 0) {
						return new VOListResponse(lstPatrocinadores, false,
								lstPatrocinadores.size());
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
		this.setFrameTitle(ConfigManager.getString("DlgLicencia.seleccionar.patrocinador.text"));
		this.setLookupValueObjectClassName("com.proasecal.licman.vo.PatrocinadorVO");
		this.addLookup2ParentLink("nombre", "patrocinadorVO.nombre");
		this.addLookup2ParentLink("id", "patrocinadorVO.id");
		this.setAllColumnVisible(false);
		this.setVisibleColumn("nombre", true);
		this.setVisibleColumn("descripcion", true);
		this.setFramePreferedSize(new Dimension(350, 500));
		this.setCodeSelectionWindow(super.GRID_AND_PANEL_FRAME);
	}
}
