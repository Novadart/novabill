package com.novadart.novabill.shared.client.exception;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.validation.ErrorObject;

public class ValidationException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 3473233325260372771L;
	
	private List<ErrorObject> errors;
	
	private String objectClassName;

	public List<ErrorObject> getErrors() {
		return errors;
	}
	
	public String getObjectClassName() {
		return objectClassName;
	}

	public ValidationException(){
		super();
	}
	
	public ValidationException(List<ErrorObject> errors, String objectClassName){
		super();
		this.errors = errors;
		this.objectClassName = objectClassName;
	}
}
