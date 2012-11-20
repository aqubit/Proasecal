package com.proasecal.plugin.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.plugin.exceptions.PluginException;

public class DlgError extends JDialog {
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("messages"); //$NON-NLS-1$

	/**
	 * 
	 */
	private static final long serialVersionUID = -3504674883403201963L;
	private JPanel contentPane;
	private JPanel pnlBotones;
	private JButton btnSalir;
	private JButton btnRegistrar;
	private JLabel lblLogo;
	private JTextArea txtAreaExtraInfo;

	/**
	 * Create the dialog.
	 */
	public DlgError(PluginException ex, final String strProducto) {
		setResizable(false);
		setModal(true);
		setTitle(BUNDLE.getString("app.title") + " " + strProducto); //$NON-NLS-1$
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 282);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		FormLayout fl_contentPane = new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("524px:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("60px"),
				FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("40px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(35dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("40px"), });
		contentPane.setLayout(fl_contentPane);

		lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
		lblLogo.setIcon(new ImageIcon(DlgError.class
				.getResource("/images/logo.png")));
		contentPane.add(lblLogo, "2, 2");
		String errorMessage = BUNDLE
				.getString("jfmError.lblInstrucciones.text");
		String strCode = BUNDLE.getString(ex.getCode());
		Object[] params = { strCode };
		String fullError = MessageFormat.format(errorMessage, params);
		JLabel lblInstrucciones = new JLabel(fullError); //$NON-NLS-1$ //$NON-NLS-1$
		lblInstrucciones.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPane.add(lblInstrucciones, "2, 4, fill, top");

		StringBuffer extraInfo = new StringBuffer();
		extraInfo.append("OS : ");
		extraInfo.append(System.getProperty("os.name"));
		extraInfo.append(System.getProperty("os.version"));
		extraInfo.append("\nJRE: ");
		extraInfo.append(System.getProperty("java.version"));
		extraInfo.append("\nJava home: ");
		extraInfo.append(System.getProperty("java.home"));
		txtAreaExtraInfo = new JTextArea();
		txtAreaExtraInfo.setEditable(false);
		txtAreaExtraInfo.setText(extraInfo.toString()); //$NON-NLS-1$
		contentPane.add(txtAreaExtraInfo, "2, 6, fill, fill");

		pnlBotones = new JPanel();
		pnlBotones.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(pnlBotones, "2, 8, default, top");
		pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnRegistrar = new JButton(BUNDLE.getString("btnRegistrar.text")); //$NON-NLS-1$
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DlgError.this.dispose();
				DlgRegistro frame = new DlgRegistro(strProducto);
				frame.setVisible(true);
			}
		});
		pnlBotones.add(btnRegistrar);

		btnSalir = new JButton(BUNDLE.getString("btnCancelar.text")); //$NON-NLS-1$
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pnlBotones.add(btnSalir);
		btnRegistrar.setEnabled(ex.permitirRegistro());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
					System.exit(0);
				}
			}
		);
	}

}
