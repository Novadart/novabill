package com.novadart.novabill.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


@Configurable
@Entity
public class PayPalTransactionID {
	
	private String transactionID;
	
	public PayPalTransactionID(String transactionID){
		this.transactionID = transactionID;
	}
	
	public PayPalTransactionID(){}
	
	public static List<PayPalTransactionID> findByTransactionID(String transactionID){
		return entityManager().createQuery("SELECT o FROM PayPalTransactionID o WHERE o.transactionID = :transactionID", PayPalTransactionID.class)
				.setParameter("transactionID", transactionID).getResultList();
	}
	
	
	/*
	 * Getters and setters
	 * */
	public String getTransactionID() {
        return this.transactionID;
    }
    
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    /*
     * End of getters and setters section
     * */
    
    /*
     * Active record functionality
     * */
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new PayPalTransactionID().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countPayPalTransactionIDs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PayPalTransactionID o", Long.class).getSingleResult();
    }
    
    public static List<PayPalTransactionID> findAllPayPalTransactionIDs() {
        return entityManager().createQuery("SELECT o FROM PayPalTransactionID o", PayPalTransactionID.class).getResultList();
    }
    
    public static PayPalTransactionID findPayPalTransactionID(Long id) {
        if (id == null) return null;
        return entityManager().find(PayPalTransactionID.class, id);
    }
    
    public static List<PayPalTransactionID> findPayPalTransactionIDEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PayPalTransactionID o", PayPalTransactionID.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            PayPalTransactionID attached = PayPalTransactionID.findPayPalTransactionID(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public PayPalTransactionID merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PayPalTransactionID merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    /*
     * End of active record functionality
     * */
    
    /*
     * Entity
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    /*
     * End of entity section
     * */
	
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
