package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
public class Estimation extends AccountingDocument implements Serializable {
	
	private static final long serialVersionUID = 8020837636815686509L;

	@ManyToOne
    protected Business business;

    @ManyToOne
    protected Client client;
    
    public static Integer countEstimationsForClient(Long id){
    	String query = "select count(o) FROM Estimation o where o.client.id = :clientID"; 
    	return entityManager().createQuery(query, Integer.class).setParameter("clientID", id).getSingleResult();
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
    
    public static long countEstimations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Estimation o", Long.class).getSingleResult();
    }
    
    public static List<Estimation> findAllEstimations() {
        return entityManager().createQuery("SELECT o FROM Estimation o", Estimation.class).getResultList();
    }
    
    public static Estimation findEstimation(Long id) {
        if (id == null) return null;
        return entityManager().find(Estimation.class, id);
    }
    
    public static List<Estimation> findEstimationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Estimation o", Estimation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Estimation merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Estimation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
    /*
     * End of active record functionality
     * */
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
