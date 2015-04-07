package com.novadart.novabill.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
public abstract class AbstractInvoice extends AccountingDocument {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    protected Date paymentDueDate;
    
    @NotNull
    protected Boolean payed = false;
    
    /**
     * Getters and setters  
     */
    
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
	
	public abstract Business getBusiness();

	/**
	 * End of getters and setters section
	 */
	
}
