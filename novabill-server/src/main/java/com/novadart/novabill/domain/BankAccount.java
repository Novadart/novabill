package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class BankAccount implements Serializable {
	
	private static final long serialVersionUID = 5546509172175255602L;

	@Column(unique = true)
    @Size(max = 30)
    private String iban;

    @Size(max = 255)
    private String name;

    @ManyToOne
    private Business business;
    
    /*
     * Getters and setters
     * */
    
    public String getIban() {
        return this.iban;
    }
    
    public void setIban(String iban) {
        this.iban = iban;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Business getBusiness() {
        return this.business;
    }
    
    public void setBusiness(Business business) {
        this.business = business;
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
        EntityManager em = new BankAccount().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countBankAccounts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BankAccount o", Long.class).getSingleResult();
    }
    
    public static List<BankAccount> findAllBankAccounts() {
        return entityManager().createQuery("SELECT o FROM BankAccount o", BankAccount.class).getResultList();
    }
    
    public static BankAccount findBankAccount(Long id) {
        if (id == null) return null;
        return entityManager().find(BankAccount.class, id);
    }
    
    public static List<BankAccount> findBankAccountEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BankAccount o", BankAccount.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BankAccount attached = BankAccount.findBankAccount(this.id);
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
    public BankAccount merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BankAccount merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    /*
     * End of active record functionality section
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
    	return String.format("<id: %d, iban: %s, name: %s>", id, iban, name);
    }
	
}
