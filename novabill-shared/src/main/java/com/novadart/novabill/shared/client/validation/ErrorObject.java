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
	
	private Object[] arguments;
	
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

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public ErrorObject(){}
	
	public ErrorObject(Field field, ErrorCode errorCode) {
		this(field, errorCode, null);
	}
	
	public ErrorObject(Field field, ErrorCode errorCode, Object[] arguments){
		this.field = field;
		this.errorCode = errorCode;
		this.arguments = arguments;
	}
	

}
