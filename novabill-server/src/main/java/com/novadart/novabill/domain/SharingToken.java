package com.novadart.novabill.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Trimmed;

@Entity
@Configurable
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"businessid", "token"})})
public class SharingToken {

	@Size(max = 255)
    @Email
    @Trimmed
    @NotNull
	private String email;
	
	private Long createdOn = System.currentTimeMillis();
	
	private Long businessID;
	
	private String token;

	public SharingToken(){}
	
	public SharingToken(String email, Long createdOn, Long businessID, String token) {
		this.email = email;
		this.createdOn = createdOn;
		this.businessID = businessID;
		this.token = token;
	}

	public SharingToken(String email, Long businessID, String token) {
		this(email, System.currentTimeMillis(), businessID, token);
	}
	
	public static SharingToken findSharingToken(Long businessID, String token){
		String sql = "select st from SharingToken st where st.businessID = :businessID and st.token = :token";
		List<SharingToken> r = entityManager().createQuery(sql, SharingToken.class).
				setParameter("businessID", businessID).
				setParameter("token", token).getResultList();
		return r.size() == 0? null: r.get(0);
	}

	/**
	 * Getters and setters
	 */
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getBusinessID() {
		return businessID;
	}

	public void setBusinessID(Long businessID) {
		this.businessID = businessID;
	}

	public String getToken() {
		return token;
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
        EntityManager em = new SharingToken().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countSharingTokens() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SharingToken o", Long.class).getSingleResult();
    }
    
    public static List<SharingToken> findAllSharingTokens() {
        return entityManager().createQuery("SELECT o FROM SharingToken o", SharingToken.class).getResultList();
    }
    
    public static SharingToken findSharingToken(Long id) {
        if (id == null) return null;
        return entityManager().find(SharingToken.class, id);
    }
    
    public static List<SharingToken> findSharingTokenEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SharingToken o", SharingToken.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	SharingToken attached = SharingToken.findSharingToken(this.id);
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
    public SharingToken merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SharingToken merged = this.entityManager.merge(this);
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
