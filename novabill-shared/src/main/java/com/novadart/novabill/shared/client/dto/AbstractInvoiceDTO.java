package com.novadart.novabill.shared.client.dto;

import java.util.Date;

public class AbstractInvoiceDTO extends AccountingDocumentDTO implements IAbstractInvoiceDTO {
	
    private Date paymentDueDate;
    
    private boolean payed;
    
	@Override
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	@Override
	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	@Override
	public boolean getPayed() {
		return payed;
	}

	@Override
	public void setPayed(boolean payed) {
		this.payed = payed;
	}

}
