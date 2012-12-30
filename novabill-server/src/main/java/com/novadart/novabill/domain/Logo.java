package com.novadart.novabill.domain;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


@Entity
@Configurable
public class Logo {
	
	public static enum LogoFormat{
		PNG, JPEG
	}
	
	private String name;

    private Integer width;

    private Integer height;
    
    private LogoFormat format;
    
    @Basic(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private byte[] data;
    
    @NotNull
    private Long businessID;

    public static Logo getLogoByBusinessID(Long businessID){
		String query = "select logo from Logo logo where logo.businessID = :id";
		List<Logo> result = entityManager().createQuery(query, Logo.class).setParameter("id", businessID).getResultList();
		return result.size() > 0? result.get(0): null;
	}
    
    /*
	 * Getters and setters
	 * */
    
	public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getWidth() {
        return this.width;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public Integer getHeight() {
        return this.height;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public LogoFormat getFormat() {
		return format;
	}

	public void setFormat(LogoFormat format) {
		this.format = format;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Long getBusinessID() {
		return businessID;
	}

	public void setBusinessID(Long businessID) {
		this.businessID = businessID;
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
        EntityManager em = new Logo().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countLogoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Logo o", Long.class).getSingleResult();
    }
    
    public static List<Logo> findAllLogoes() {
        return entityManager().createQuery("SELECT o FROM Logo o", Logo.class).getResultList();
    }
    
    public static Logo findLogo(Long id) {
        if (id == null) return null;
        return entityManager().find(Logo.class, id);
    }
    
    public static List<Logo> findLogoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Logo o", Logo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Logo attached = Logo.findLogo(this.id);
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
    public Logo merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Logo merged = this.entityManager.merge(this);
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
