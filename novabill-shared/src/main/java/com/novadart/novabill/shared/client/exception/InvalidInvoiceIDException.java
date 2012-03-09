package com.novadart.novabill.shared.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvalidInvoiceIDException extends Exception implements IsSerializable {
	
	private Long firstValidInvoiceID;

	private static final long serialVersionUID = -8042639486612398159L;
	
	public InvalidInvoiceIDException(){
		this(1l);
	}
	
	public InvalidInvoiceIDException(Long firstValidInvoiceID) {
		this.firstValidInvoiceID = firstValidInvoiceID;
	}
	
	public Long getFirstValidInvoiceID() {
		return firstValidInvoiceID;
	}
}
