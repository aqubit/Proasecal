package com.proasecal.licman.exceptions;

public class NumeroLicenciaException extends Exception {

	private static final long serialVersionUID = 6491832603916255366L;

	public NumeroLicenciaException(String error, Throwable cause) {
		super(error,cause);
	}
	
	public NumeroLicenciaException(String error) {
		super(error);
	}	

}
