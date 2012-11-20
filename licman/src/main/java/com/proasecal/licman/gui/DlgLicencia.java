package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.openswing.swing.client.CodLookupControl;
import org.openswing.swing.client.DateControl;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.client.LabelControl;
import org.openswing.swing.client.TextControl;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.util.java.Consts;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.licman.controller.ClienteLookupController;
import com.proasecal.licman.controller.LicenciaFormController;
import com.proasecal.licman.controller.PatrocinadorLookupController;
import com.proasecal.licman.controller.ProductoLookupController;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.LicenciaVO;

public class DlgLicencia extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6250231007405918804L;

	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$

	private final Form mainPanel = new Form();
	private JButton btnGenerar;
	private JPanel pnlBuscarProducto;
	private JPanel pnlBuscarCliente;
	private CodLookupControl lkpProducto;
	private CodLookupControl lkpCliente;
	private TextControl txtFldProductoNombreVersion;
	private TextControl txtFldCliente;
	private LabelControl lblInstrucciones;
	private LabelControl lblProducto;
	private LabelControl lblCliente;
	private LabelControl lblNumeroLicencia;
	private LabelControl lblFechaDeVencimiento;
	private JButton btnCancel;
	private JPanel pnlNumeroLicencia;
	private ArrayList<JFormattedTextField> arrFldLicencia = new ArrayList<JFormattedTextField>();

	private GridControl _gridControl;
	private LicenciaVO _licencia;
	private DateControl datctrlFechaActivacion;
	private LabelControl lblDeptoActivacion;
	private TextControl txtFldDeptoActivacion;
	private LabelControl lblPesonaActiva;
	private LabelControl lblTelPersonaActiva;
	private TextControl txtFldPesonaActiva;
	private TextControl txtFldTelPersonaActiva;
	private LabelControl lblPatrocinador;
	private JPanel pnlBuscarPatrocinador;
	private TextControl txtFldPatrocinador;
	private CodLookupControl lkpPatrocinador;
	private LabelControl lblClaveActivacion;
	private LabelControl lblNumeroOrden;
	private TextControl txtFldNumeroOrden;
	private JPanel pnlClaveActivacion;

	/**
	 * Create the dialog.
	 */
	public DlgLicencia(LicenciaVO licencia, GridControl gridControl) {
		_licencia = licencia;
		_gridControl = gridControl;
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle(BUNDLE.getString("DlgLicencia.this.title")); //$NON-NLS-1$
		setBounds(100, 100, 666, 524);
		Font font = ConfigManager.getFont();
		getContentPane().setLayout(new BorderLayout());
		mainPanel.setVOClassName(BUNDLE
				.getString("DlgLicencia.mainPanel.VOClassName")); //$NON-NLS-1$
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:default:grow"),
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(30dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("30dlu"), }));
		{
			lblInstrucciones = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblInstrucciones.text")); //$NON-NLS-1$
			mainPanel.add(lblInstrucciones, "2, 2, 2, 1");
		}
		{
			lblProducto = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblProducto.text")); //$NON-NLS-1$
			mainPanel.add(lblProducto, "2, 4, left, default");
		}
		{
			pnlBuscarProducto = new JPanel();
			FlowLayout fl_pnlBuscarProducto = new FlowLayout(FlowLayout.LEFT);
			fl_pnlBuscarProducto.setHgap(0);
			pnlBuscarProducto.setLayout(fl_pnlBuscarProducto);
			lkpProducto = new CodLookupControl();
			lkpProducto.setMaxCharacters(10);
			lkpProducto.setColumns(10);
			pnlBuscarProducto.add(lkpProducto);
			{
				txtFldProductoNombreVersion = new TextControl();
				txtFldProductoNombreVersion.setEnabledOnInsert(false);
				txtFldProductoNombreVersion.setEnabledOnEdit(false);
				txtFldProductoNombreVersion.setCanCopy(false);
				txtFldProductoNombreVersion.getBindingComponent()
						.setBackground(mainPanel.getBackground());
				txtFldProductoNombreVersion.setColumns(15);
				txtFldProductoNombreVersion.getBindingComponent().setEnabled(
						false);
				txtFldProductoNombreVersion.setRequired(true);
				txtFldProductoNombreVersion
						.setAttributeName("productoVO.nombreVersion");
				txtFldProductoNombreVersion.setLinkLabel(lblProducto);
				pnlBuscarProducto.add(txtFldProductoNombreVersion);
			}
			LookupController lookupController = new ProductoLookupController();
			lkpProducto.setLookupController(lookupController);
			lkpProducto.setAutoCompletitionWaitTime(1000);
			mainPanel.add(pnlBuscarProducto, "3, 4, left, default");
		}
		{
			lblCliente = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblCliente.text")); //$NON-NLS-1$
			mainPanel.add(lblCliente, "2, 6, left, default");
		}
		{
			pnlBuscarCliente = new JPanel();
			FlowLayout fl_pnlBuscarCliente = new FlowLayout(FlowLayout.LEFT);
			fl_pnlBuscarCliente.setHgap(0);
			pnlBuscarCliente.setLayout(fl_pnlBuscarCliente);
			lkpCliente = new CodLookupControl();
			lkpCliente.setMaxCharacters(10);
			lkpCliente.setColumns(10);
			pnlBuscarCliente.add(lkpCliente);
			{
				txtFldCliente = new TextControl();
				txtFldCliente.setEnabledOnInsert(false);
				txtFldCliente.setEnabledOnEdit(false);
				txtFldCliente.setCanCopy(false);
				txtFldCliente.getBindingComponent().setBackground(
						mainPanel.getBackground());
				txtFldCliente.setColumns(15);
				txtFldCliente.getBindingComponent().setEnabled(false);
				txtFldCliente.setRequired(true);
				txtFldCliente.setAttributeName("clienteVO.nombre_laboratorio");
				txtFldCliente.setLinkLabel(lblCliente);
				pnlBuscarCliente.add(txtFldCliente);
			}
			LookupController lookupController = new ClienteLookupController();
			lkpCliente.setLookupController(lookupController);
			lkpCliente.setAutoCompletitionWaitTime(1000);
			mainPanel.add(pnlBuscarCliente, "3, 6, left, default");
		}
		{
			lblPatrocinador = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblPatrocinador.text")); //$NON-NLS-1$
			mainPanel.add(lblPatrocinador, "2, 8, left, default");
		}
		{
			pnlBuscarPatrocinador = new JPanel();
			FlowLayout fl_pnlBuscarPatrocinador = new FlowLayout(
					FlowLayout.LEFT);
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
				txtFldPatrocinador.getBindingComponent().setBackground(
						mainPanel.getBackground());
				txtFldPatrocinador.setColumns(15);
				txtFldPatrocinador.getBindingComponent().setEnabled(false);
				txtFldPatrocinador.setRequired(false);
				txtFldPatrocinador.setAttributeName("patrocinadorVO.nombre");
				txtFldPatrocinador.setLinkLabel(lblPatrocinador);
				pnlBuscarPatrocinador.add(txtFldPatrocinador);

			}
			LookupController lookupController = new PatrocinadorLookupController();
			lkpPatrocinador.setLookupController(lookupController);
			lkpPatrocinador.setAutoCompletitionWaitTime(1000);
			mainPanel.add(pnlBuscarPatrocinador, "3, 8, left, default");
		}
		{
			lblFechaDeVencimiento = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblFechaDeVencimiento.text")); //$NON-NLS-1$
			mainPanel.add(lblFechaDeVencimiento, "2, 10, left, default");
		}
		{
			datctrlFechaActivacion = new DateControl();
			datctrlFechaActivacion
					.setTimeFormat(BUNDLE
							.getString("DlgLicencia.datctrlFechaActivacion.timeFormat")); //$NON-NLS-1$
			datctrlFechaActivacion.setRequired(true);
			datctrlFechaActivacion.setAttributeName("fecha_vencimiento");
			datctrlFechaActivacion.setDateType(Consts.TYPE_DATE);
			datctrlFechaActivacion.setLowerLimit(new Date());
			datctrlFechaActivacion.setLinkLabel(lblFechaDeVencimiento);
			mainPanel.add(datctrlFechaActivacion, "3, 10, left, fill");
		}
		{
			lblNumeroOrden = new LabelControl();
			lblNumeroOrden.setText(BUNDLE
					.getString("DlgLicencia.lblNumeroOrden.text")); //$NON-NLS-1$
			mainPanel.add(lblNumeroOrden, "2, 12, left, default");
		}
		{
			txtFldNumeroOrden = new TextControl();
			txtFldNumeroOrden.setLinkLabel(lblNumeroOrden);
			GridBagLayout gbl_txtFldNumeroOrden = (GridBagLayout) txtFldNumeroOrden
					.getLayout();
			gbl_txtFldNumeroOrden.rowWeights = new double[] { 0.0 };
			gbl_txtFldNumeroOrden.rowHeights = new int[] { 0 };
			gbl_txtFldNumeroOrden.columnWeights = new double[] { 0.0 };
			gbl_txtFldNumeroOrden.columnWidths = new int[] { 0 };
			txtFldNumeroOrden.setTrimText(true);
			txtFldNumeroOrden.setRequired(true);
			txtFldNumeroOrden.setColumns(20);
			txtFldNumeroOrden.setAttributeName("numero_orden");
			mainPanel.add(txtFldNumeroOrden, "3, 12, left, fill");
		}
		{
			lblDeptoActivacion = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblDeptoActivacion.text")); //$NON-NLS-1$
			mainPanel.add(lblDeptoActivacion, "2, 14, left, default");
		}
		{
			txtFldDeptoActivacion = new TextControl();
			txtFldDeptoActivacion.setTrimText(true);
			txtFldDeptoActivacion.setColumns(20);
			txtFldDeptoActivacion.setAttributeName("depto_activacion");
			txtFldDeptoActivacion.setRequired(true);
			txtFldDeptoActivacion.setLinkLabel(lblDeptoActivacion);
			mainPanel.add(txtFldDeptoActivacion, "3, 14, left, fill");
		}
		{
			lblPesonaActiva = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblPesonaActiva.text")); //$NON-NLS-1$
			mainPanel.add(lblPesonaActiva, "2, 16, left, default");
		}
		{
			txtFldPesonaActiva = new TextControl();
			txtFldPesonaActiva.setTrimText(true);
			txtFldPesonaActiva.setRequired(true);
			txtFldPesonaActiva.setAttributeName("nombre_persona");
			txtFldPesonaActiva.setColumns(20);
			txtFldPesonaActiva.setLinkLabel(lblPesonaActiva);
			mainPanel.add(txtFldPesonaActiva, "3, 16, left, fill");
		}
		{
			lblTelPersonaActiva = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblTelPersonaActiva.text")); //$NON-NLS-1$
			mainPanel.add(lblTelPersonaActiva, "2, 18, left, default");
		}
		{
			txtFldTelPersonaActiva = new TextControl();
			txtFldTelPersonaActiva.setTrimText(true);
			txtFldTelPersonaActiva.setRequired(true);
			txtFldTelPersonaActiva.setAttributeName("telefono_persona");
			txtFldTelPersonaActiva.setColumns(20);
			txtFldTelPersonaActiva.setLinkLabel(lblTelPersonaActiva);
			mainPanel.add(txtFldTelPersonaActiva, "3, 18, left, fill");
		}
		{
			lblNumeroLicencia = new LabelControl(
					BUNDLE.getString("DlgLicencia.lblNumeroLicencia.text")); //$NON-NLS-1$
			mainPanel.add(lblNumeroLicencia, "2, 20, left, default");
		}
		{
			pnlNumeroLicencia = new JPanel();
			pnlNumeroLicencia.setBackground(UIManager
					.getColor("Button.background"));
			mainPanel.add(pnlNumeroLicencia, "2, 22, 2, 1, center, center");
			FlowLayout fl_pnlNumeroLicencia = new FlowLayout(FlowLayout.CENTER,
					10, 5);
			fl_pnlNumeroLicencia.setAlignOnBaseline(true);
			// Action listeners para validar la longitud de la licencia a medida
			// que se digita
			MouseListener ml = new MouseAdapter() {
				public void mousePressed(final MouseEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JTextField tf = (JTextField) e.getSource();
							int offset = tf.getText().trim().length();
							tf.setCaretPosition(offset);
						}
					});
				}
			};

			KeyListener ka = new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							validarLongitudLicencia();
						}
					});

				}
			};
			pnlNumeroLicencia.setLayout(fl_pnlNumeroLicencia);
			{
				int nroCampos = ConfigManager.getNroCamposLicencia();
				int longitudCampo = ConfigManager
						.getLongitudxCampoNroLicencia();
				String strFormat = ConfigManager.getNroLicenciaFormat();
				for (int i = 0; i < nroCampos; i++) {
					JFormattedTextField txtFldNroLicencia = new JFormattedTextField(
							LicmanUtils.createFormatter(strFormat));
					txtFldNroLicencia
							.setFocusLostBehavior(JFormattedTextField.COMMIT);
					txtFldNroLicencia
							.setHorizontalAlignment(SwingConstants.CENTER);
					txtFldNroLicencia.setColumns(longitudCampo);
					txtFldNroLicencia.addKeyListener(ka);
					txtFldNroLicencia.addMouseListener(ml);
					txtFldNroLicencia.setFont(font);
					txtFldNroLicencia.setForeground(ConfigManager
							.getFontColor());
					pnlNumeroLicencia.add(txtFldNroLicencia);
					arrFldLicencia.add(txtFldNroLicencia);
				}
				// Cargar licencia si se está en modo de edición
				if (_licencia != null) {
					int i = 0;
					String[] arrNroLicencia = _licencia.getNumero_licencia()
							.split("(?<=\\G.{" + longitudCampo + "})");
					for (JFormattedTextField fld : arrFldLicencia) {
						fld.setText(arrNroLicencia[i++]);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnGenerar = new JButton(
						BUNDLE.getString("DlgLicencia.btnOK.text")); //$NON-NLS-1$

				btnGenerar.setEnabled(false);
				btnGenerar.addActionListener(new GenerarAction());
				btnGenerar.setActionCommand("OK");
				buttonPane.add(btnGenerar);
				getRootPane().setDefaultButton(btnGenerar);
			}
			{
				btnCancel = new JButton(BUNDLE.getString("btnCancel.text")); //$NON-NLS-1$
				buttonPane.add(btnCancel);
			}
		}
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		getRootPane().registerKeyboardAction(new CerrarAction(), stroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btnCancel.addActionListener(new CerrarAction());
		mainPanel.setFormController(new LicenciaFormController(_licencia));
		{
			lblClaveActivacion = new LabelControl("N\u00FAmero de licencia");
			lblClaveActivacion.setLabel("");
			lblClaveActivacion.setText(BUNDLE
					.getString("DlgLicencia.lblClaveActivacion.text")); //$NON-NLS-1$
			mainPanel.add(lblClaveActivacion, "2, 24, left, default");
		}
		{
			pnlClaveActivacion = new JPanel();
			pnlClaveActivacion.setBackground(UIManager
					.getColor("Button.background"));
			FlowLayout fl_pnlClaveActivacion = new FlowLayout(
					FlowLayout.CENTER, 10, 5);
			fl_pnlClaveActivacion.setAlignOnBaseline(true);
			pnlClaveActivacion.setLayout(fl_pnlClaveActivacion);
			mainPanel.add(pnlClaveActivacion, "2, 26, 2, 1, center, center");
			// Cargar clave de activación
			if (_licencia != null) {
				int longitudCampo = ConfigManager
						.getLongitudxCampoNroLicencia();
				String[] arrClaveActivacion = _licencia.getClave_activacion()
						.split("(?<=\\G.{" + longitudCampo + "})");
				for (int i = 0; i < arrClaveActivacion.length; i++) {
					JTextField txtClaveActivacion = new JTextField();
					txtClaveActivacion
							.setHorizontalAlignment(SwingConstants.CENTER);
					txtClaveActivacion.setColumns(longitudCampo);
					txtClaveActivacion.setFont(font);
					txtClaveActivacion.setForeground(ConfigManager
							.getFontColor());
					txtClaveActivacion.setEditable(false);
					txtClaveActivacion.setText(arrClaveActivacion[i]);
					pnlClaveActivacion.add(txtClaveActivacion);
				}
			}
		}
		if (_licencia != null && _licencia.getHijoVO() != null) {
			mainPanel.reload();
			mainPanel.setMode(Consts.READONLY);
			btnGenerar.setEnabled(false);
		} else if (_licencia != null) {
			mainPanel.reload();
			mainPanel.setMode(Consts.EDIT);
			btnGenerar.setEnabled(true);
		} else {
			mainPanel.setMode(Consts.INSERT);
		}
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				lkpProducto.requestFocus();
			}
		});
		LicmanUtils.locateOnScreenCenter(this);
	}

	// Actions ****************************************************************

	@SuppressWarnings("serial")
	private final class CerrarAction extends AbstractAction {

		private CerrarAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	@SuppressWarnings("serial")
	private final class GenerarAction extends AbstractAction {

		private GenerarAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			StringBuffer nrolicencia = new StringBuffer();
			for (JFormattedTextField f : arrFldLicencia) {
				nrolicencia.append(f.getText());
			}
			LicenciaVO vo = (LicenciaVO) mainPanel.getVOModel()
					.getValueObject();
			vo.setNumero_licencia(nrolicencia.toString());
			boolean resultOK = mainPanel.save();
			if (resultOK) {
				_gridControl.reloadCurrentBlockOfData();
				// Mostrar la clave de activación
				DlgClaveActivacion dlg = new DlgClaveActivacion(
						vo.getClave_activacion());
				dlg.setVisible(true);
				dispose();
			}
		}
	}

	// Helpers ****************************************************************

	private void validarLongitudLicencia() {
		boolean enableOK = true;
		int fieldLength = ConfigManager.getNroLicenciaFormat().length();
		for (JFormattedTextField f : arrFldLicencia) {
			if (f.getText().trim().length() != fieldLength) {
				enableOK = false;
			}
		}
		btnGenerar.setEnabled(enableOK);
	}
}