package com.proasecal.licman.svcs;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.xml.DOMConfigurator;

public class LoggerManager {

	public static void init() throws IOException {
		URL log4Jresource = LoggerManager.class
				.getResource("/com/proasecal/licman/log4j.xml");
		DOMConfigurator.configure(log4Jresource);
	}
}