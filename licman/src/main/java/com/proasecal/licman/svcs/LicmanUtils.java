package com.proasecal.licman.svcs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

public class LicmanUtils {
	
	public static void locateOnScreenCenter(Component component) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = component.getSize().width;
		int h = component.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		component.setLocation(x, y);
	}

	public static void MsgBox(String key) {
		String msg = ConfigManager.getString(key);
		String title = ConfigManager.getAppName();
		JOptionPane.showMessageDialog((Component) null, msg, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static int ConfirmDialogMsg(String msg) {
		String title = ConfigManager.getAppName();
		int result = JOptionPane.showConfirmDialog(null, msg, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return result;
	}

	public static int ConfirmDialog(String key) {
		String msg = ConfigManager.getString(key);
		String title = ConfigManager.getAppName();
		int result = JOptionPane.showConfirmDialog(null, msg, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return result;
	}

	public static MaskFormatter createFormatter(String s) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(s);
		} catch (java.text.ParseException exc) {
			System.err.println("formatter is bad: " + exc.getMessage());
		}
		return formatter;
	}

	public static Date removeTime(Date date) {
		Calendar calServer = Calendar.getInstance(ConfigManager.getLocale());
		calServer.setTime(date);
		calServer.set(Calendar.HOUR_OF_DAY, 0);
		calServer.set(Calendar.MINUTE, 0);
		calServer.set(Calendar.SECOND, 0);
		calServer.set(Calendar.MILLISECOND, 0);
		return calServer.getTime();
	}

	public static Date endOfDay(Date date) {
		Calendar calServer = Calendar.getInstance(ConfigManager.getLocale());
		calServer.setTime(date);
		calServer.set(Calendar.HOUR_OF_DAY, 0);
		calServer.set(Calendar.MINUTE, 0);
		calServer.set(Calendar.SECOND, 0);
		calServer.set(Calendar.MILLISECOND, 0);
		calServer.add(Calendar.DATE, 1);
		return calServer.getTime();
	}

	public static Date fechaHoy() {
		Calendar calServer = Calendar.getInstance(ConfigManager.getLocale());
		calServer.set(Calendar.HOUR_OF_DAY, 0);
		calServer.set(Calendar.MINUTE, 0);
		calServer.set(Calendar.SECOND, 0);
		calServer.set(Calendar.MILLISECOND, 0);
		return calServer.getTime();
	}

	public static Date maxFecha() {
		Calendar calServer = Calendar.getInstance(ConfigManager.getLocale());
		calServer.add(Calendar.YEAR, 1000);
		calServer.set(Calendar.HOUR_OF_DAY, 0);
		calServer.set(Calendar.MINUTE, 0);
		calServer.set(Calendar.SECOND, 0);
		calServer.set(Calendar.MILLISECOND, 0);
		return calServer.getTime();
	}

	public static Date diffHoy(int dias) {
		Calendar calServer = Calendar.getInstance(ConfigManager.getLocale());
		calServer.set(Calendar.HOUR_OF_DAY, 0);
		calServer.set(Calendar.MINUTE, 0);
		calServer.set(Calendar.SECOND, 0);
		calServer.set(Calendar.MILLISECOND, 0);
		calServer.add(Calendar.DATE, dias);
		return calServer.getTime();
	}

}
