package com.novadart.novabill.domain;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import com.novadart.novabill.shared.client.dto.PaymentType;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

public abstract class AbstractInvoice extends AccountingDocument {

	@Type(type = "text")
    protected String paymentNote;
    
	@NotNull
    protected PaymentType paymentType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    protected Date paymentDueDate;
    
    @NotNull
    protected Boolean payed = false;
    
    /**
     * Getters and setters  
     */
    
    public String getPaymentNote() {
        return this.paymentNote;
    }
    
    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }
    
    public PaymentType getPaymentType() {
        return this.paymentType;
    }
    
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    
    public Date getPaymentDueDate() {
        return this.paymentDueDate;
    }
    
    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }
    
    public Boolean getPayed() {
		return payed;
	}

	public void setPayed(Boolean payed) {
		this.payed = payed;
	}
	
	/**
	 * End of getters and setters section
	 */
	
}
