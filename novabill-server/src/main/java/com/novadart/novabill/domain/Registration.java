package com.novadart.novabill.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Hash;

import java.io.Serializable;
import java.util.List;


@Configurable
@Entity
public class Registration implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "{required.email}")
	@Email(message = "{invalid.format.email}")
	private String email;
	
	@NotEmpty(message = "{required.password}")
	private String password;
	
	private String confirmPassword;
	
	private Long creationTime = System.currentTimeMillis();
	
	/*
	 * Getters and setters
	 * */
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	@Hash(saltMethod = "getCreationTime")
	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	@Hash(saltMethod = "getCreationTime")
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public Long getCreationTime() {
		return creationTime;
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
        EntityManager em = new Registration().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countRegistrations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Registration o", Long.class).getSingleResult();
    }
    
    public static List<Registration> findAllRegistrations() {
        return entityManager().createQuery("SELECT o FROM Registration o", Registration.class).getResultList();
    }
    
    public static Registration findRegistration(Long id) {
        if (id == null) return null;
        return entityManager().find(Registration.class, id);
    }
    
    public static List<Registration> findRegistrationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Registration o", Registration.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	Registration attached = Registration.findRegistration(this.id);
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
    public Registration merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Registration merged = this.entityManager.merge(this);
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
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
