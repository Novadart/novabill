package com.novadart.novabill.shared.client.exception;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.validation.ErrorObject;

public class ValidationException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 3473233325260372771L;
	
	private List<ErrorObject> errors;
	
	private String objectRepr;

	public List<ErrorObject> getErrors() {
		return errors;
	}
	
	public Object getObjectRepr() {
		return objectRepr;
	}

	public ValidationException(){
		super();
	}
	
	public ValidationException(List<ErrorObject> errors, String objectRepr){
		super();
		this.errors = errors;
		this.objectRepr = objectRepr;
	}
}
