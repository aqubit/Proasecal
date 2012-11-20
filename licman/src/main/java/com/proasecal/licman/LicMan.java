package com.proasecal.licman;

import java.awt.EventQueue;
import java.security.Security;

import com.proasecal.licman.gui.MainWindow;
import com.proasecal.licman.svcs.ConfigManager;
import com.proasecal.licman.svcs.DBManager;
import com.proasecal.licman.svcs.LoggerManager;

public class LicMan {

	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(LicMan.class);

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoggerManager.init();
					LOGGER.info("Iniciando Licman");
					DBManager.init();
					ConfigManager.init();
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					LOGGER.error("Error en la configuraci\u00F3n", e);
				}
			}
		});
	}

}
