package com.novadart.novabill.shared.client.validation;

public class ErrorObject {
	
	private Field field;
	
	private ErrorCode errorCode;
	
	private Object parameter;

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

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	
	public ErrorObject(){}

	public ErrorObject(Field field, ErrorCode errorCode, Object parameter) {
		this.field = field;
		this.errorCode = errorCode;
		this.parameter = parameter;
	}
	

}
