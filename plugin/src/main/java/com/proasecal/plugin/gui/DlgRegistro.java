package com.proasecal.plugin.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.proasecal.plugin.exceptions.PluginException;
import com.proasecal.plugin.svcs.ConfigManager;
import com.proasecal.plugin.svcs.PluginSecurityManager;

public class DlgRegistro extends JDialog {
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("messages"); //$NON-NLS-1$

	/**
	 * 
	 */

	private static final long serialVersionUID = -8799775167098262288L;
	private PluginSecurityManager securityManager;
	
	private JPanel contentPane;
	private JLabel lblingreseLaLlave;
	private JPanel pnlClaveActivacion;
	private JPanel pnlBotones;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private JLabel lblLogo;
	private List arrClaveAct = new ArrayList();

	/**
	 * Create the frame.
	 */
	public DlgRegistro(String strProducto) {
		setResizable(false);
		setModal(true);
		setTitle(BUNDLE.getString("app.title") + " " + strProducto); //$NON-NLS-1$
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		FormLayout fl_contentPane = new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("524px:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(28dlu;min)"),
				FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("50px"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("60px"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("60px"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("40px"), });
		contentPane.setLayout(fl_contentPane);

		lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
		lblLogo.setIcon(new ImageIcon(DlgRegistro.class
				.getResource("/images/logo.png")));
		contentPane.add(lblLogo, "2, 2");

		JLabel lblInstrucciones = new JLabel(
				BUNDLE.getString("jfmNoRegistrado.lblInstrucciones.text")); //$NON-NLS-1$
		lblInstrucciones.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPane.add(lblInstrucciones, "2, 4, fill, top");

		JPanel pnlNumeroLicencia = new JPanel();
		pnlNumeroLicencia
				.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(pnlNumeroLicencia, "2, 6");
		// Generar la licencia
		String[] licencia = null;
		try {
			securityManager = new PluginSecurityManager();
			licencia = securityManager.generarNumeroLicencia();
		} catch (PluginException e1) {
			DlgError winError = new DlgError(e1,strProducto);
			winError.setVisible(true);
		}
		// Crear controles para mostrar el nro de licencia
		int iColumns = licencia[0].length();
		for (int i = 0; i < licencia.length; i++) {
			JTextField txtFldNroLicencia = new JTextField();
			txtFldNroLicencia.setHorizontalAlignment(SwingConstants.CENTER);
			txtFldNroLicencia.setEditable(false);
			txtFldNroLicencia.setText(licencia[i]);
			txtFldNroLicencia.setColumns(iColumns);
			txtFldNroLicencia.setFont(ConfigManager.PLUGIN_FONT);
			txtFldNroLicencia.setForeground(ConfigManager.PLUGIN_TEXT_COLOR);
			pnlNumeroLicencia.add(txtFldNroLicencia);
		}
		FlowLayout fl_pnlNumeroLicencia = new FlowLayout(FlowLayout.CENTER, 10,
				5);
		pnlNumeroLicencia.setLayout(fl_pnlNumeroLicencia);

		lblingreseLaLlave = new JLabel(
				BUNDLE.getString("jfmNoRegistrado.lblingreseLaLlave.text")); //$NON-NLS-1$
		lblingreseLaLlave.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPane.add(lblingreseLaLlave, "2, 8, left, default");

		pnlClaveActivacion = new JPanel();
		pnlClaveActivacion.setBackground(UIManager
				.getColor("Button.background"));
		contentPane.add(pnlClaveActivacion, "2, 10");
		pnlClaveActivacion.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		// Action listeners para validar la longitud de la licencia
		PluginMouseAdapter ml = new PluginMouseAdapter();
		PluginKeyAdapter ka = new PluginKeyAdapter();
		// Crear controles para ingresar la clave de activación
		for (int i = 0; i < licencia.length; i++) {
			JFormattedTextField txtFldClave = new JFormattedTextField();
			txtFldClave = new JFormattedTextField(createFormatter(ConfigManager.FIELD_FORMAT));
			txtFldClave.setHorizontalAlignment(SwingConstants.CENTER);
			txtFldClave.setColumns(iColumns);
			txtFldClave.addKeyListener(ka);
			txtFldClave.addMouseListener(ml);
			txtFldClave.setFont(ConfigManager.PLUGIN_FONT);
			txtFldClave.setForeground(ConfigManager.PLUGIN_TEXT_COLOR);
			txtFldClave.setFocusLostBehavior(JFormattedTextField.COMMIT);
			arrClaveAct.add(txtFldClave);
			pnlClaveActivacion.add(txtFldClave);
		}
		// Crear botones
		pnlBotones = new JPanel();
		pnlBotones.setBackground(UIManager.getColor("Button.background"));
		contentPane.add(pnlBotones, "2, 12, default, top");
		pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnAceptar = new JButton(BUNDLE.getString("btnRegistrar.text")); //$NON-NLS-1$
		btnAceptar.setEnabled(false);
		pnlBotones.add(btnAceptar);
		btnAceptar.addActionListener(new AceptarAction());
		btnCancelar = new JButton(BUNDLE.getString("btnCancelar.text")); //$NON-NLS-1$
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pnlBotones.add(btnCancelar);
		// Focus default component
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				JComponent comp = (JComponent) arrClaveAct.get(0);
				comp.requestFocusInWindow();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
					System.exit(0);
				}
			}
		);		
	}

	// Actions

	MouseListener ml = new MouseAdapter() {
	};

	private final class PluginMouseAdapter extends MouseAdapter{
		public void mousePressed(final MouseEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JTextField tf = (JTextField) e.getSource();
					int offset = tf.getText().trim().length();
					tf.setCaretPosition(offset);
				}
			});
		}
	}
	
	private final class PluginKeyAdapter extends KeyAdapter{
		public void keyTyped(KeyEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					validarLongitudLicencia();
				}
			});
		}
	}
	
	private final class AceptarAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private AceptarAction() {
			super();
		}

		public void actionPerformed(ActionEvent e) {
			StringBuffer nrolicencia = new StringBuffer();
			for (int i = 0; i < arrClaveAct.size(); i++) {
				JFormattedTextField f = (JFormattedTextField) arrClaveAct
						.get(i);
				nrolicencia.append(f.getText());
			}
			try {
				PluginSecurityManager plugin = new PluginSecurityManager();
				plugin.validarClaveActivacion(nrolicencia.toString());
				dispose();
			} catch (PluginException ex) {
				String errorCode = BUNDLE.getString(ex.getCode());
				if (ex.permitirRegistro()) {
					String errorMsg = BUNDLE
							.getString("error.clave.incorrecta");
					String msg = MessageFormat.format(errorMsg, new Object[] {
							errorCode, new Integer(ex.getNumReintentos()) });
					JOptionPane.showMessageDialog((Component) null, msg,
							"Proasecal", JOptionPane.ERROR_MESSAGE);
				} else {
					String errorMsg = BUNDLE
							.getString("error.reintentos.alcanzados");
					errorCode = BUNDLE.getString("error.code.diez");
					String msg = MessageFormat.format(errorMsg,
							new Object[] { errorCode });
					JOptionPane.showMessageDialog((Component) null, msg,
							"Proasecal", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}

			}
		}
	}

	// Helpers ****************************************************************

	private MaskFormatter createFormatter(String s) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(s);
		} catch (java.text.ParseException exc) {
			System.err.println("formatter is bad: " + exc.getMessage());
		}
		return formatter;

	}

	private void validarLongitudLicencia() {
		boolean enableOK = true;
		int fieldLength = ConfigManager.FIELD_FORMAT.length();
		for (int i = 0; i < arrClaveAct.size(); i++) {
			JFormattedTextField f = (JFormattedTextField) arrClaveAct.get(i);
			if (f.getText().trim().length() != fieldLength) {
				enableOK = false;
			}
		}
		btnAceptar.setEnabled(enableOK);
	}

}
