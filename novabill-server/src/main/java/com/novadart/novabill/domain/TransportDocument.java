package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class TransportDocument extends AccountingDocument implements Serializable {

	private static final long serialVersionUID = 9178463460405596881L;
	
	@ManyToOne
    protected Business business;

    @ManyToOne
    protected Client client;
    
    public static Integer countTransportDocumentsForClient(Long id){
    	return countForClient(TransportDocument.class, id);
    }
    
    /*
     *Getters and setters 
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
    
    public static long countTransportDocuments() {
        return count(TransportDocument.class);
    }
    
    public static List<TransportDocument> findAllTransportDocuments() {
        return findAll(TransportDocument.class);
    }
    
    public static TransportDocument findTransportDocument(Long id) {
        return find(TransportDocument.class, id);
    }
    
    public static List<TransportDocument> findTransportDocumentEntries(int firstResult, int maxResults) {
        return findInRange(TransportDocument.class, firstResult, maxResults);
    }
    
    @Transactional
    public TransportDocument merge() {
        return merge(this);
    }
	
    /*
     * End of active record functionality
     * */

}
