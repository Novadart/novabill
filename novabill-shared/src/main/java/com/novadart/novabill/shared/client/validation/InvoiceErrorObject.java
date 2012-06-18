package com.novadart.novabill.shared.client.validation;

import java.util.List;

public class InvoiceErrorObject extends ErrorObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvoiceErrorObject() {
	}
	
	public InvoiceErrorObject(Field field, ErrorCode errorCode, List<Long> gaps) {
		super(field, errorCode);
		this.gaps = gaps;
	}

	private List<Long> gaps;

	public List<Long> getGaps() {
		return gaps;
	}

	public void setGaps(List<Long> gaps) {
		this.gaps = gaps;
	}
}
