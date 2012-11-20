package com.proasecal.licman.exceptions;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 7980527712490933318L;

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	public DatabaseException(String message,Throwable cause) {
		super(message,cause);
	}
	

}
