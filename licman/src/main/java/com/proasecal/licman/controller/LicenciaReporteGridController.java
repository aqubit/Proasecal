package com.proasecal.licman.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.GridDataLocator;

import com.proasecal.licman.dao.LicenciaDAO;
import com.proasecal.licman.exceptions.DatabaseException;
import com.proasecal.licman.gui.PanelReporteLicencia.RangoLicencias;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.LicenciaVO;

public class LicenciaReporteGridController extends GridController implements
		GridDataLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5393806144945545505L;
	private RangoLicencias _rango;
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(LicenciaReporteGridController.class);

	public LicenciaReporteGridController(RangoLicencias rango) {
		_rango = rango;
	}

	public boolean beforeEditGrid(GridControl grid) {
		return false;
	}

	public boolean beforeInsertGrid(GridControl grid) {
		return false;
	}
	
	public String[] getExportingFormats() {
		return ConfigManager.EXPORTING_FORMATS;
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response loadData(int action, int startIndex, Map filteredColumns,
			ArrayList currentSortedColumns,
			ArrayList currentSortedVersusColumns, Class valueObjectType,
			Map otherGridParams) {
		try {
			LOGGER.debug("Consulta de licencias en la base de datos: " + _rango);
			LinkedHashMap<String, Object> filtro = new LinkedHashMap<String, Object>();
			LicenciaDAO dao = new LicenciaDAO();
			List<LicenciaVO> lstLicencias = null;
			if (_rango.equals(RangoLicencias.EXPIRADAS)) {
				Date fechaVencimiento1 = new Date(0L);
				Date fechaVencimiento2 = LicmanUtils.diffHoy(-1);
				filtro.put("fecha_vencimiento", fechaVencimiento1);
				filtro.put("fecha_vencimiento2", fechaVencimiento2);
				filtro.put("fue_renovada", false);
			} else if (_rango.equals(RangoLicencias.POR_EXPIRAR)) {
				Date fechaVencimiento1 = LicmanUtils.fechaHoy();
				Date fechaVencimiento2 = LicmanUtils.diffHoy(30);
				filtro.put("fecha_vencimiento", fechaVencimiento1);
				filtro.put("fecha_vencimiento2", fechaVencimiento2);
				filtro.put("fue_renovada", false);
			} else {
				Date fechaCreacion1 = LicmanUtils.diffHoy(-30);
				Date fechaCreacion2 = LicmanUtils.diffHoy(1);
				filtro.put("fecha_creacion", fechaCreacion1);
				filtro.put("fecha_creacion2", fechaCreacion2);
				filtro.put("fue_renovada", false);
			}
			lstLicencias = dao.findLicencias(startIndex, 50,
					currentSortedColumns, currentSortedVersusColumns,
					filtro);
			return new VOListResponse(lstLicencias, false, lstLicencias.size());
		} catch (DatabaseException ex) {
			LOGGER.error(ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}