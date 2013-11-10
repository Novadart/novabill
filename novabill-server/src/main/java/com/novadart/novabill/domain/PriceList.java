package com.novadart.novabill.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Trimmed;

@Configurable
@Entity
public class PriceList {

	@Size(max = 255)
	@NotBlank
	@Trimmed
	private String name;
	
	@ManyToOne
	private Business business;

	@PreRemove
	@Transactional
	public void preremove(){
		TypedQuery<Commodity> tquery = entityManager().createQuery("select c from Commodity c where c.business.id = :businessID", Commodity.class);
		tquery.setParameter("businessID", getBusiness().getId());
		for(Commodity commodity: tquery.getResultList()){
			Map<String, BigDecimal> customPrices = commodity.getCustomPrices();
			if(customPrices.containsKey(name)){
				customPrices.remove(name);
				commodity.setCustomPrices(customPrices);
			}
		}
	}
	
	
	/*
     * Getters and setters
     * */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Business getBusiness() {
		return business;
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
        EntityManager em = new PriceList().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countPriceLists() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PriceList o", Long.class).getSingleResult();
    }
    
    public static List<PriceList> findAllPriceLists() {
        return entityManager().createQuery("SELECT o FROM PriceList o", PriceList.class).getResultList();
    }
    
    public static PriceList findPriceList(Long id) {
        if (id == null) return null;
        return entityManager().find(PriceList.class, id);
    }
    
    public static List<PriceList> findPriceListEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PriceList o", PriceList.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	PriceList attached = PriceList.findPriceList(this.id);
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
    public PriceList merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PriceList merged = this.entityManager.merge(this);
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
