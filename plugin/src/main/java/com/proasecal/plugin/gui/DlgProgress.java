package com.proasecal.plugin.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class DlgProgress extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1997335629434629563L;

	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("messages"); //$NON-NLS-1$

	private final JPanel contentPanel = new JPanel();
	private final JProgressBar pgbAvance;

	/**
	 * Create the dialog.
	 */
	public DlgProgress(String strProducto) {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle(BUNDLE.getString("app.title") + " " + strProducto); //$NON-NLS-1$
		setBounds(100, 100, 450, 89);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		JLabel lblInstrucciones = new JLabel(
				BUNDLE.getString("DlgProgress.lblInstrucciones.text")); //$NON-NLS-1$
		contentPanel.add(lblInstrucciones);
		pgbAvance = new JProgressBar();
		pgbAvance.setIndeterminate(true);
		contentPanel.add(pgbAvance);
		locateOnScreenCenter(this);
	}

	public void dispose() {
		// Bug VM 1.4. Sino el Event dispatching thread no cierra
		pgbAvance.setIndeterminate(false);
		super.dispose();
	}

	public void locateOnScreenCenter(Component component) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = component.getSize().width;
		int h = component.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		component.setLocation(x, y);
	}
}
