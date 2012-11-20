package com.proasecal.licman.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.table.model.client.VOListTableModel;

import com.proasecal.licman.dao.LicenciaDAO;
import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.gui.DlgLicencia;
import com.proasecal.licman.gui.DlgRenovacion;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanSecurityManager;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.LicenciaVO;

public class LicenciaGridController extends GridController implements
		GridDataLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5393806144945545505L;
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(LicenciaGridController.class);
	private static final char charSeparator = ',';
	private GridControl _gridControl;
	private LinkedHashMap<String, Object> _filter;

	public LicenciaGridController(GridControl gridControl) {
		_filter = new LinkedHashMap<String, Object>();
		_gridControl = gridControl;
	}

	public void buscar(LinkedHashMap<String, Object> filter) {
		LOGGER.debug("Nueva b\u00FAsqueda");
		_filter = filter;
		_gridControl.reloadData();
	}

	public void renovar() {
		Set<LicenciaVO> setLicenciasSeleccionadas = new LinkedHashSet<LicenciaVO>();
		try {
			LicmanSecurityManager secManager = new LicmanSecurityManager();
			DlgRenovacion dlgRenovar = new DlgRenovacion();
			dlgRenovar.setVisible(true);
			if (!dlgRenovar.getFueCancelado()) {
				LOGGER.debug("Renovando licencias");
				Date fechaVencimiento = dlgRenovar.getSelectedDate();
				int continuar = validarLicencias(setLicenciasSeleccionadas,
						fechaVencimiento);
				if (continuar == JOptionPane.YES_OPTION
						&& !setLicenciasSeleccionadas.isEmpty()) {
					LOGGER.debug("Licencias para renovar: "
							+ setLicenciasSeleccionadas.size());
					List<LicenciaVO> lstNuevasLicencias = secManager
							.renovarLicencias(setLicenciasSeleccionadas,
									fechaVencimiento);
					LOGGER.debug("Licencias renovadas con \u00E9xito");
					LicenciaDAO dao = new LicenciaDAO();
					dao.renovarLicencias(setLicenciasSeleccionadas,
							lstNuevasLicencias);
					LOGGER.debug("Licencias guardadas con \u00E9xito");
					_gridControl.reloadCurrentBlockOfData();
					LicmanUtils.MsgBox("DlgLicencia.renovacion.exitosa");
				}
			}
			dlgRenovar.dispose();			
		} catch (Exception ex) {
			LOGGER.error(ex);
			LicmanUtils.MsgBox("DlgLicencia.renovacion.failed");
		}
	}

	public Color getBackgroundColor(int row, String attributeName, Object value) {
		LicenciaVO vo = (LicenciaVO) _gridControl.getVOListTableModel()
				.getObjectForRow(row);
		// Renovada
		Date today = LicmanUtils.fechaHoy();
		if (vo.getHijoVO() != null) {
			return Color.RED;
			// Vencida
		} else if (today.after(vo.getFecha_vencimiento())) {
			return Color.YELLOW;
		}
		return Color.GREEN;
	}

	public String[] getExportingFormats() {
		return ConfigManager.EXPORTING_FORMATS;
	}

	public void enterButton(int rowNumber, ValueObject persistentObject) {
		LicenciaVO vo = (LicenciaVO) _gridControl.getVOListTableModel()
				.getObjectForRow(rowNumber);
		LOGGER.debug("Editanto licencia: " + vo);
		DlgLicencia dlg = new DlgLicencia(vo, _gridControl);
		dlg.setVisible(true);
	}

	public boolean beforeEditGrid(GridControl grid) {
		int rowNumber = grid.getSelectedRow();
		LicenciaVO vo = (LicenciaVO) grid.getVOListTableModel()
				.getObjectForRow(rowNumber);
		LOGGER.debug("Editanto licencia: " + vo);
		DlgLicencia dlg = new DlgLicencia(vo, _gridControl);
		dlg.setVisible(true);
		return false;
	}

	public boolean beforeDeleteGrid(GridControl grid) {
		boolean continuar = false;
		int result = LicmanUtils
				.ConfirmDialog("msg.confirmation.delete.licencia");
		if (result == JOptionPane.YES_OPTION) {
			continuar = true;
		}
		return continuar;
	}

	public void afterDeleteGrid() {
		LicmanUtils.MsgBox("msg.success.delete.licencia");
	}

	public boolean beforeInsertGrid(GridControl grid) {
		LOGGER.debug("Agregando nuevo licencia");
		DlgLicencia dialog = new DlgLicencia(null, _gridControl);
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
			LicenciaDAO dao = new LicenciaDAO();
			int iNumFilas = dao.findLicenciasCount(_filter);
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
			LOGGER.debug("Consulta de clientes en la base de datos");
			List<LicenciaVO> lstLicencias = dao.findLicencias(iRealIndex,
					ConfigManager.DATABASE_BLOCK, currentSortedColumns,
					currentSortedVersusColumns, _filter);
			LOGGER.debug("Consulta exitosa! # de registros: "
					+ lstLicencias.size());
			boolean bMoreRows = ((iRealIndex + ConfigManager.DATABASE_BLOCK) < iNumFilas ? true
					: false);
			return new VOListResponse(lstLicencias, bMoreRows, iNumFilas);
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

	@SuppressWarnings("rawtypes")
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		LicenciaDAO dao = new LicenciaDAO();
		List<LicenciaVO> lstLicenciasSeleccionadas = new ArrayList<LicenciaVO>();
		for (Object obj : persistentObjects) {
			lstLicenciasSeleccionadas.add((LicenciaVO) obj);
		}
		try {
			LOGGER.debug("Eliminar licencias..");
			dao.delete(lstLicenciasSeleccionadas);
			_gridControl.reloadCurrentBlockOfData();
			return new VOResponse(new Boolean(true));
		} catch (DatabaseException ex) {
			LOGGER.error(ex);
			return new ErrorResponse(ex.getMessage());
		}
	}

	private int validarLicencias(Set<LicenciaVO> setLicenciasxRenovar,
			Date fechaVencimiento) throws DatabaseException {
		int result = 0;
		StringBuffer strBfrValidation = new StringBuffer();
		StringBuffer strBfrClienteNoActivo = new StringBuffer();
		StringBuffer strBfrProductoNoActivo = new StringBuffer();
		StringBuffer strBfrLicenciaYaRenovada = new StringBuffer();
		StringBuffer strBfrFechaIncorrecta = new StringBuffer();
		int[] arrSelectedRows = _gridControl.getTable().getSelectedRows();
		if (arrSelectedRows.length > 0) {
			Set<LicenciaVO> setSelectedLicencias = new LinkedHashSet<LicenciaVO>();
			for (int i : arrSelectedRows) {
				VOListTableModel tableModel = _gridControl
						.getVOListTableModel();
				LicenciaVO lic = (LicenciaVO) tableModel.getObjectForRow(i);
				setSelectedLicencias.add(lic);
			}
			LicenciaDAO dao = new LicenciaDAO();
			dao.refreshLicencias(setSelectedLicencias);
			// Validar licencias seleccionadas
			for (LicenciaVO lic : setSelectedLicencias) {
				// ya ha sido renovada
				if (lic.getHijoVO() != null) {
					strBfrLicenciaYaRenovada.append(lic.getId());
					strBfrLicenciaYaRenovada.append(charSeparator);
				}// producto no activo
				else if (!lic.getProductoVO().getActivo()) {
					strBfrProductoNoActivo.append(lic.getId());
					strBfrProductoNoActivo.append(charSeparator);
				}// cliente no activo
				else if (!lic.getClienteVO().getActivo()) {
					strBfrClienteNoActivo.append(lic.getId());
					strBfrClienteNoActivo.append(charSeparator);
				}// fecha de renovación incorrecta
				else if (fechaVencimiento.compareTo(lic.getFecha_vencimiento()) <= 0) {
					strBfrFechaIncorrecta.append(lic.getId());
					strBfrFechaIncorrecta.append(charSeparator);
				} else {
					setLicenciasxRenovar.add(lic);
				}
			}
			// Mostrar mensajes de error
			String strMsgValidas = ConfigManager
					.getString("DlgRenovacion.licencias.validas.text");
			String strMsgErrorNoValidas = ConfigManager
					.getString("DlgRenovacion.licencias.novalidas.text");
			String strMsgErrorYaRenovadas = ConfigManager
					.getString("DlgRenovacion.yarenovadas.text");
			String strMsgErrorFecha = ConfigManager
					.getString("DlgRenovacion.fechaincorrecta.text");
			String strMsgErrorProdNoActivo = ConfigManager
					.getString("DlgRenovacion.producto.noactivo.text");
			String strMsgErrorClienteNoActivo = ConfigManager
					.getString("DlgRenovacion.cliente.noactivo.text");
			String strMsgContinuar = ConfigManager
					.getString("DlgRenovacion.continuar.text");
			strBfrValidation.append(strMsgValidas);
			strBfrValidation.append(' ');
			strBfrValidation.append(setLicenciasxRenovar.size());
			strBfrValidation.append('\n');
			if (strBfrLicenciaYaRenovada.length() > 0
					|| strBfrClienteNoActivo.length() > 0
					|| strBfrProductoNoActivo.length() > 0
					|| strBfrFechaIncorrecta.length() > 0) {
				strBfrValidation.append(strMsgErrorNoValidas);
				strBfrValidation.append('\n');
				if (strBfrLicenciaYaRenovada.length() > 0) {
					strBfrValidation.append(strMsgErrorYaRenovadas);
					strBfrValidation.append(' ');
					strBfrValidation
							.append(strBfrLicenciaYaRenovada
									.deleteCharAt(strBfrLicenciaYaRenovada
											.length() - 1));
					strBfrValidation.append('\n');
				}
				if (strBfrClienteNoActivo.length() > 0) {
					strBfrValidation.append(strMsgErrorClienteNoActivo);
					strBfrValidation.append(' ');
					strBfrValidation.append(strBfrClienteNoActivo
							.deleteCharAt(strBfrClienteNoActivo.length() - 1));
					strBfrValidation.append('\n');
				}
				if (strBfrProductoNoActivo.length() > 0) {
					strBfrValidation.append(strMsgErrorProdNoActivo);
					strBfrValidation.append(' ');
					strBfrValidation.append(strBfrProductoNoActivo
							.deleteCharAt(strBfrProductoNoActivo.length() - 1));
					strBfrValidation.append('\n');
				}
				if (strBfrFechaIncorrecta.length() > 0) {
					strBfrValidation.append(strMsgErrorFecha);
					strBfrValidation.append(' ');
					strBfrValidation.append(strBfrFechaIncorrecta
							.deleteCharAt(strBfrFechaIncorrecta.length() - 1));
					strBfrValidation.append('\n');
				}
			}
			strBfrValidation.append(strMsgContinuar);
			result = LicmanUtils.ConfirmDialogMsg(strBfrValidation.toString());
		} else {
			LicmanUtils.MsgBox("DlgLicencia.renovacion.noseleccion.text");
			result = JOptionPane.NO_OPTION;
		}
		return result;
	}

}