package com.novadart.novabill.shared.client.dto;

import java.util.Date;
import com.google.gwt.user.client.rpc.IsSerializable;

public class InvoiceDTO extends AccountingDocumentDTO implements IsSerializable {
    
    private String paymentNote;
    
    private PaymentType paymentType;
    
    private Date paymentDueDate;
    
    private boolean payed;
    
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public String getPaymentNote() {
		return paymentNote;
	}

	public void setPaymentNote(String paymentNote) {
		this.paymentNote = paymentNote;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public boolean getPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}
	
}
