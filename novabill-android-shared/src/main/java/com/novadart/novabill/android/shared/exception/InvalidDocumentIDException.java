package com.novadart.novabill.android.shared.exception;


public class InvalidDocumentIDException extends Exception {
	
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
