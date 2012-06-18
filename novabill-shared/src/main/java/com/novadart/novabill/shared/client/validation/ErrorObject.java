package com.novadart.novabill.shared.client.validation;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ErrorObject implements Serializable, IsSerializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Field field;
	
	private ErrorCode errorCode;
	
	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorObject(){}

	public ErrorObject(Field field, ErrorCode errorCode) {
		this.field = field;
		this.errorCode = errorCode;
	}
	

}
