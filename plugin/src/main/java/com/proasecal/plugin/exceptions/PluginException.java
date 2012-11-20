package com.proasecal.plugin.exceptions;

public class PluginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4027237422298808L;
	private boolean _bContinuar = true;
	private String _code = null;
	private int _iNumReintentos = 0;
	
	public PluginException(String code, boolean bContinuar) {
		super(code);
		_code = code;
		_bContinuar = bContinuar;
	}

	public PluginException(String code, boolean bContinuar, int numReintentos) {
		super(code);
		_code = code;
		_bContinuar = bContinuar;
		_iNumReintentos = numReintentos;
	}
	
	public PluginException(String code, int numReintentos) {
		super(code);
		_code = code;
		_bContinuar = (numReintentos > 0 ? true : false);
		_iNumReintentos = numReintentos;
	}

	public PluginException(String code) {
		super(code);
		_code = code;
	}
	
	public boolean permitirRegistro() {
		return _bContinuar;
	}

	public int getNumReintentos() {
		return _iNumReintentos;
	}

	
	public String getCode() {
		return _code;
	}
}
