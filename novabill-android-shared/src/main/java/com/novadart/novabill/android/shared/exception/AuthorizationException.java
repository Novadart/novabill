package com.novadart.novabill.android.shared.exception;


public class AuthorizationException extends Exception {

	private static final long serialVersionUID = -9025048459126219816L;
	
	private AuthorizationError error;
	
	public AuthorizationError getError() {
		return error;
	}

	public AuthorizationException(AuthorizationError error) {
		super();
		this.error = error;
	}
	
	public AuthorizationException() {
	}
	
}
