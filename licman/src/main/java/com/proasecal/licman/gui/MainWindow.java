package com.proasecal.licman.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.DBManager;
import com.proasecal.licman.svcs.LicmanUtils;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7007605391749991398L;

	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages"); //$NON-NLS-1$
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(MainWindow.class);

	private JPanel contentPane;
	PanelProducto pnlProducto;
	PanelLicencia pnlLicencia;
	PanelPatrocinador pnlPatrocinador;
	PanelCliente pnlCliente;
	
	
	JTextArea txtAreaLlavePlugin = null;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setTitle(BUNDLE.getString("app.title")); //$NON-NLS-1$
		setIconImage(ConfigManager.get_icon().getImage());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);

		JPanel pnlGeneral = new PanelGeneral();
		tabbedPane
				.addTab(BUNDLE.getString("jfmMainWindow.tabgeneral.title"), null, pnlGeneral, null); //$NON-NLS-1$

		JPanel pnlLicencias = new PanelLicencia();
		tabbedPane.addTab(BUNDLE.getString("jfmMainWindow.tablicencias.title"),
				null, pnlLicencias, null);

		JPanel pnlProductos = new PanelProducto();
		tabbedPane
				.addTab(BUNDLE.getString("jfmMainWindow.tabproductos.title"), null, pnlProductos, null); //$NON-NLS-1$*/
		JPanel pnlClientes = new PanelCliente();
		tabbedPane
				.addTab(BUNDLE.getString("jfmMainWindow.tabclientes.title"), null, pnlClientes, null); //$NON-NLS-1$

		JPanel pnlPatrocinador = new PanelPatrocinador();
		tabbedPane
				.addTab(BUNDLE.getString("jfmMainWindow.tabpatrocinador.title"), null, pnlPatrocinador, null); //$NON-NLS-1$

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				int result = LicmanUtils.ConfirmDialog("app.exit.msg");
				if (result == JOptionPane.YES_OPTION) {
					LOGGER.info("Cerrando Licman");
					DBManager.close();
					System.exit(0);
				}
			}

			public void windowOpened(WindowEvent e) {
				 setExtendedState(MAXIMIZED_BOTH); // Load the main window in a maximize state
			}
		});
		tabbedPane.setSelectedIndex(0);
	}
}
