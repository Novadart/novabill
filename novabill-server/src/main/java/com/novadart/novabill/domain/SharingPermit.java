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

@Entity
@Configurable
public class SharingPermit {

	private String token;
	
	private String email;
	
	private Long businessID;
	
	private Long createdOn;
	
	public SharingPermit() {}

	public SharingPermit(String token, String email, Long businessID, Long createdOn) {
		this.token = token;
		this.email = email;
		this.businessID = businessID;
		this.createdOn = createdOn;
	}
	
	public SharingPermit(String token, String email, Long businessID){
		this(token, email, businessID, System.currentTimeMillis());
	}
	
	public static List<SharingPermit> findSharingPermits(Long businessID, String token){
		String sql = "select o from SharingPermit o where o.token = :tk and o.businessID = :bid";
		return entityManager().createQuery(sql, SharingPermit.class).
				setParameter("tk", token).
				setParameter("bid", businessID).getResultList();
	}
	
	
	/**
	 * Getters and setters
	 */
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getBusinessID() {
		return businessID;
	}

	public void setBusinessID(Long businessID) {
		this.businessID = businessID;
	}
	
	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
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
        EntityManager em = new SharingPermit().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countSharingPermits() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SharingPermit o", Long.class).getSingleResult();
    }
    
    public static List<SharingPermit> findAllSharingPermits() {
        return entityManager().createQuery("SELECT o FROM SharingPermit o", SharingPermit.class).getResultList();
    }
    
    public static SharingPermit findSharingPermit(Long id) {
        if (id == null) return null;
        return entityManager().find(SharingPermit.class, id);
    }
    
    public static List<SharingPermit> findSharingPermitEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SharingPermit o", SharingPermit.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	SharingPermit attached = SharingPermit.findSharingPermit(this.id);
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
    public SharingPermit merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SharingPermit merged = this.entityManager.merge(this);
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
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
}
