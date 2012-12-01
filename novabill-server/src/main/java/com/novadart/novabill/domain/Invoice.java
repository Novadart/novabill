package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
public class Invoice extends AbstractInvoice implements Serializable {
	
	private static final long serialVersionUID = 3369941491294470750L;

    @ManyToOne
    protected Business business;

    @ManyToOne
    protected Client client;
    
    public static Long countInvocesForClient(Long id){
    	return countForClient(Invoice.class, id);
    }
    
    /*
     * Getters and setters
     * */
    
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
        return count(Invoice.class);
    }
    
    public static List<Invoice> findAllInvoices() {
        return findAll(Invoice.class);
    }
    
    public static Invoice findInvoice(Long id) {
        return find(Invoice.class, id);
    }
    
    public static List<Invoice> findInvoiceEntries(int firstResult, int maxResults) {
        return findInRange(Invoice.class, firstResult, maxResults);
    }
    
    @Transactional
    public Invoice merge() {
        return merge(this);
    }
    
    /*
     * End of active record functionality section
     * */
    
}
