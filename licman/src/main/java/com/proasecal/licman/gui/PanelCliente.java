package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import org.openswing.swing.client.DeleteButton;
import org.openswing.swing.client.EditButton;
import org.openswing.swing.client.ExportButton;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.client.InsertButton;
import org.openswing.swing.client.NavigatorBar;
import org.openswing.swing.table.columns.client.CheckBoxColumn;
import org.openswing.swing.table.columns.client.DateTimeColumn;
import org.openswing.swing.table.columns.client.TextColumn;
import org.openswing.swing.util.java.Consts;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.licman.controller.ClienteGridController;

public class PanelCliente extends JPanel {
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$
	/**
	 * 
	 */
	private static final long serialVersionUID = 5900350163776851973L;
	private JTextField txtFldNombreLaboratorio;
	private JTextField txtFldCodigoLaboratorio;
	ClienteGridController controller;	

	@SuppressWarnings("serial")
	public PanelCliente() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:max(61dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));

		JPanel pnlGrupo1 = new JPanel();
		pnlGrupo1.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(pnlGrupo1, "2, 2, fill, fill");
		pnlGrupo1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("top:default"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(30dlu;default):grow"), }));

		JLabel lblCodigoLaboratorio = new JLabel(
				BUNDLE.getString("PanelCliente.lblCodigoLaboratorio.text")); //$NON-NLS-1$
		pnlGrupo1.add(lblCodigoLaboratorio, "2, 2, default, top");

		txtFldCodigoLaboratorio = new JTextField();
		txtFldCodigoLaboratorio.setColumns(20);
		pnlGrupo1.add(txtFldCodigoLaboratorio, "2, 4, left, default");

		JLabel lblNombreCliente = new JLabel(
				BUNDLE.getString("PanelCliente.lblNombreLaboratorio.text")); //$NON-NLS-1$
		pnlGrupo1.add(lblNombreCliente, "2, 6, default, top");

		txtFldNombreLaboratorio = new JTextField();
		txtFldNombreLaboratorio.setColumns(20);
		pnlGrupo1.add(txtFldNombreLaboratorio, "2, 8, left, default");

		JPanel pnlBusquedaButtons = new JPanel();
		pnlGrupo1.add(pnlBusquedaButtons, "2, 10, left, fill");
		pnlBusquedaButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblResultados = new JLabel(
				BUNDLE.getString("PanelCliente.lblResultados.text")); //$NON-NLS-1$
		add(lblResultados, "2, 4, center, default");

		JPanel pnlGrupo2 = new JPanel();
		pnlGrupo2.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(pnlGrupo2, "2, 6, fill, fill");
		pnlGrupo2.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pnlGrupo2.add(panel, BorderLayout.NORTH);
		EditButton btnEdit = new EditButton();
		panel.add(btnEdit);
		final InsertButton btnInsert = new InsertButton();
		panel.add(btnInsert);
		DeleteButton btnDelete = new DeleteButton();
		panel.add(btnDelete);
		ExportButton btnExport = new ExportButton();
		panel.add(btnExport);
		NavigatorBar navigatorBar = new NavigatorBar();
		panel.add(navigatorBar);
		GridControl gridControl = new GridControl();
		controller = new ClienteGridController(gridControl);
		gridControl.setNavBar(navigatorBar);
		gridControl.setInsertButton(btnInsert);
		gridControl.setDeleteButton(btnDelete);
		gridControl.setExportButton(btnExport);
		gridControl.setEditButton(btnEdit);
		gridControl.setColorsInReadOnlyMode(false);
		gridControl.setReorderingAllowed(false);
		gridControl.setAutoLoadData(true);
		gridControl.setController(controller);
		gridControl.setGridDataLocator(controller);
		pnlGrupo2.add(gridControl, BorderLayout.CENTER);
		gridControl.setDefaultQuickFilterCriteria(Consts.CONTAINS);
		gridControl.setValueObjectClassName(BUNDLE
				.getString("PanelCliente.gridControl.valueObjectClassName")); //$NON-NLS-1$
		TextColumn columnNombre = new TextColumn();
		columnNombre.setHeaderColumnName(BUNDLE
				.getString("PanelCliente.columnNombre.headerColumnName")); //$NON-NLS-1$
		columnNombre.autoFitColumn = true;
		columnNombre.setColumnSortable(true);
		columnNombre.setColumnName("codigo_laboratorio");
		gridControl.getColumnContainer().add(columnNombre);

		TextColumn columnVersion = new TextColumn();
		columnVersion.autoFitColumn = true;
		columnVersion.setColumnSortable(true);
		columnVersion.setColumnName("nombre_laboratorio");
		columnVersion.setHeaderColumnName("Nombre del laboratorio");
		gridControl.getColumnContainer().add(columnVersion);

		TextColumn columnTipoPersona = new TextColumn();
		columnTipoPersona.setColumnName("tipo_persona");
		columnTipoPersona.setColumnSortable(true);
		columnTipoPersona.setHeaderColumnName(BUNDLE
				.getString("PanelCliente.columnTipoPersona.headerColumnName")); //$NON-NLS-1$
		columnTipoPersona.autoFitColumn = true;
		gridControl.getColumnContainer().add(columnTipoPersona);

		TextColumn columnaNIT = new TextColumn();
		columnaNIT.setHeaderColumnName(BUNDLE
				.getString("PanelCliente.columnaNIT.headerColumnName")); //$NON-NLS-1$
		columnaNIT.setColumnName("nit");
		columnaNIT.setColumnSortable(true);
		gridControl.getColumnContainer().add(columnaNIT);

		TextColumn columnaNombreContacto = new TextColumn();
		columnaNombreContacto.autoFitColumn = true;
		columnaNombreContacto
				.setHeaderColumnName(BUNDLE
						.getString("PanelCliente.columnaNombreContacto.headerColumnName")); //$NON-NLS-1$
		columnaNombreContacto.setColumnName("nombre_contacto");
		columnaNombreContacto.setColumnSortable(true);
		gridControl.getColumnContainer().add(columnaNombreContacto);

		TextColumn columnaTelefonoContacto = new TextColumn();
		columnaTelefonoContacto.autoFitColumn = true;
		columnaTelefonoContacto.setColumnName("telefono_contacto");
		columnaTelefonoContacto.setColumnSortable(true);
		columnaTelefonoContacto
				.setHeaderColumnName(BUNDLE
						.getString("PanelCliente.columnaTelefonoContacto.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnaTelefonoContacto);

		CheckBoxColumn columnaActivo = new CheckBoxColumn();
		columnaActivo.setHeaderColumnName(BUNDLE
				.getString("PanelCliente.columnaActivo.headerColumnName")); //$NON-NLS-1$
		columnaActivo.setColumnName("activo");
		columnaActivo.autoFitColumn = true;
		gridControl.getColumnContainer().add(columnaActivo);

		DateTimeColumn columnFechaCreacion = new DateTimeColumn();
		columnFechaCreacion.autoFitColumn = true;
		columnFechaCreacion.setColumnName("fecha_creacion");
		columnFechaCreacion.setColumnSortable(true);
		columnFechaCreacion
				.setHeaderColumnName(BUNDLE
						.getString("PanelCliente.columnFechaCreacion.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnFechaCreacion);

		JButton btnBuscar = new JButton(
				BUNDLE.getString("PanelCliente.btnBuscar.text")); //$NON-NLS-1$
		btnBuscar.setIcon(new ImageIcon(PanelCliente.class
				.getResource("/com/proasecal/licman/images/search.png")));
		pnlBusquedaButtons.add(btnBuscar);

		// Actions
		// ****************************************************************

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				txtFldCodigoLaboratorio.requestFocusInWindow();
				btnInsert.setEnabled(true);
			}
		});

		AbstractAction actionBuscar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				LinkedHashMap<String, Object> filtro = new LinkedHashMap<String, Object>();
				filtro.put("nombre_laboratorio",
						txtFldNombreLaboratorio.getText());
				filtro.put("codigo_laboratorio",
						txtFldCodigoLaboratorio.getText());
				controller.buscar(filtro);
			}
		};
		txtFldNombreLaboratorio.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Buscar");
		txtFldNombreLaboratorio.getActionMap().put("Buscar", actionBuscar);
		txtFldCodigoLaboratorio.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Buscar");
		txtFldCodigoLaboratorio.getActionMap().put("Buscar", actionBuscar);
		btnBuscar.addActionListener(actionBuscar);
	}
}
