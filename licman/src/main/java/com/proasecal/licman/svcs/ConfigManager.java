package com.proasecal.licman.svcs;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.openswing.swing.export.java.ExportOptions;
import org.openswing.swing.internationalization.java.ResourcesFactory;
import org.openswing.swing.util.client.ClientSettings;

import com.proasecal.licman.exceptions.PluginKeyGenerationException;
import com.proasecal.licman.gui.SpanishOnlyResourceFactory;

public class ConfigManager {

	private static final Locale _localeColombia;

	static {
		_localeColombia = new Locale("es_CO");
		Locale.setDefault(_localeColombia);
		JOptionPane.setDefaultLocale(new Locale("es", "ES"));
	}

	private static final int LONGITUD_NRO_LICENCIA = 25;
	private static final int LONGITUD_CAMPO_NRO_LICENCIA = 5;
	private static final int LONGITUD_CAMPO_CLAVE_ACTIVACION = 5;
	private static final String NRO_LICENCIA_FIELD_FORMAT = "HHHHH";
	private static final Font _font = new Font("Arial", Font.ROMAN_BASELINE, 16);
	public static final int DATABASE_BLOCK = 50;

	public static String[] EXPORTING_FORMATS = new String[] {
		ExportOptions.XLS_FORMAT, ExportOptions.CSV_FORMAT1,
		ExportOptions.XML_FORMAT_FAT, ExportOptions.HTML_FORMAT
		};
	
	private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(ConfigManager.class);
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.messages");
	private static final ResourceBundle OPSWG_BUNDLE = ResourceBundle
			.getBundle("com.proasecal.licman.openswing");

	private static final String STORE_PASSWORD = BUNDLE
			.getString("app.STORE_PASSWORD");
	private static final String KEY_PASSWORD = BUNDLE
			.getString("app.KEY_PASSWORD");
	private static final String DESED_KEY_NAME = BUNDLE
			.getString("app.DESED_KEY_NAME");
	private static final String RSA_KEY_NAME = BUNDLE
			.getString("app.RSA_KEY_NAME");

	private static Key desedeKey;
	private static PublicKey publicKey;
	private static PrivateKey privateKey;

	private static ImageIcon _icon;
	
	public static void init() {
		try {
			LOGGER.debug("Agregando BC");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			LOGGER.debug("Iniciando carga de llaves");
			// Key configuration
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			InputStream keyStream = ConfigManager.class
					.getResourceAsStream("/com/proasecal/licman/licman.data");
			LOGGER.debug("Abriendo KeyStore");
			keyStore.load(keyStream, STORE_PASSWORD.toCharArray());
			LOGGER.debug("Cargando llave privada");
			privateKey = (PrivateKey) keyStore.getKey(RSA_KEY_NAME,
					KEY_PASSWORD.toCharArray());
			LOGGER.debug(privateKey);
			LOGGER.debug("Cargando llave 3DES ");
			desedeKey = keyStore.getKey(DESED_KEY_NAME,
					KEY_PASSWORD.toCharArray());
			LOGGER.debug(desedeKey);
			LOGGER.debug("Cargando certificado");
			Certificate cert = keyStore.getCertificate(RSA_KEY_NAME);
			publicKey = cert.getPublicKey();
			LOGGER.debug(publicKey);
			LOGGER.debug("Cargando icono");
			URL imgURL = ConfigManager.class.getResource("/com/proasecal/licman/images/icon.png");
			_icon = new ImageIcon(imgURL);
			// OpenSwing configuration
			Properties props = convertResourceBundleToProperties(OPSWG_BUNDLE);
			@SuppressWarnings({ "rawtypes", "unused" })
			LicManOpenSwingSettings clientSettings = new LicManOpenSwingSettings(
					new SpanishOnlyResourceFactory("$", props, false),
					new Hashtable());
			LOGGER.debug("OpenSwing configurado con \u00E9xito");
			if (System.getProperty("pluginkey") != null) {
				LOGGER.debug("Generando llave para el plugin");
				generarLlavePlugin();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(ex);
			System.exit(0);
		}
	}

	public static PrivateKey getPrivateKey() {
		return privateKey;
	}

	public static Key getPublicKey() {
		return publicKey;
	}

	public static Key getDESedeKey() {
		return desedeKey;

	}

	public static String getString(String key) {
		return BUNDLE.getString(key);
	}

	public static String getAppName() {
		return BUNDLE.getString("app.name");
	}

	public static String getNroLicenciaFormat() {
		return NRO_LICENCIA_FIELD_FORMAT;
	}

	public static int getLongitudNroLicencia() {
		return LONGITUD_NRO_LICENCIA;
	}

	public static int getLongitudxCampoNroLicencia() {
		return LONGITUD_CAMPO_NRO_LICENCIA;
	}

	public static int getLongitudxCampoClaveActivacion() {
		return LONGITUD_CAMPO_CLAVE_ACTIVACION;
	}

	public static Font getFont() {
		return _font;
	}

	public static Color getFontColor() {
		return Color.BLUE;
	}

	public static Locale getLocale() {
		return _localeColombia;
	}

	public static int getNroCamposLicencia() {
		Double nroCampos = new Double(LONGITUD_NRO_LICENCIA
				/ LONGITUD_CAMPO_NRO_LICENCIA);
		nroCampos = Math.ceil(nroCampos);
		return nroCampos.intValue();
	}

	public static ImageIcon get_icon() {
		return _icon;
	}

	private static void generarLlavePlugin() {
		LicmanSecurityManager secManager = new LicmanSecurityManager();
		try {
			String key = secManager.generatePluginKey();
			LOGGER.info(key);
		} catch (PluginKeyGenerationException ex) {
			LOGGER.error(ex);
		}
	}

	private static Properties convertResourceBundleToProperties(
			ResourceBundle resource) {
		Properties properties = new Properties();
		Enumeration<String> keys = resource.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String origKey = key.replace('.', ' ');
			origKey = origKey.replace('%', '.');
			origKey = origKey.replace('$', ':');
			String data = resource.getString(key);
			data = data.replace('.', ' ');
			data = data.replace('%', '.');
			data = data.replace('$', ':');
			properties.put(origKey, data);
		}
		return properties;
	}

}

class LicManOpenSwingSettings extends ClientSettings {

	public LicManOpenSwingSettings(ResourcesFactory resourceFactory,
			@SuppressWarnings("rawtypes") Hashtable domains) {
		super(resourceFactory, domains);
		MAX_NR_OF_LOOPS_IN_ANALYZE_VO = 1;
	}
}
