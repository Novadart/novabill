package com.novadart.novabill.shared.client.validation;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/*
 * ErrorObject object holds the information for a single validation error
 * ({@link ErrorCode}) of a field ({@link Field}). In case the field belongs
 * to an object placed in an array(s) the index(es) is also provided - for
 * example, this can happen for accounting document items. 
 * 
 */
public class ErrorObject implements Serializable, IsSerializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Field field;
	
	private ErrorCode errorCode;
	
	private Integer[] indexes;
	
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

	public Integer[] getIndexes() {
		return indexes;
	}

	public void setIndexes(Integer[] indexes) {
		this.indexes = indexes;
	}

	public ErrorObject(){}
	
	public ErrorObject(Field field, ErrorCode errorCode) {
		this(field, errorCode, null);
	}
	
	public ErrorObject(Field field, ErrorCode errorCode, Integer[] indexes){
		this.field = field;
		this.errorCode = errorCode;
		this.indexes = indexes;
	}
	

}
