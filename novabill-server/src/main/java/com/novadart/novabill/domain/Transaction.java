package com.novadart.novabill.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Configurable
@Entity
public class Transaction {
	
	private String transactionID;
	
	public Transaction(String transactionID){
		this.transactionID = transactionID;
	}
	
	public Transaction(){}
	
	public static List<Transaction> findByTransactionID(String transactionID){
		List<Transaction> r = entityManager().createQuery("SELECT o FROM Transaction o WHERE o.transactionID = :transactionID", Transaction.class)
				.setParameter("transactionID", transactionID).getResultList();
		return new ArrayList<Transaction>(r);
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
        EntityManager em = new Transaction().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countTransactions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Transaction o", Long.class).getSingleResult();
    }
    
    public static List<Transaction> findAllTransactions() {
        return entityManager().createQuery("SELECT o FROM Transaction o", Transaction.class).getResultList();
    }
    
    public static Transaction findTransaction(Long id) {
        if (id == null) return null;
        return entityManager().find(Transaction.class, id);
    }
    
    public static List<Transaction> findTransactionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Transaction o", Transaction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Transaction attached = Transaction.findTransaction(this.id);
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
    public Transaction merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Transaction merged = this.entityManager.merge(this);
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
