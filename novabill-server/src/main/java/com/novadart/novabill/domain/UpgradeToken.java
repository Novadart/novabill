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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class UpgradeToken {

	private String email;
	
	private String token;
	
	public static List<UpgradeToken> findByEmail(String email){
		String query = "select st from UpgradeToken st where st.email = :email";
		return entityManager().createQuery(query, UpgradeToken.class).setParameter("email", email).getResultList();
	}
	
	/**
	 * Getters and setters
	 */
	
	public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * End of getters and setters section
     */
    
    /**
     * Active record functionality
     */
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new UpgradeToken().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countUpgradeTokens() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UpgradeToken o", Long.class).getSingleResult();
    }
    
    public static List<UpgradeToken> findAllUpgradeTokens() {
        return entityManager().createQuery("SELECT o FROM UpgradeToken o", UpgradeToken.class).getResultList();
    }
    
    public static UpgradeToken findUpgradeToken(Long id) {
        if (id == null) return null;
        return entityManager().find(UpgradeToken.class, id);
    }
    
    public static List<UpgradeToken> findUpgradeTokenEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UpgradeToken o", UpgradeToken.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            UpgradeToken attached = UpgradeToken.findUpgradeToken(this.id);
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
    public UpgradeToken merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UpgradeToken merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    /**
     * End of active record functionality section
     */
    
    /**
     * Entity
     */
    
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
    
    /**
     * End of entity section
     */
    
}
