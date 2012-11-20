package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.openswing.swing.client.ExportButton;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.client.NavigatorBar;
import org.openswing.swing.table.columns.client.DateTimeColumn;
import org.openswing.swing.table.columns.client.TextColumn;
import org.openswing.swing.util.java.Consts;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.licman.controller.LicenciaReporteGridController;
import org.openswing.swing.table.columns.client.IntegerColumn;

public class PanelReporteLicencia extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8430027864574125274L;
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$
	private GridControl _gridControl;

	public enum RangoLicencias {
		EXPIRADAS, POR_EXPIRAR, RECIENTES
	}

	/**
	 * 
	 */

	public PanelReporteLicencia(String strTitle, RangoLicencias rango) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));

		JLabel lbTitle = new JLabel(BUNDLE.getString(strTitle));
		lbTitle.setHorizontalAlignment(SwingConstants.LEFT);
		add(lbTitle, "2, 2");

		JPanel pnlGrupo2 = new JPanel();
		pnlGrupo2.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(pnlGrupo2, "2, 4, fill, fill");
		pnlGrupo2.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pnlGrupo2.add(panel, BorderLayout.NORTH);
		ExportButton btnExport = new ExportButton();
		panel.add(btnExport);
		NavigatorBar navigatorBar = new NavigatorBar();
		panel.add(navigatorBar);
		_gridControl = new GridControl();
		LicenciaReporteGridController controller = new LicenciaReporteGridController(rango);
		_gridControl.setNavBar(navigatorBar);
		_gridControl.setExportButton(btnExport);
		_gridControl.setColorsInReadOnlyMode(false);
		_gridControl.setReorderingAllowed(false);
		_gridControl.setAutoLoadData(true);
		_gridControl.setController(controller);
		_gridControl.setGridDataLocator(controller);
		pnlGrupo2.add(_gridControl, BorderLayout.CENTER);
		_gridControl.setDefaultQuickFilterCriteria(Consts.CONTAINS);
		_gridControl
				.setValueObjectClassName("com.proasecal.licman.vo.LicenciaVO");
		
		IntegerColumn columnId = new IntegerColumn();
		columnId.setColumnSortable(true);
		columnId.autoFitColumn = true;
		columnId.setColumnName("id");
		_gridControl.getColumnContainer().add(columnId);
		TextColumn columnProducto = new TextColumn();
		columnProducto
				.setHeaderColumnName(BUNDLE
						.getString("PanelReporteLicencia.columnProducto.headerColumnName")); //$NON-NLS-1$
		columnProducto.autoFitColumn = true;
		columnProducto.setColumnSortable(true);
		columnProducto.setColumnName("productoVO.nombre");
		_gridControl.getColumnContainer().add(columnProducto);
		
		TextColumn columnVersion = new TextColumn();
		columnVersion.autoFitColumn = true;
		columnVersion.setColumnSortable(true);
		columnVersion.setHeaderColumnName(BUNDLE.getString("PanelProducto.columnVersion.headerColumnName")); //$NON-NLS-1$
		columnVersion.setColumnName("productoVO.version");
		_gridControl.getColumnContainer().add(columnVersion);

		TextColumn columnCliente = new TextColumn();
		columnCliente.autoFitColumn = true;
		columnCliente.setColumnSortable(true);
		columnCliente.setColumnName("clienteVO.nombre_laboratorio");
		columnCliente
				.setHeaderColumnName(BUNDLE
						.getString("PanelReporteLicencia.columnCliente.headerColumnName")); //$NON-NLS-1$
		_gridControl.getColumnContainer().add(columnCliente);

		TextColumn columnDepartamento = new TextColumn();
		columnDepartamento.setColumnSortable(true);
		columnDepartamento.setColumnName("depto_activacion");
		columnDepartamento
				.setHeaderColumnName(BUNDLE
						.getString("PanelReporteLicencia.columnDepartamento.headerColumnName")); //$NON-NLS-1$
		columnDepartamento.autoFitColumn = true;
		_gridControl.getColumnContainer().add(columnDepartamento);

		TextColumn columnPersonaActiva = new TextColumn();
		columnPersonaActiva.setColumnSortable(true);
		columnPersonaActiva.autoFitColumn = true;
		columnPersonaActiva.setColumnName("nombre_persona");
		columnPersonaActiva.setHeaderColumnName(BUNDLE
				.getString("PanelReporteLicencia.textColumn.headerColumnName")); //$NON-NLS-1$
		_gridControl.getColumnContainer().add(columnPersonaActiva);

		TextColumn columnTelPersonaActiva = new TextColumn();
		columnTelPersonaActiva.setColumnSortable(true);
		columnTelPersonaActiva.autoFitColumn = true;
		columnTelPersonaActiva
				.setHeaderColumnName(BUNDLE
						.getString("PanelReporteLicencia.columnTelPersonaActiva.headerColumnName")); //$NON-NLS-1$
		columnTelPersonaActiva.setColumnName("telefono_persona");
		_gridControl.getColumnContainer().add(columnTelPersonaActiva);

		DateTimeColumn columnFechaVencimiento = new DateTimeColumn();
		columnFechaVencimiento.setColumnSortable(true);
		columnFechaVencimiento.autoFitColumn = true;
		columnFechaVencimiento.setColumnName("fecha_vencimiento");
		columnFechaVencimiento
				.setHeaderColumnName(BUNDLE
						.getString("PanelReporteLicencia.columnFechaVencimiento.headerColumnName")); //$NON-NLS-1$
		columnFechaVencimiento.setTimeFormat("");
		_gridControl.getColumnContainer().add(columnFechaVencimiento);

		DateTimeColumn columnFechaCreacion = new DateTimeColumn();
		columnFechaCreacion.setColumnSortable(true);
		GridBagLayout gbl_columnFechaCreacion = (GridBagLayout) columnFechaCreacion
				.getLayout();
		gbl_columnFechaCreacion.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_columnFechaCreacion.rowHeights = new int[] { 0, 0, 0 };
		gbl_columnFechaCreacion.columnWeights = new double[] { 0.0 };
		gbl_columnFechaCreacion.columnWidths = new int[] { 0 };
		columnFechaCreacion
				.setHeaderColumnName(BUNDLE
						.getString("PanelReporteLicencia.dateTimeColumn.headerColumnName")); //$NON-NLS-1$
		columnFechaCreacion.setColumnName("fecha_creacion");
		columnFechaCreacion.autoFitColumn = true;
		columnFechaCreacion.setAutoFitColumn(true);
		_gridControl.getColumnContainer().add(columnFechaCreacion);

	}

	@Override
	public void update(Observable o, Object arg) {
		_gridControl.reloadData();
	}

}
