package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
public class Estimation extends AccountingDocument implements Serializable {
	
	private static final long serialVersionUID = 8020837636815686509L;
	
	@Size(max = 1500)
	private String limitations;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
	private Date validTill;
	
	@Column(columnDefinition = "boolean default false")
	private boolean incognito = false;

	@ManyToOne
    protected Business business;

    @ManyToOne
    protected Client client;
    
	public static Long countEstimationsForClient(Long id){
    	return countForClient(Estimation.class, id);
    }
    
    /*
     *Getters and setters 
     * */
    
	public String getLimitations() {
		return limitations;
	}

	public void setLimitations(String limitations) {
		this.limitations = limitations;
	}
	
    public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public boolean isIncognito() {
		return incognito;
	}

	public void setIncognito(boolean incognito) {
		this.incognito = incognito;
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
    
    public static long countEstimations() {
        return count(Estimation.class);
    }
    
    public static List<Estimation> findAllEstimations() {
        return findAll(Estimation.class);
    }
    
    public static Estimation findEstimation(Long id) {
        return find(Estimation.class, id);
    }
    
    public static List<Estimation> findEstimationEntries(int firstResult, int maxResults) {
        return findInRange(Estimation.class, firstResult, maxResults);
    }
    
    @Transactional
    public Estimation merge() {
        return merge(this);
    }
	
    /*
     * End of active record functionality
     * */
    
}
