package com.novadart.novabill.shared.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FreeUserAccessForbiddenException extends Exception implements IsSerializable {

	private static final long serialVersionUID = -9025048459126219816L;
	
	private FreeUserAccessErrorType error;
	
	public FreeUserAccessErrorType getError() {
		return error;
	}

	public FreeUserAccessForbiddenException(FreeUserAccessErrorType error) {
		super();
		this.error = error;
	}
	
	public FreeUserAccessForbiddenException() {
	}
	
}
