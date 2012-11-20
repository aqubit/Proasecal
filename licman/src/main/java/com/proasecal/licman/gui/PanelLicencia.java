package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.openswing.swing.client.CodLookupControl;
import org.openswing.swing.client.DateControl;
import org.openswing.swing.client.DeleteButton;
import org.openswing.swing.client.EditButton;
import org.openswing.swing.client.ExportButton;
import org.openswing.swing.client.GenericButton;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.client.InsertButton;
import org.openswing.swing.client.NavigatorBar;
import org.openswing.swing.client.TextControl;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.table.columns.client.DateTimeColumn;
import org.openswing.swing.table.columns.client.IntegerColumn;
import org.openswing.swing.table.columns.client.TextColumn;
import org.openswing.swing.util.java.Consts;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.licman.controller.LicenciaGridController;
import com.proasecal.licman.controller.PatrocinadorLookupController;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanUtils;

public class PanelLicencia extends JPanel {
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$
	/**
	 * 
	 */
	private static final long serialVersionUID = 5900350163776851973L;
	private TextControl txtFldNombreProducto;
	private TextControl txtFldCodigoLab;
	private LicenciaGridController controller;
	private DateControl datectrlFechaCreacion1;
	private DateControl datectrlFechaCreacion2;
	private DateControl datectrlFechaVencimiento1;
	private DateControl datectrlFechaVencimiento2;
	private CodLookupControl lkpPatrocinador;
	private TextControl txtFldPatrocinador;
	private TextControl txtFldPatrocinadorId;
	private JButton btnBuscar;
	private JRadioButton jrbTodas;
	private JRadioButton jrbActivas;
	private JRadioButton jrbRenovadas;
	private JRadioButton jrbVencidas;

	
	@SuppressWarnings("serial")
	public PanelLicencia() {
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
		pnlGrupo1
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						ColumnSpec.decode("left:max(111dlu;default):grow"),
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("top:default"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblNombreProducto = new JLabel(
				BUNDLE.getString("PanelLicencia.lblNombreProducto.text")); //$NON-NLS-1$
		pnlGrupo1.add(lblNombreProducto, "2, 2, left, top");

		txtFldNombreProducto = new TextControl();
		txtFldNombreProducto.setColumns(20);
		txtFldNombreProducto.setAttributeName("productoVO.nombre");
		txtFldNombreProducto.getBindingComponent().getInputMap()
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Buscar");
		txtFldNombreProducto.getBindingComponent().getActionMap()
				.put("Buscar", new BuscarAction());

		pnlGrupo1.add(txtFldNombreProducto, "3, 2, left, default");

		JLabel lblCodLab = new JLabel(
				BUNDLE.getString("PanelLicencia.lblCodigoLab.text")); //$NON-NLS-1$
		pnlGrupo1.add(lblCodLab, "2, 4, left, default");

		txtFldCodigoLab = new TextControl();
		txtFldCodigoLab.setColumns(20);
		txtFldCodigoLab.setAttributeName("clienteVO.codigo_laboratorio");
		txtFldCodigoLab.getBindingComponent().getInputMap()
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Buscar");
		txtFldCodigoLab.getBindingComponent().getActionMap()
				.put("Buscar", new BuscarAction());
		pnlGrupo1.add(txtFldCodigoLab, "3, 4, left, default");

		JLabel lblFechaCreacion = new JLabel(
				BUNDLE.getString("PanelProducto.dateColumn.headerColumnName")); //$NON-NLS-1$
		pnlGrupo1.add(lblFechaCreacion, "2, 6, left, default");

		JPanel pnlFechaCreacion = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) pnlFechaCreacion.getLayout();
		flowLayout_1.setHgap(0);
		flowLayout_1.setVgap(0);
		pnlGrupo1.add(pnlFechaCreacion, "3, 6, left, fill");

		datectrlFechaCreacion1 = new DateControl();
		datectrlFechaCreacion1.setAttributeName("fecha_creacion");
		pnlFechaCreacion.add(datectrlFechaCreacion1);
		datectrlFechaCreacion1.setDateType(Consts.TYPE_DATE);

		JLabel lblFechaHasta = new JLabel(
				BUNDLE.getString("PanelLicencia.lblFechaHasta.text")); //$NON-NLS-1$
		pnlFechaCreacion.add(lblFechaHasta);

		datectrlFechaCreacion2 = new DateControl();
		datectrlFechaCreacion2.setAttributeName("fecha_creacion");
		pnlFechaCreacion.add(datectrlFechaCreacion2);
		datectrlFechaCreacion2.setDateType(Consts.TYPE_DATE);

		JLabel lblFechaVencimiento = new JLabel(
				BUNDLE.getString("PanelLicencia.columnFechaVencimiento.headerColumnName")); //$NON-NLS-1$
		pnlGrupo1.add(lblFechaVencimiento, "2, 8");

		JPanel pnlFechaVencimiento = new JPanel();
		FlowLayout fl_pnlFechaVencimiento = (FlowLayout) pnlFechaVencimiento
				.getLayout();
		fl_pnlFechaVencimiento.setVgap(0);
		fl_pnlFechaVencimiento.setHgap(0);
		pnlGrupo1.add(pnlFechaVencimiento, "3, 8, left, fill");

		datectrlFechaVencimiento1 = new DateControl();
		datectrlFechaVencimiento1.setAttributeName("fecha_vencimiento");
		GridBagLayout gbl_datectrlFechaVencimiento1 = (GridBagLayout) datectrlFechaVencimiento1
				.getLayout();
		gbl_datectrlFechaVencimiento1.rowWeights = new double[] { 0.0 };
		gbl_datectrlFechaVencimiento1.rowHeights = new int[] { 0 };
		gbl_datectrlFechaVencimiento1.columnWeights = new double[] { 0.0 };
		gbl_datectrlFechaVencimiento1.columnWidths = new int[] { 0 };
		datectrlFechaVencimiento1.setDateType(1);
		pnlFechaVencimiento.add(datectrlFechaVencimiento1);

		JLabel lblFechaHasta2 = new JLabel(
				BUNDLE.getString("PanelLicencia.lblFechaHasta.text")); //$NON-NLS-1$
		pnlFechaVencimiento.add(lblFechaHasta2);

		datectrlFechaVencimiento2 = new DateControl();
		datectrlFechaVencimiento2.setAttributeName("fecha_vencimiento");
		GridBagLayout gbl_datectrlFechaVencimiento2 = (GridBagLayout) datectrlFechaVencimiento2
				.getLayout();
		gbl_datectrlFechaVencimiento2.rowWeights = new double[] { 0.0 };
		gbl_datectrlFechaVencimiento2.rowHeights = new int[] { 0 };
		gbl_datectrlFechaVencimiento2.columnWeights = new double[] { 0.0 };
		gbl_datectrlFechaVencimiento2.columnWidths = new int[] { 0 };
		datectrlFechaVencimiento2.setDateType(1);
		pnlFechaVencimiento.add(datectrlFechaVencimiento2);

		JLabel lblPatrocinador = new JLabel(
				BUNDLE.getString("DlgLicencia.lblPatrocinador.text")); //$NON-NLS-1$
		pnlGrupo1.add(lblPatrocinador, "2, 10, left, fill");

		Form pnlBuscarPatrocinador = new Form();
		pnlBuscarPatrocinador.setVOClassName(BUNDLE
				.getString("DlgLicencia.mainPanel.VOClassName")); //$NON-NLS-1$
		FlowLayout fl_pnlBuscarPatrocinador = new FlowLayout(FlowLayout.LEFT);
		fl_pnlBuscarPatrocinador.setHgap(0);
		pnlBuscarPatrocinador.setLayout(fl_pnlBuscarPatrocinador);
		lkpPatrocinador = new CodLookupControl();
		lkpPatrocinador.setMaxCharacters(10);
		lkpPatrocinador.setColumns(10);
		pnlBuscarPatrocinador.add(lkpPatrocinador);
		{
			txtFldPatrocinador = new TextControl();
			txtFldPatrocinador.setEnabledOnInsert(false);
			txtFldPatrocinador.setEnabledOnEdit(false);
			txtFldPatrocinador.setCanCopy(false);
			txtFldPatrocinador.setColumns(15);
			txtFldPatrocinador.getBindingComponent().setEnabled(false);
			txtFldPatrocinador.setRequired(false);
			txtFldPatrocinador.setAttributeName("patrocinadorVO.nombre");
			JTextField txtFldBindingPatrocinador = (JTextField) txtFldPatrocinador
					.getBindingComponent();
			txtFldBindingPatrocinador.getDocument().addDocumentListener(
					new LookUpAction());
			pnlBuscarPatrocinador.add(txtFldPatrocinador);
			txtFldPatrocinadorId = new TextControl();
			txtFldPatrocinadorId.setVisible(false);
			txtFldPatrocinadorId.setAttributeName("patrocinadorVO.id");
			pnlBuscarPatrocinador.add(txtFldPatrocinadorId);
		}
		LookupController lookupController = new PatrocinadorLookupController();
		lkpPatrocinador.setLookupController(lookupController);
		lkpPatrocinador.setAutoCompletitionWaitTime(1000);
		pnlGrupo1.add(pnlBuscarPatrocinador, "3, 10, left, fill");

		JLabel lblEstado = new JLabel(
				BUNDLE.getString("PanelLicencia.estado.text")); //$NON-NLS-1$
		pnlGrupo1.add(lblEstado, "2, 12, left, fill");
		
		jrbTodas = new JRadioButton (
				BUNDLE.getString("PanelLicencia.checkBoxTodas.text"));
		jrbActivas = new JRadioButton(
				BUNDLE.getString("PanelLicencia.checkBoxActivas.text"));
		jrbRenovadas = new JRadioButton(
				BUNDLE.getString("PanelLicencia.checkBoxRenovadas.text"));
		jrbVencidas = new JRadioButton(
				BUNDLE.getString("PanelLicencia.checkBoxVencidas.text")); 
		JPanel jplRadio = new JPanel();
		jplRadio.setLayout(new GridLayout(1, 4));
		jrbTodas.setSelected(true);
		ButtonGroup jrbGroup = new ButtonGroup();
		jrbGroup.add(jrbTodas);
		jrbGroup.add(jrbActivas);
		jrbGroup.add(jrbVencidas);
		jrbGroup.add(jrbRenovadas);		
		jplRadio.add(jrbTodas);
		jplRadio.add(jrbActivas);
		jplRadio.add(jrbRenovadas);
		jplRadio.add(jrbVencidas);
		pnlGrupo1.add(jplRadio, "3, 12, left, default");

		btnBuscar = new JButton(
				BUNDLE.getString("PanelLicencia.btnBuscar.text")); //$NON-NLS-1$
		pnlGrupo1.add(btnBuscar, "2, 14, left, default");
		btnBuscar.setIcon(new ImageIcon(PanelLicencia.class
				.getResource("/com/proasecal/licman/images/search.png")));

		JLabel lblResultados = new JLabel(
				BUNDLE.getString("PanelLicencia.lblResultados.text")); //$NON-NLS-1$
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
		URL iconRenovarURL = ConfigManager.class
				.getResource("/com/proasecal/licman/images/renovar.png");
		ImageIcon iconRenovar = new ImageIcon(iconRenovarURL);
		GenericButton btnRenovar = new GenericButton(iconRenovar);
		btnRenovar.setToolTipText(ConfigManager
				.getString("DlgLicencia.renovarButton.text"));
		panel.add(btnRenovar);
		NavigatorBar navigatorBar = new NavigatorBar();
		panel.add(navigatorBar);
		final GridControl gridControl = new GridControl();
		gridControl.setMaxSortedColumns(2);
		controller = new LicenciaGridController(gridControl);
		gridControl
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
		gridControl
				.setValueObjectClassName("com.proasecal.licman.vo.LicenciaVO");

		IntegerColumn columnId = new IntegerColumn();
		columnId.setColumnSortable(true);
		columnId.setAutoFitColumn(true);
		columnId.setColumnName("id");
		gridControl.getColumnContainer().add(columnId);
		TextColumn columnProducto = new TextColumn();
		columnProducto.setColumnSortable(true);
		GridBagLayout gridBagLayout = (GridBagLayout) columnProducto
				.getLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		columnProducto.setHeaderColumnName(BUNDLE
				.getString("PanelLicencia.columnProducto.headerColumnName")); //$NON-NLS-1$
		columnProducto.setAutoFitColumn(true);
		columnProducto.setColumnName("productoVO.nombre");
		gridControl.getColumnContainer().add(columnProducto);

		TextColumn columnVersion = new TextColumn();
		columnVersion.setColumnSortable(true);
		columnVersion.setAutoFitColumn(true);
		columnVersion.setHeaderColumnName(BUNDLE
				.getString("DlgProducto.lblVersion.text")); //$NON-NLS-1$
		columnVersion.setColumnName("productoVO.version");
		gridControl.getColumnContainer().add(columnVersion);

		TextColumn columnCliente = new TextColumn();
		columnCliente.setAutoFitColumn(true);
		columnCliente.setColumnSortable(true);
		columnCliente.setColumnName("clienteVO.nombre_laboratorio");
		columnCliente.setHeaderColumnName(BUNDLE
				.getString("PanelLicencia.columnCliente.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnCliente);

		TextColumn columnDepartamento = new TextColumn();
		columnDepartamento.setColumnSortable(true);
		columnDepartamento.setColumnName("depto_activacion");
		columnDepartamento
				.setHeaderColumnName(BUNDLE
						.getString("PanelLicencia.columnDepartamento.headerColumnName")); //$NON-NLS-1$
		columnDepartamento.setAutoFitColumn(true);
		gridControl.getColumnContainer().add(columnDepartamento);

		TextColumn columnNumOrden = new TextColumn();
		columnNumOrden.setColumnSortable(true);
		columnNumOrden.setHeaderColumnName(BUNDLE
				.getString("DlgLicencia.lblNumeroOrden.text")); //$NON-NLS-1$
		columnNumOrden.setColumnName("numero_orden");
		columnNumOrden.setAutoFitColumn(true);
		gridControl.getColumnContainer().add(columnNumOrden);

		TextColumn columnPatrocinador = new TextColumn();
		columnPatrocinador.setColumnSortable(true);
		columnPatrocinador.setHeaderColumnName(BUNDLE
				.getString("DlgLicencia.lblPatrocinador.text")); //$NON-NLS-1$
		columnPatrocinador.setColumnName("patrocinadorVO.nombre");
		columnPatrocinador.setAutoFitColumn(true);
		gridControl.getColumnContainer().add(columnPatrocinador);

		TextColumn columnPersonaActiva = new TextColumn();
		columnPersonaActiva.setColumnSortable(true);
		columnPersonaActiva.setAutoFitColumn(true);
		columnPersonaActiva.setColumnName("nombre_persona");
		columnPersonaActiva.setHeaderColumnName(BUNDLE
				.getString("PanelLicencia.textColumn.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnPersonaActiva);

		TextColumn columnTelPersonaActiva = new TextColumn();
		columnTelPersonaActiva.setColumnSortable(true);
		columnTelPersonaActiva.setAutoFitColumn(true);
		columnTelPersonaActiva
				.setHeaderColumnName(BUNDLE
						.getString("PanelLicencia.columnTelPersonaActiva.headerColumnName")); //$NON-NLS-1$
		columnTelPersonaActiva.setColumnName("telefono_persona");
		gridControl.getColumnContainer().add(columnTelPersonaActiva);

		DateTimeColumn columnFechaVencimiento = new DateTimeColumn();
		columnFechaVencimiento.setColumnSortable(true);
		columnFechaVencimiento.setAutoFitColumn(true);
		columnFechaVencimiento.setColumnName("fecha_vencimiento");
		columnFechaVencimiento.setTimeFormat("");
		columnFechaVencimiento
				.setHeaderColumnName(BUNDLE
						.getString("PanelLicencia.columnFechaVencimiento.headerColumnName")); //$NON-NLS-1$
		gridControl.getColumnContainer().add(columnFechaVencimiento);

		DateTimeColumn columnFechaCreacion = new DateTimeColumn();
		columnFechaCreacion.setColumnSortable(true);
		GridBagLayout gbl_columnFechaCreacion = (GridBagLayout) columnFechaCreacion
				.getLayout();
		gbl_columnFechaCreacion.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_columnFechaCreacion.rowHeights = new int[] { 0, 0, 0 };
		gbl_columnFechaCreacion.columnWeights = new double[] { 0.0 };
		gbl_columnFechaCreacion.columnWidths = new int[] { 0 };
		columnFechaCreacion.setHeaderColumnName(BUNDLE
				.getString("PanelLicencia.dateTimeColumn.headerColumnName")); //$NON-NLS-1$
		columnFechaCreacion.setColumnName("fecha_creacion");
		columnFechaCreacion.setAutoFitColumn(true);
		gridControl.getColumnContainer().add(columnFechaCreacion);

		TextColumn columnClaveActivacion = new TextColumn();
		columnClaveActivacion.setHeaderColumnName(BUNDLE
				.getString("DlgLicencia.lblClaveDeActivacion.text")); //$NON-NLS-1$
		columnClaveActivacion.setColumnName("clave_activacion");
		columnClaveActivacion.setColumnSortable(true);
		columnFechaCreacion.setAutoFitColumn(true);
		gridControl.getColumnContainer().add(columnClaveActivacion);

		// Actions
		// ****************************************************************

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				txtFldNombreProducto.requestFocusInWindow();
				btnInsert.setEnabled(true);
			}
		});

		AbstractAction actionRenovar = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				controller.renovar();
			}
		};

