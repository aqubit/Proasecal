package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.LicmanUtils;

public class DlgClaveActivacion extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6250231007405918804L;

	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$

	private final JPanel contentPanel = new JPanel();
	private JLabel lblInstrucciones;
	private JPanel pnlClaveActivacion;

	/**
	 * Create the dialog.
	 */
	public DlgClaveActivacion(String claveActivacion) {
		int longitudCampo = ConfigManager.getLongitudxCampoClaveActivacion();
		String[] arrClaveActivacion = claveActivacion.split("(?<=\\G.{"
				+ longitudCampo + "})");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle(BUNDLE.getString("DlgClaveActivacion.this.title")); //$NON-NLS-1$
		setBounds(100, 100, 666, 235);
		Font font = ConfigManager.getFont();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		// Put dummy data in design mode
		contentPanel
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(30dlu;default)"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("60px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));
		{
			lblInstrucciones = new JLabel(
					BUNDLE.getString("DlgClaveActivacion.lblInstrucciones.text")); //$NON-NLS-1$
			contentPanel.add(lblInstrucciones, "2, 2");
		}
		{
			pnlClaveActivacion = new JPanel();
			pnlClaveActivacion.setBackground(UIManager
					.getColor("Button.background"));
			contentPanel.add(pnlClaveActivacion, "2, 4, center, center");
			FlowLayout fl_pnlClaveActivacion = new FlowLayout(
					FlowLayout.CENTER, 10, 5);
			fl_pnlClaveActivacion.setAlignOnBaseline(true);
			pnlClaveActivacion.setLayout(fl_pnlClaveActivacion);
			// Agregar campos de texto
			for (String s : arrClaveActivacion) {
				JTextField fld = new JTextField();
				fld.setText(s);
				fld.setEditable(false);
				fld.setHorizontalAlignment(SwingConstants.CENTER);
				fld.setColumns(longitudCampo);
				fld.setFont(font);
				fld.setForeground(ConfigManager.getFontColor());
				pnlClaveActivacion.add(fld);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnFinalizar = new JButton(
						BUNDLE.getString("DlgClaveActivacion.btnFinalizar.text")); //$NON-NLS-1$
				btnFinalizar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						DlgClaveActivacion.this.dispose();
					}
				});
				btnFinalizar.setActionCommand("Cancel");
				buttonPane.add(btnFinalizar);
			}
		}
		LicmanUtils.locateOnScreenCenter(this);
	}
}
