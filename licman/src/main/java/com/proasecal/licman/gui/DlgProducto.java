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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.MatteBorder;

import org.openswing.swing.client.CheckBoxControl;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.client.LabelControl;
import org.openswing.swing.client.TextAreaControl;
import org.openswing.swing.client.TextControl;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.util.java.Consts;

import com.proasecal.licman.controller.ProductoFormController;
import com.proasecal.licman.svcs.LicmanUtils;
import com.proasecal.licman.vo.ProductoVO;

public class DlgProducto extends JDialog {
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$

	/**
	 * 
	 */
	private static final long serialVersionUID = -9178113987846876325L;
	private GridControl _gridControl;
	private ProductoVO _producto;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private LabelControl lblNombre = new LabelControl();
	private TextControl txtFldNombre = new TextControl();
	private Form mainPanel = new Form();
	private TextAreaControl txtDescripcion = new TextAreaControl();
	private LabelControl lblVersion = new LabelControl();
	private TextControl txtFldVersion = new TextControl();
	private LabelControl lblActivo = new LabelControl();
	private LabelControl lblDescripcion = new LabelControl();
	private final CheckBoxControl chkbxActivo = new CheckBoxControl();
	private final JPanel panel = new JPanel();
	private final JButton btnGuardar = new JButton(
			BUNDLE.getString("btnGuardar.text")); //$NON-NLS-1$
	private final JButton btnCancelar = new JButton(
			BUNDLE.getString("btnCancel.text")); //$NON-NLS-1$

	public DlgProducto(ProductoVO p, GridControl gridControl) {
		setModal(true);
		_gridControl = gridControl;
		_producto = p;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		mainPanel.setVOClassName(BUNDLE
				.getString("DlgProducto.mainPanel.VOClassName")); //$NON-NLS-1$
		setSize(590, 274);
		setTitle(BUNDLE.getString("DlgProducto.this.title")); //$NON-NLS-1$
		
		gridBagLayout1.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0,
				0.0 };
		gridBagLayout1.rowWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0 };
		gridBagLayout1.rowHeights = new int[] { 2, 0, 0, 0, 0, 56, 48 };
		mainPanel.setLayout(gridBagLayout1);
		mainPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0,
				0)));
		
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.insets = new Insets(5, 5, 5, 5);
		gbc_lblNombre.fill = GridBagConstraints.BOTH;
		gbc_lblNombre.gridx = 0;
		gbc_lblNombre.gridy = 1;
		lblNombre.setText(BUNDLE.getString("DlgProducto.lblNombre.text"));
		mainPanel.add(lblNombre, gbc_lblNombre);


		GridBagConstraints gbc_txtFldNombre = new GridBagConstraints();
		gbc_txtFldNombre.insets = new Insets(5, 5, 5, 5);
		gbc_txtFldNombre.fill = GridBagConstraints.BOTH;
		gbc_txtFldNombre.gridx = 1;
		gbc_txtFldNombre.gridy = 1;
		txtFldNombre.setTrimText(true);
		txtFldNombre.setAttributeName("nombre");
		txtFldNombre.setRequired(true);
		txtFldNombre.setLinkLabel(lblNombre);
		mainPanel.add(txtFldNombre, gbc_txtFldNombre);

		GridBagConstraints gbc_lblVersion= new GridBagConstraints();
		gbc_lblVersion.insets = new Insets(5, 5, 5, 5);
		gbc_lblVersion.fill = GridBagConstraints.BOTH;
		gbc_lblVersion.gridx = 0;
		gbc_lblVersion.gridy = 2;
		lblVersion.setText(BUNDLE.getString("DlgProducto.lblVersion.text"));
		mainPanel.add(lblVersion, gbc_lblVersion);
		
		GridBagConstraints gbc_txtFldVersion = new GridBagConstraints();
		gbc_txtFldVersion.insets = new Insets(5, 5, 5, 5);
		gbc_txtFldVersion.fill = GridBagConstraints.VERTICAL;
		gbc_txtFldVersion.anchor = GridBagConstraints.WEST;
		gbc_txtFldVersion.gridx = 1;
		gbc_txtFldVersion.gridy = 2;
		txtFldVersion.setTrimText(true);
		txtFldVersion.setLinkLabel(lblVersion);
		txtFldVersion.setRequired(true);
		txtFldVersion.setAttributeName("version");
		mainPanel.add(txtFldVersion, gbc_txtFldVersion);

		GridBagConstraints gbc_lblActivo= new GridBagConstraints();
		gbc_lblActivo.insets = new Insets(5, 5, 5, 5);
		gbc_lblActivo.fill = GridBagConstraints.BOTH;
		gbc_lblActivo.gridx = 0;
		gbc_lblActivo.gridy = 3;
		lblActivo.setText(BUNDLE.getString("DlgProducto.lblActivo.text"));
		mainPanel.add(lblActivo, gbc_lblActivo);
		
		GridBagConstraints gbc_chkbxActivo = new GridBagConstraints();
		gbc_chkbxActivo.fill = GridBagConstraints.VERTICAL;
		gbc_chkbxActivo.anchor = GridBagConstraints.WEST;
		gbc_chkbxActivo.insets = new Insets(0, 0, 5, 5);
		gbc_chkbxActivo.gridx = 1;
		gbc_chkbxActivo.gridy = 3;
		chkbxActivo.setAttributeName("activo");
		chkbxActivo.setLinkLabel(lblActivo);
		mainPanel.add(chkbxActivo, gbc_chkbxActivo);

		GridBagConstraints gbc_lblDescripcion= new GridBagConstraints();
		gbc_lblDescripcion.insets = new Insets(5, 5, 5, 5);
		gbc_lblDescripcion.fill = GridBagConstraints.BOTH;
		gbc_lblDescripcion.gridx = 0;
		gbc_lblDescripcion.gridy = 4;
		lblDescripcion.setText(BUNDLE
				.getString("DlgProducto.lblDescripcion.text")); //$NON-NLS-1$
		mainPanel.add(lblDescripcion, gbc_lblDescripcion);

		GridBagConstraints gbc_txtDescripcion= new GridBagConstraints();
		gbc_txtDescripcion.gridwidth = 6;
		gbc_txtDescripcion.insets = new Insets(5, 5, 5, 0);
		gbc_txtDescripcion.fill = GridBagConstraints.BOTH;
		gbc_txtDescripcion.gridx = 0;
		gbc_txtDescripcion.gridy = 5;
		txtDescripcion.setRequired(true);
		txtDescripcion.setAttributeName("descripcion");
		txtDescripcion.setLinkLabel(lblDescripcion);
		mainPanel.add(txtDescripcion, gbc_txtDescripcion);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 6;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 6;
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(panel, gbc_panel);

		panel.add(btnGuardar);
		panel.add(btnCancelar);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setFormController(new ProductoFormController(_producto));
		if (_producto != null) {
			mainPanel.reload();
		}else{
			mainPanel.setMode(Consts.INSERT);
		}
		LicmanUtils.locateOnScreenCenter(this);

		// Actions
		// ****************************************************************

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				txtFldNombre.requestFocus();
				//BUG. Checkbox null old value
				if (_producto != null) {
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
