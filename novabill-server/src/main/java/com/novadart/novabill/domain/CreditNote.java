package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

public class CreditNote extends AbstractInvoice implements Serializable {

	private static final long serialVersionUID = -6394611948337345685L;
	
	@ManyToOne
    protected Business business;

    @ManyToOne
    @NotNull
    protected Client client;
    
    public static Integer countCreditNotesForClient(Long id){
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
