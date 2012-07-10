package com.novadart.novabill.shared.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AuthorizationException extends Exception implements IsSerializable {

	private static final long serialVersionUID = -9025048459126219816L;
	
	private AuthorizationError error;
	
	public AuthorizationError getError() {
		return error;
	}

	public AuthorizationException(AuthorizationError error) {
		super();
		this.error = error;
	}
	
}
