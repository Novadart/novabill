package com.novadart.novabill.domain;

import com.novadart.novabill.annotation.Hash;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

/*
 * EmailPasswordHolder class is a superclass of entities that need to store
 * email, password, confirmPassord, and expiration data.
 */
@Configurable
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class EmailPasswordHolder {
	
	@NotEmpty(message = "{required.email}")
	@Email(message = "{invalid.format.email}")
	protected String email;
	
	@NotEmpty(message = "{required.password}")
	protected String password;
	
	@NotEmpty(message = "{required.password}")
	protected String confirmPassword;
	
	protected Date expirationDate;
	
	protected Long creationTime = System.currentTimeMillis();
	
	@Size(max = 64)
	protected String activationToken;
	
	
	protected <T extends EmailPasswordHolder> T copy(T obj) {
		obj.email = email;
		obj.password = password;
		obj.confirmPassword = confirmPassword;
		obj.expirationDate = expirationDate == null? null: new Date(expirationDate.getTime());
		obj.creationTime = creationTime;
		obj.activationToken = activationToken;
		obj.id = id;
		return obj;
	}
	
	
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

	@Hash
	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	@Hash
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Date expirationDate){
		this.expirationDate = expirationDate;
	}
	
	public Long getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Long creationTime){
		this.creationTime = creationTime;
	}

	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
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
        EntityManager em = new EmailPasswordHolder().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static EmailPasswordHolder findEmailPasswordHolder(Long id) {
        if (id == null) return null;
        return entityManager().find(EmailPasswordHolder.class, id);
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
        	EmailPasswordHolder attached = EmailPasswordHolder.findEmailPasswordHolder(this.id);
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
    
	/*
	 * End of active record functionality section
	 * */
	
	
	/*
     * Entity
     * */
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;
    
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
