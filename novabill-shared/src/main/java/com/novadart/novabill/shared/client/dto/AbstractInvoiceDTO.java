package com.novadart.novabill.shared.client.dto;

import java.util.Date;

public class AbstractInvoiceDTO extends AccountingDocumentDTO {
	
    private Date paymentDueDate;
    
    private boolean payed;
    
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public boolean getPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

}
