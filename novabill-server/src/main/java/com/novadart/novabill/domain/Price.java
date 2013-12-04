package com.novadart.novabill.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.shared.client.data.PriceType;

@Configurable
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"commodity", "priceList"})})
public class Price {

	@ManyToOne
	@JoinColumn(name = "priceList")
	private PriceList priceList;
	
	@ManyToOne
	@JoinColumn(name = "commodity")
	private Commodity commodity;
	
	private PriceType priceType;
	
	private BigDecimal priceValue;
	
	public static Price findPrice(Long priceListID, Long commodityID){
		String sql = "select p from Price p where p.commodity.id = :cID and p.priceList.id = :pID";
		List<Price> r = entityManager().createQuery(sql, Price.class).
				setParameter("pID", priceListID).
				setParameter("cID", commodityID).getResultList();
		return r.size() == 0? null: r.get(0);
	}
	
	public static List<Price> findPricesForCommodity(Long id){
		String sql = "select p from Price p join p.priceList where p.commodity.id = :id";
		return entityManager().createQuery(sql, Price.class).setParameter("id", id).getResultList();
	}

	
	/*
     * Getters and setters
     * */
	
	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	public PriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(BigDecimal priceValue) {
		this.priceValue = priceValue;
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
        EntityManager em = new Price().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static Price findPrice(Long id) {
        if (id == null) return null;
        return entityManager().find(Price.class, id);
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
        	Price attached = Price.findPrice(this.id);
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
    public Price merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Price merged = this.entityManager.merge(this);
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
