package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.MatteBorder;

import org.openswing.swing.client.CheckBoxControl;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.client.LabelControl;
import org.openswing.swing.client.RadioButtonControl;
import org.openswing.swing.client.TextControl;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.util.java.Consts;

import com.proasecal.licman.controller.ClienteFormController;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.ClienteVO;

public class DlgCliente extends JDialog {
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$

	/**
	 * 
	 */
	private static final long serialVersionUID = -9178113987846876325L;
	/**
	 * 
	 */
	private GridControl _gridControl;
	private ClienteVO _cliente;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private LabelControl lblCodigoLab = new LabelControl();
	private TextControl txtFldCodLab = new TextControl();
	private Form mainPanel = new Form();
	private LabelControl lblNombreLab = new LabelControl();
	private TextControl txtNombreLab = new TextControl();
	private LabelControl lblTipoPersona = new LabelControl();
	private LabelControl lblNIT = new LabelControl();
	private final JPanel pnlButtons = new JPanel();
	private final JButton btnGuardar = new JButton(
			BUNDLE.getString("btnGuardar.text")); //$NON-NLS-1$
	private final JButton btnCancelar = new JButton(
			BUNDLE.getString("btnCancel.text")); //$NON-NLS-1$
	private final LabelControl lblPersonaContacto = new LabelControl();
	private final LabelControl lblTelContacto = new LabelControl();
	private final TextControl txtFldNombrePersona = new TextControl();
	private final JPanel pnlTipoPersona = new JPanel();
	private final RadioButtonControl rbtnNatural = new RadioButtonControl();
	private final RadioButtonControl rbtnJuridica = new RadioButtonControl();
	private final TextControl txtFldNIT = new TextControl();
	private final TextControl txtFldTelefonoContacto = new TextControl();
	private final LabelControl lblActivo = new LabelControl();
	private final CheckBoxControl chbxActivo = new CheckBoxControl();
	
