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
import com.proasecal.licman.controller.ProductoGridController;

public class PanelProducto extends JPanel {
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$
	/**
	 * 
	 */
	private static final long serialVersionUID = 5900350163776851973L;
	private JTextField txtFldNombreProducto;
	ProductoGridController controller;

	@SuppressWarnings("serial")
	public PanelProducto() {
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
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(30dlu;default):grow"), }));

		JLabel lblNombreProducto = new JLabel(
				BUNDLE.getString("PanelProducto.lblNombreProducto.text")); //$NON-NLS-1$
		pnlGrupo1.add(lblNombreProducto, "2, 2, default, top");

		txtFldNombreProducto = new JTextField();
		txtFldNombreProducto.setText("");
		txtFldNombreProducto.setColumns(20);
		pnlGrupo1.add(txtFldNombreProducto, "2, 4, left, default");

		JPanel pnlBusquedaButtons = new JPanel();
		pnlGrupo1.add(pnlBusquedaButtons, "2, 6, left, fill");
		pnlBusquedaButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblResultados = new JLabel(
				BUNDLE.getString("PanelProducto.lblResultados.text")); //$NON-NLS-1$
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
		controller = new ProductoGridController(gridControl);
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
				.getString("PanelProducto.gridControl.valueObjectClassName"));
		TextColumn columnNombre = new TextColumn();
		columnNombre.autoFitColumn = true;
		columnNombre.setColumnSortable(true);
		columnNombre.setColumnName("nombre");
		columnNombre.setHeaderColumnName(BUNDLE
				.getString("PanelProducto.textColumn.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnNombre);

		TextColumn columnVersion = new TextColumn();
		columnVersion.autoFitColumn = true;
		columnVersion.setColumnSortable(true);
		columnVersion.setColumnName("version");
		columnVersion.setHeaderColumnName(BUNDLE
				.getString("PanelProducto.columnVersion.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnVersion);

		CheckBoxColumn columnActivo = new CheckBoxColumn();
		columnActivo.autoFitColumn = true;
		columnActivo.setColumnName("activo");
		columnActivo.setHeaderColumnName(BUNDLE
				.getString("PanelProducto.columnActivo.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnActivo);

		DateTimeColumn columnFechaCreacion = new DateTimeColumn();
		columnFechaCreacion.autoFitColumn = true;
		columnFechaCreacion.setColumnName("fecha_creacion");
		columnFechaCreacion.setColumnSortable(true);
		columnFechaCreacion.setHeaderColumnName(BUNDLE
				.getString("PanelProducto.dateColumn.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnFechaCreacion);

		JButton btnBuscar = new JButton(
				BUNDLE.getString("PanelProducto.btnBuscar.text")); //$NON-NLS-1$
		btnBuscar.setIcon(new ImageIcon(PanelProducto.class
				.getResource("/com/proasecal/licman/images/search.png")));
		pnlBusquedaButtons.add(btnBuscar);

		// Actions
		// ****************************************************************

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				txtFldNombreProducto.requestFocusInWindow();
				btnInsert.setEnabled(true);
			}
		});

		AbstractAction actionBuscar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				LinkedHashMap<String, Object> filtro = new LinkedHashMap<String, Object>();
				filtro.put("nombre", txtFldNombreProducto.getText());		
				controller.buscar(filtro);
				
			}
		};
		txtFldNombreProducto.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Buscar");
		txtFldNombreProducto.getActionMap().put("Buscar", actionBuscar);
		btnBuscar.addActionListener(actionBuscar);
	}
}