		btnRenovar.addActionListener(actionRenovar);
		btnBuscar.addActionListener(new BuscarAction());
	}

	// Actions ****************************************************************

	@SuppressWarnings("serial")
	private final class BuscarAction extends AbstractAction {

		private BuscarAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			LinkedHashMap<String, Object> filtro = new LinkedHashMap<String, Object>();
			filtro.put(txtFldNombreProducto.getAttributeName(),
					txtFldNombreProducto.getText());
			filtro.put(txtFldCodigoLab.getAttributeName(),
					txtFldCodigoLab.getText());
			Date today = LicmanUtils.fechaHoy();
			Date fechaCreacion1 = datectrlFechaCreacion1.getDate();
			Date fechaCreacion2 = datectrlFechaCreacion2.getDate();
			if (fechaCreacion1 != null && fechaCreacion2 != null) {
				fechaCreacion1 = LicmanUtils.removeTime(fechaCreacion1);
				fechaCreacion2 = LicmanUtils.endOfDay(fechaCreacion2);
				filtro.put(datectrlFechaCreacion1.getAttributeName(),
						fechaCreacion1);
				filtro.put(datectrlFechaCreacion2.getAttributeName() + "2",
						fechaCreacion2);
			}
			Date fechaVencimiento1 = datectrlFechaVencimiento1.getDate();
			Date fechaVencimiento2 = datectrlFechaVencimiento2.getDate();
			if (fechaVencimiento1 != null && fechaVencimiento2 != null) {
				fechaVencimiento1 = LicmanUtils.removeTime(fechaVencimiento1);
				fechaVencimiento2 = LicmanUtils.removeTime(fechaVencimiento2);
				filtro.put(datectrlFechaVencimiento1.getAttributeName(),
						fechaVencimiento1);
				filtro.put(datectrlFechaVencimiento2.getAttributeName() + "2",
						fechaVencimiento2);
			}
			if (!txtFldPatrocinador.getText().isEmpty()) {
				String strId = txtFldPatrocinadorId.getText();
				filtro.put(txtFldPatrocinadorId.getAttributeName(),
						Integer.valueOf(strId));
			}
			if(!jrbTodas.isSelected()){
				boolean bRenovadas = false;
				
				if( jrbActivas.isSelected() ){
					fechaVencimiento1 = LicmanUtils.removeTime(today);
					fechaVencimiento2 = LicmanUtils.maxFecha();
					filtro.put(datectrlFechaVencimiento1.getAttributeName(),
							fechaVencimiento1);
					filtro.put(datectrlFechaVencimiento2.getAttributeName() + "2",
							fechaVencimiento2);
				}
				else if( jrbRenovadas.isSelected() ){
					bRenovadas = true;
				}else if( jrbVencidas.isSelected() ){
					fechaVencimiento1 = new Date(0L);
					fechaVencimiento2 = LicmanUtils.removeTime(LicmanUtils.diffHoy(-1));
					filtro.put(datectrlFechaVencimiento1.getAttributeName(),
							fechaVencimiento1);
					filtro.put(datectrlFechaVencimiento2.getAttributeName() + "2",
							fechaVencimiento2);
				}			
				filtro.put("fue_renovada",
						bRenovadas);
			}
			PanelLicencia.this.controller.buscar(filtro);
		}
	}

	private final class LookUpAction implements DocumentListener {

		private LookUpAction() {
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					btnBuscar.doClick();
				}
			});
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}

	}
}
