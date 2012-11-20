package com.proasecal.plugin.svcs;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

public class ConfigManager {
	public static final String FIELD_FORMAT = "HHHHH";
	public static final Font PLUGIN_FONT = new Font("Arial", Font.ROMAN_BASELINE, 16);
	public static final Color PLUGIN_TEXT_COLOR = Color.BLUE;
	public static final int WIN_KEY_SIZE = 23;
	public static final int RANDOM_LENGTH = 5;
	public static final int RANDOM_ENCODED_LENGTH = 10;
	public static final int FIELD_LENGTH = 5;
	public static final int NUMERO_REINTENTOS = 5;
	public static final byte[] REINTENTOS_KEY = "3FA40E8A984D43156A271787AB8883F9893D51EC4B563B53".getBytes();
	public static final Locale PLUGIN_LOCALE = new Locale("es_CO");
}
