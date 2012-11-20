package com.proasecal.licman.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.GridDataLocator;

import com.proasecal.licman.dao.PatrocinadorDAO;
import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.gui.DlgPatrocinador;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.PatrocinadorVO;

public class PatrocinadorGridController extends GridController implements
		GridDataLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4832265415609218281L;
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(PatrocinadorGridController.class);
	private GridControl _gridControl;
	private LinkedHashMap<String, Object> _filter;

	public PatrocinadorGridController(GridControl gridControl) {
		_filter = new LinkedHashMap<String, Object>();
		_gridControl = gridControl;
	}

	public void buscar(LinkedHashMap<String, Object> filter) {
		LOGGER.debug("Nueva b\u00FAsqueda");
		_filter = filter;
		_gridControl.reloadData();
	}

	public void enterButton(int rowNumber, ValueObject persistentObject) {
		PatrocinadorVO vo = (PatrocinadorVO) _gridControl.getVOListTableModel()
				.getObjectForRow(rowNumber);
		LOGGER.debug("Editanto patrocinador: " + vo);
		DlgPatrocinador dlg = new DlgPatrocinador(vo, _gridControl);
		dlg.setVisible(true);
	}

	public boolean beforeEditGrid(GridControl grid) {
		int rowNumber = grid.getSelectedRow();
		PatrocinadorVO vo = (PatrocinadorVO) grid.getVOListTableModel()
				.getObjectForRow(rowNumber);
		LOGGER.debug("Editanto patrocinador: " + vo);
		DlgPatrocinador dlg = new DlgPatrocinador(vo, _gridControl);
		dlg.setVisible(true);
		return false;
	}

	public boolean beforeInsertGrid(GridControl grid) {
		LOGGER.debug("Agregando nuevo patrocinador");
		DlgPatrocinador dialog = new DlgPatrocinador(null, _gridControl);
		dialog.setVisible(true);
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response loadData(int action, int startIndex, Map filteredColumns,
			ArrayList currentSortedColumns,
			ArrayList currentSortedVersusColumns, Class valueObjectType,
			Map otherGridParams) {
		EntityManager em = null;
		try {
			LOGGER.debug("Consulta de clientes en la base de datos");
			try {
				PatrocinadorDAO dao = new PatrocinadorDAO();
				int iNumFilas = dao.findPatrocinadorCount(_filter);
				int iRealIndex = startIndex;
				if (action == GridParams.PREVIOUS_BLOCK_ACTION) {
					iRealIndex -= ConfigManager.DATABASE_BLOCK;
				} else if (action == GridParams.LAST_BLOCK_ACTION) {
					iRealIndex = iNumFilas
							- (iNumFilas % ConfigManager.DATABASE_BLOCK);
					if (iRealIndex == iNumFilas) {
						iRealIndex -= ConfigManager.DATABASE_BLOCK;
					}
				}
				List<PatrocinadorVO> lstPatrocinadores = dao.findPatrocinador(
						iRealIndex, ConfigManager.DATABASE_BLOCK,
						currentSortedColumns, currentSortedVersusColumns,
						_filter);
				LOGGER.debug("Consulta exitosa! # de registros: "
						+ lstPatrocinadores.size());
				boolean bMoreRows = ((iRealIndex + ConfigManager.DATABASE_BLOCK) < iNumFilas ? true
						: false);
				return new VOListResponse(lstPatrocinadores, bMoreRows,
						iNumFilas);
			} catch (DatabaseException ex) {
				LOGGER.error(ex);
				return new ErrorResponse(ex.getMessage());
			}
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

	public String[] getExportingFormats() {
		return ConfigManager.EXPORTING_FORMATS;
	}

	public void afterDeleteGrid() {
		LicmanUtils.MsgBox("msg.success.delete.patrocinador");
	}

	@SuppressWarnings("rawtypes")
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		PatrocinadorDAO dao = new PatrocinadorDAO();
		try {
			PatrocinadorVO p = (PatrocinadorVO) persistentObjects.get(0);
			LOGGER.debug("Eliminar patrocinador : " + p);
			dao.delete(p);
			_gridControl.reloadCurrentBlockOfData();
			return new VOResponse(new Boolean(true));
		} catch (DatabaseException ex) {
			LOGGER.error(ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}