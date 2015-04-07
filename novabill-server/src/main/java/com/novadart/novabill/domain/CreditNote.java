package com.novadart.novabill.domain;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.List;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Entity
@Configurable
@javax.persistence.Table(name = "credit_note")
@Table(appliesTo = "credit_note",
	indexes = @Index(columnNames = "business", name = "credit_note_business_fkey_index"))
public class CreditNote extends AbstractInvoice implements Serializable {

	private static final long serialVersionUID = -6394611948337345685L;
	
	@ManyToOne
	@JoinColumn(name = "business")
    protected Business business;

    @ManyToOne
    protected Client client;
    
    public static Long countCreditNotesForClient(Long id){
    	return countForClient(CreditNote.class, id);
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
    
    @Override
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
    
    public static long countCreditNotes() {
        return count(CreditNote.class);
    }
    
    public static List<CreditNote> findAllCreditNotes() {
        return findAll(CreditNote.class);
    }
    
    public static CreditNote findCreditNote(Long id) {
        return find(CreditNote.class, id);
    }
    
    public static List<CreditNote> findCreditNoteEntries(int firstResult, int maxResults) {
        return findInRange(CreditNote.class, firstResult, maxResults);
    }
    
    @Transactional
    public CreditNote merge() {
        return merge(this);
    }
    
    /*
     * End of active record functionality section
     * */

}
