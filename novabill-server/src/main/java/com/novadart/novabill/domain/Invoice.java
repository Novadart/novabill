package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.shared.client.dto.PaymentType;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
public class Invoice extends AccountingDocument implements Serializable {
	
	private static final long serialVersionUID = 3369941491294470750L;

    @Type(type = "text")
    private String paymentNote;
    
	@NotNull
    private PaymentType paymentType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date paymentDueDate;
    
    @NotNull
    private Boolean payed = false; 
    
    @ManyToOne
    protected Business business;

    @ManyToOne
    @NotNull
    protected Client client;
    
    public static Integer countInvocesForClient(Long id){
    	String query = "select count(o) FROM Invoice o where o.client.id = :clientID"; 
    	return entityManager().createQuery(query, Integer.class).setParameter("clientID", id).getSingleResult();
    }
    
    /*
     * Getters and setters
     * */
    
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
    
    public Business getBusiness() {
        return this.business;
    }
    
    public void setBusiness(Business business) {
        this.business = business;
    }
    
    public Client getClient() {
        return this.client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    /*
     * End of getters and setters section
     * */
    
    /*
     * Active record functionality
     * */
    
    public static long countInvoices() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Invoice o", Long.class).getSingleResult();
    }
    
    public static List<Invoice> findAllInvoices() {
        return entityManager().createQuery("SELECT o FROM Invoice o", Invoice.class).getResultList();
    }
    
    public static Invoice findInvoice(Long id) {
        if (id == null) return null;
        return entityManager().find(Invoice.class, id);
    }
    
    public static List<Invoice> findInvoiceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Invoice o", Invoice.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Invoice merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Invoice merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    /*
     * End of active record functionality section
     * */
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
