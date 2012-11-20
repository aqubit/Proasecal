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

import com.proasecal.licman.dao.ClienteDAO;
import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.gui.DlgCliente;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.ClienteVO;

public class ClienteGridController extends GridController implements
		GridDataLocator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7538255994349799614L;
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ClienteGridController.class);
	private LinkedHashMap<String, Object> _filter;
	private GridControl _gridControl;
	
	public ClienteGridController(GridControl gridControl) {
		_filter = new LinkedHashMap<String, Object>();
		_gridControl = gridControl;
	}

	public void buscar(LinkedHashMap<String, Object> filter) {
		LOGGER.debug("Nueva b\u00FAsqueda");
		_filter = filter;
		_gridControl.reloadData();
	}

	public void enterButton(int rowNumber, ValueObject persistentObject) {
		ClienteVO vo = (ClienteVO) _gridControl.getVOListTableModel()
				.getObjectForRow(rowNumber);
		LOGGER.debug("Editanto cliente: " + vo);
		DlgCliente dlg = new DlgCliente(vo, _gridControl);
		dlg.setVisible(true);
	}

	public boolean beforeEditGrid(GridControl grid) {
		int rowNumber = grid.getSelectedRow();
		ClienteVO vo = (ClienteVO) grid.getVOListTableModel().getObjectForRow(
				rowNumber);
		LOGGER.debug("Editanto cliente: " + vo);
		DlgCliente dlg = new DlgCliente(vo, _gridControl);
		dlg.setVisible(true);
		return false;
	}

	public boolean beforeInsertGrid(GridControl grid) {
		LOGGER.debug("Agregando nuevo cliente");
		DlgCliente dlg = new DlgCliente(null, _gridControl);
		dlg.setVisible(true);
		return false;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Response loadData(int action, int startIndex, Map filteredColumns,
			ArrayList currentSortedColumns,
			ArrayList currentSortedVersusColumns, Class valueObjectType,
			Map otherGridParams) {
		EntityManager em = null;
		try {
			LOGGER.debug("Consulta de clientes en la base de datos");
			ClienteDAO dao = new ClienteDAO();
			int iNumFilas = dao.findClientesCount(_filter);
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
			List<ClienteVO> lstClientes = dao.findClientes(
					iRealIndex,
					ConfigManager.DATABASE_BLOCK, currentSortedColumns, currentSortedVersusColumns,
					_filter);
			LOGGER.debug("Consulta exitosa! # de registros: "
					+ lstClientes.size());
			boolean bMoreRows = ((iRealIndex + ConfigManager.DATABASE_BLOCK) < iNumFilas ? true
					: false);
			return new VOListResponse(lstClientes, bMoreRows,
					iNumFilas);
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
		LicmanUtils.MsgBox("msg.success.delete.cliente");
	}

	@SuppressWarnings("rawtypes")
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		ClienteDAO dao = new ClienteDAO();
		try {
			ClienteVO prod = (ClienteVO) persistentObjects.get(0);
			LOGGER.debug("Eliminar cliente : " + prod);
			dao.delete(prod);
			_gridControl.reloadCurrentBlockOfData();
			return new VOResponse(new Boolean(true));
		} catch (DatabaseException ex) {
			LOGGER.error(ex);
			return new ErrorResponse(ex.getMessage());
		}	
	}
}