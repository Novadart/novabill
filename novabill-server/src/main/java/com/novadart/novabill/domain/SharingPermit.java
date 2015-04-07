package com.novadart.novabill.domain;

import com.novadart.novabill.annotation.SharingPermitEmailBusinessUnique;
import com.novadart.novabill.annotation.Trimmed;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Configurable
@Table(name = "sharing_permit", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "business"})})
@org.hibernate.annotations.Table(appliesTo = "sharing_permit",
	indexes = @Index(columnNames = "business", name = "sharing_permit_business_fkey_index"))
@SharingPermitEmailBusinessUnique
public class SharingPermit {

	@Size(max = 255)
    @Email
    @Trimmed
    @NotNull
	private String email;
	
	@Size(max = 255)
	private String description;
	
	private Long createdOn = System.currentTimeMillis();
	
	@JoinColumn(name = "business")
	@ManyToOne
	private Business business;
	
	public static SharingPermit findByEmailForBusiness(String email, Long businessID){
		String sql = "select sp from SharingPermit sp where sp.email = :email and sp.business.id = :businessID";
		List<SharingPermit> r = entityManager().createQuery(sql, SharingPermit.class).
					setParameter("email", email).
					setParameter("businessID", businessID).getResultList();
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

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	
	public Business getBusiness() {
		return business;
	}


	public void setBusiness(Business business) {
		this.business = business;
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
