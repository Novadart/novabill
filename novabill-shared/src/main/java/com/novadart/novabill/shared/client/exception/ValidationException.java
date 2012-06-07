package com.novadart.novabill.shared.client.exception;

import java.util.List;
import com.novadart.novabill.shared.client.validation.ErrorObject;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 3473233325260372771L;
	
	private List<ErrorObject> errors;

	public List<ErrorObject> getErrors() {
		return errors;
	}
	
	public ValidationException(){
		super();
	}
	
	public ValidationException(List<ErrorObject> errors){
		super();
		this.errors = errors;
	}
}
