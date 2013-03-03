package com.novadart.novabill.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import com.novadart.novabill.shared.client.dto.PaymentDateType;

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
    
    @Size(max = 255)
	@NotNull
    private String paymentTypeName;
    
    @NotNull
	private PaymentDateType paymentDateGenerator;
    
    private Integer paymentDateDelta;
    
    
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

	public PaymentDateType getPaymentDateGenerator() {
		return paymentDateGenerator;
	}

	public void setPaymentDateGenerator(PaymentDateType paymentDateGenerator) {
		this.paymentDateGenerator = paymentDateGenerator;
	}

	public Integer getPaymentDateDelta() {
		return paymentDateDelta;
	}

	public void setPaymentDateDelta(Integer paymentDateDelta) {
		this.paymentDateDelta = paymentDateDelta;
	}

	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}
	
	/**
	 * End of getters and setters section
	 */
	
}
