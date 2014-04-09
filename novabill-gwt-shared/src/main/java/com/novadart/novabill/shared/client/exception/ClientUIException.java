package com.novadart.novabill.shared.client.exception;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientUIException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 2162370773300464186L;
	
	private Map<String, Object> errorMessage;

	public Map<String, Object> getErrorMessage() {
		return errorMessage;
	}

	public ClientUIException() {
		super();
	}

	public ClientUIException(Map<String, Object> errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	
	

}
