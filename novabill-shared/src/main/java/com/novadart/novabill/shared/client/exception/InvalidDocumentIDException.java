package com.novadart.novabill.shared.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvalidDocumentIDException extends Exception implements IsSerializable {
	
	private Long firstValidInvoiceID;

	private static final long serialVersionUID = -8042639486612398159L;
	
	public InvalidDocumentIDException(){
		this(1l);
	}
	
	public InvalidDocumentIDException(Long firstValidInvoiceID) {
		this.firstValidInvoiceID = firstValidInvoiceID;
	}
	
	public Long getFirstValidInvoiceID() {
		return firstValidInvoiceID;
	}
}