	public DlgCliente(ClienteVO c, GridControl gridControl) {
		setModal(true);
		_gridControl = gridControl;
		_cliente = c;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(590, 324);
		setResizable(false);
		setTitle(BUNDLE.getString("DlgCliente.this.title")); //$NON-NLS-1$
		gridBagLayout1.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0,
				0.0 };
		gridBagLayout1.rowWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, 1.0 };
		gridBagLayout1.rowHeights = new int[] { 10, 0, 0, 36, 0, 6, 0, 0, 48 };
		mainPanel.setLayout(gridBagLayout1);
		mainPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0,
				0)));
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setVOClassName(BUNDLE.getString("DlgCliente.mainPanel.VOClassName")); //$NON-NLS-1$

		GridBagConstraints gbc_lblCodigoLab = new GridBagConstraints();
		gbc_lblCodigoLab.insets = new Insets(5, 5, 5, 5);
		gbc_lblCodigoLab.fill = GridBagConstraints.BOTH;
		gbc_lblCodigoLab.gridx = 0;
		gbc_lblCodigoLab.gridy = 1;
		lblCodigoLab.setText(BUNDLE.getString("DlgCliente.lblCodigoLab.text")); //$NON-NLS-1$	
		mainPanel.add(lblCodigoLab, gbc_lblCodigoLab);

		GridBagConstraints gbc_txtFldCodLab = new GridBagConstraints();
		gbc_txtFldCodLab.anchor = GridBagConstraints.WEST;
		gbc_txtFldCodLab.insets = new Insets(5, 5, 5, 5);
		gbc_txtFldCodLab.fill = GridBagConstraints.VERTICAL;
		gbc_txtFldCodLab.gridx = 1;
		gbc_txtFldCodLab.gridy = 1;
		txtFldCodLab.setTrimText(true);
		txtFldCodLab.setAttributeName("codigo_laboratorio");
		txtFldCodLab.setRequired(true);
		txtFldCodLab.setLinkLabel(lblCodigoLab);
		mainPanel.add(txtFldCodLab, gbc_txtFldCodLab);
		
		GridBagConstraints gbc_lblNombreLab = new GridBagConstraints();
		gbc_lblNombreLab.insets = new Insets(5, 5, 5, 5);
		gbc_lblNombreLab.fill = GridBagConstraints.BOTH;
		gbc_lblNombreLab.gridx = 0;
		gbc_lblNombreLab.gridy = 2;
		lblNombreLab.setText(BUNDLE.getString("DlgCliente.lblNombreLab.text")); //$NON-NLS-1$	
		mainPanel.add(lblNombreLab, gbc_lblNombreLab);
		
		GridBagConstraints gbc_txtNombreLab = new GridBagConstraints();
		gbc_txtNombreLab.insets = new Insets(5, 5, 5, 5);
		gbc_txtNombreLab.fill = GridBagConstraints.BOTH;
		gbc_txtNombreLab.gridx = 1;
		gbc_txtNombreLab.gridy = 2;
		txtNombreLab.setTrimText(true);
		txtNombreLab.setLinkLabel(lblNombreLab);
		txtNombreLab.setRequired(true);
		txtNombreLab.setAttributeName("nombre_laboratorio");
		mainPanel.add(txtNombreLab, gbc_txtNombreLab);
		
		GridBagConstraints gbc_lblTipoPersona = new GridBagConstraints();
		gbc_lblTipoPersona.insets = new Insets(5, 5, 5, 5);
		gbc_lblTipoPersona.fill = GridBagConstraints.BOTH;
		gbc_lblTipoPersona.gridx = 0;
		gbc_lblTipoPersona.gridy = 3;
		lblTipoPersona.setText(BUNDLE.getString("DlgCliente.lblTipoPersona.text")); //$NON-NLS-1$	
		mainPanel.add(lblTipoPersona, gbc_lblTipoPersona);
		
		GridBagConstraints gbc_pnlTipoPersona = new GridBagConstraints();
		gbc_pnlTipoPersona.insets = new Insets(5, 5, 5, 5);
		gbc_pnlTipoPersona.fill = GridBagConstraints.BOTH;
		gbc_pnlTipoPersona.gridx = 1;
		gbc_pnlTipoPersona.gridy = 3;
		FlowLayout flowLayout = (FlowLayout) pnlTipoPersona.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		mainPanel.add(pnlTipoPersona, gbc_pnlTipoPersona);
		ButtonGroup group = new ButtonGroup();
		rbtnNatural.setButtonGroup(group);
		rbtnJuridica.setButtonGroup(group);
		rbtnNatural.setAttributeName("tipo_persona");
		rbtnNatural.setSelectedValue(BUNDLE.getString("DlgCliente.rbtnNatural.text"));
		rbtnNatural.setText(BUNDLE.getString("DlgCliente.rbtnNatural.text")); //$NON-NLS-1$
		rbtnNatural.setLinkLabel(lblTipoPersona);
		pnlTipoPersona.add(rbtnNatural);
		rbtnJuridica.setSelected(true);
		rbtnJuridica.setAttributeName("tipo_persona");
		rbtnJuridica.setSelectedValue(BUNDLE.getString("DlgCliente.rbtnJuridica.text")); //$NON-NLS-1$
		rbtnJuridica.setText(BUNDLE.getString("DlgCliente.rbtnJuridica.text")); //$NON-NLS-1$
		rbtnJuridica.setLinkLabel(lblTipoPersona);
		pnlTipoPersona.add(rbtnJuridica);
		
		GridBagConstraints gbc_lblNIT = new GridBagConstraints();
		gbc_lblNIT.insets = new Insets(5, 5, 5, 5);
		gbc_lblNIT.fill = GridBagConstraints.BOTH;
		gbc_lblNIT.gridx = 0;
		gbc_lblNIT.gridy = 4;
		lblNIT.setText(BUNDLE.getString("DlgCliente.lblNIT.text")); //$NON-NLS-1$
		mainPanel.add(lblNIT, gbc_lblNIT);
		
		GridBagConstraints gbc_txtFldNIT = new GridBagConstraints();
		gbc_txtFldNIT.insets = new Insets(5, 5, 5, 5);
		gbc_txtFldNIT.fill = GridBagConstraints.VERTICAL;
		gbc_txtFldNIT.anchor = GridBagConstraints.WEST;
		gbc_txtFldNIT.gridx = 1;
		gbc_txtFldNIT.gridy = 4;
		txtFldNIT.setTrimText(true);
		txtFldNIT.setColumns(15);
		txtFldNIT.setRequired(true);
		txtFldNIT.setAttributeName("nit");
		txtFldNIT.setLinkLabel(lblNIT);
		mainPanel.add(txtFldNIT, gbc_txtFldNIT);
		
		GridBagConstraints gbc_lblPersonaContacto = new GridBagConstraints();
		gbc_lblPersonaContacto.fill = GridBagConstraints.VERTICAL;
		gbc_lblPersonaContacto.anchor = GridBagConstraints.WEST;
		gbc_lblPersonaContacto.insets = new Insets(5, 5, 5, 5);
		gbc_lblPersonaContacto.gridx = 0;
		gbc_lblPersonaContacto.gridy = 5;
		lblPersonaContacto.setText(BUNDLE.getString("DlgCliente.lblPersonaContacto.text")); //$NON-NLS-1$
		mainPanel.add(lblPersonaContacto, gbc_lblPersonaContacto);
		
		GridBagConstraints gbc_txtFldNombrePersona = new GridBagConstraints();
		gbc_txtFldNombrePersona.insets = new Insets(5, 5, 5, 5);
		gbc_txtFldNombrePersona.fill = GridBagConstraints.BOTH;
		gbc_txtFldNombrePersona.gridx = 1;
		gbc_txtFldNombrePersona.gridy = 5;
		
		GridBagLayout gbl_txtFldNombrePersona = (GridBagLayout) txtFldNombrePersona.getLayout();
		gbl_txtFldNombrePersona.rowWeights = new double[]{0.0};
		gbl_txtFldNombrePersona.rowHeights = new int[]{0};
		gbl_txtFldNombrePersona.columnWeights = new double[]{0.0};
		gbl_txtFldNombrePersona.columnWidths = new int[]{0};
		txtFldNombrePersona.setTrimText(true);
		txtFldNombrePersona.setAttributeName("nombre_contacto");
		txtFldNombrePersona.setLinkLabel(lblPersonaContacto);
		txtFldNombrePersona.setRequired(true);
		mainPanel.add(txtFldNombrePersona, gbc_txtFldNombrePersona);
		
		GridBagConstraints gbc_lblTelContacto = new GridBagConstraints();
		gbc_lblTelContacto.fill = GridBagConstraints.VERTICAL;
		gbc_lblTelContacto.anchor = GridBagConstraints.WEST;
		gbc_lblTelContacto.insets = new Insets(5, 5, 5, 5);
		gbc_lblTelContacto.gridx = 0;
		gbc_lblTelContacto.gridy = 6;
		lblTelContacto.setText(BUNDLE.getString("DlgCliente.lblTelContacto.text")); //$NON-NLS-1$
		mainPanel.add(lblTelContacto, gbc_lblTelContacto);
		
		GridBagConstraints gbc_txtFldTelefonoContacto = new GridBagConstraints();
		gbc_txtFldTelefonoContacto.insets = new Insets(5, 5, 5, 5);
		gbc_txtFldTelefonoContacto.fill = GridBagConstraints.VERTICAL;
		gbc_txtFldTelefonoContacto.anchor = GridBagConstraints.WEST;
		gbc_txtFldTelefonoContacto.gridx = 1;
		gbc_txtFldTelefonoContacto.gridy = 6;
		txtFldTelefonoContacto.setTrimText(true);
		txtFldTelefonoContacto.setColumns(15);
		txtFldTelefonoContacto.setAttributeName("telefono_contacto");
		txtFldTelefonoContacto.setLinkLabel(lblTelContacto);
		txtFldTelefonoContacto.setRequired(true);
		mainPanel.add(txtFldTelefonoContacto, gbc_txtFldTelefonoContacto);
		
		GridBagConstraints gbc_lblActivo = new GridBagConstraints();
		gbc_lblActivo.fill = GridBagConstraints.VERTICAL;
		gbc_lblActivo.anchor = GridBagConstraints.WEST;
		gbc_lblActivo.insets = new Insets(5, 5, 5, 5);
		gbc_lblActivo.gridx = 0;
		gbc_lblActivo.gridy = 7;
		lblActivo.setText(BUNDLE.getString("DlgCliente.lblActivo.text")); //$NON-NLS-1$
		mainPanel.add(lblActivo, gbc_lblActivo);
		
		GridBagConstraints gbc_chbxActivo = new GridBagConstraints();
		gbc_chbxActivo.fill = GridBagConstraints.VERTICAL;
		gbc_chbxActivo.anchor = GridBagConstraints.WEST;
		gbc_chbxActivo.insets = new Insets(0, 0, 5, 5);
		gbc_chbxActivo.gridx = 1;
		gbc_chbxActivo.gridy = 7;
		chbxActivo.setAttributeName("activo");
		chbxActivo.setText("");
		chbxActivo.setLinkLabel(lblActivo);
		mainPanel.add(chbxActivo, gbc_chbxActivo);
		
		GridBagConstraints gbc_pnlButtons = new GridBagConstraints();
		gbc_pnlButtons.gridwidth = 6;
		gbc_pnlButtons.fill = GridBagConstraints.BOTH;
		gbc_pnlButtons.gridx = 0;
		gbc_pnlButtons.gridy = 8;
		
		FlowLayout fl_pnlButtons = (FlowLayout) pnlButtons.getLayout();
		fl_pnlButtons.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(pnlButtons, gbc_pnlButtons);
		pnlButtons.add(btnGuardar);
		pnlButtons.add(btnCancelar);
		// OpenSwing
		mainPanel.setFormController(new ClienteFormController(_cliente));
		if (_cliente != null) {
			mainPanel.reload();
		}else{
			mainPanel.setMode(Consts.INSERT);
		}
		LicmanUtils.locateOnScreenCenter(this);

		// Actions
		// ****************************************************************

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				txtFldCodLab.requestFocus();
				//BUG. Checkbox null old value
				if (_cliente != null) {
					mainPanel.setMode(Consts.EDIT);
				}
			}
		});

		@SuppressWarnings("serial")
		AbstractAction closeAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		@SuppressWarnings("serial")
		AbstractAction guardarAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (mainPanel.isChanged()) {
					boolean resultOK = mainPanel.save();
					if (resultOK) {
						_gridControl.reloadCurrentBlockOfData();
						dispose();
						LicmanUtils.MsgBox("msg.operacion.exitosa");
					}
				} else {
					dispose();
				}
			}
		};

		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		getRootPane().registerKeyboardAction(closeAction, stroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btnGuardar.addActionListener(guardarAction);
		btnCancelar.addActionListener(closeAction);
	}
}
