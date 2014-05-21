package com.novadart.novabill.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Trimmed;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.utils.fts.TermValueFilterFactory;

@Indexed
@AnalyzerDef(name = FTSNamespace.DEFAULT_COMMODITY_ANALYZER,
tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
filters = {
	@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
	@TokenFilterDef(factory = LowerCaseFilterFactory.class)
})
@Analyzer(definition = FTSNamespace.DEFAULT_COMMODITY_ANALYZER)
@FullTextFilterDefs({
	@FullTextFilterDef(name = FTSNamespace.COMMODITY_BY_BUSINESS_ID_FILTER, impl = TermValueFilterFactory.class)
})
@Configurable
@Entity
public class Commodity implements Serializable {
	
	private static final long serialVersionUID = 4265058605330997015L;
	
	@NotBlank
	@Size(max = 50)
	@Trimmed
	private String sku;

    @Field(name = FTSNamespace.DESCRIPTION)
    @Size(max = 255)
    @NotNull
    @Trimmed
    private String description;

    @Size(max = 255)
    private String unitOfMeasure;
    
    private BigDecimal tax;

    @ManyToOne
    private Business business;
    
    private boolean service;
    
    @Column(name = "weight", precision = 19, scale = 3)
    private BigDecimal weight;
    
    public Commodity shallowCopy(){
    	Commodity newCommodity = new Commodity();
    	newCommodity.setSku(getSku());
    	newCommodity.setDescription(getDescription());
    	newCommodity.setUnitOfMeasure(getUnitOfMeasure());
    	newCommodity.setTax(getTax());
    	newCommodity.setService(isService());
    	newCommodity.setBusiness(getBusiness());
    	return newCommodity;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "commodity")
    private Set<Price> prices = new HashSet<>();
    
    public boolean skuExists(){
    	String sql = "select c from Commodity c where c.business.id = :id and c.sku = :sku";
    	return entityManager().createQuery(sql, Commodity.class).
    			setParameter("id", getBusiness().getId()).
    			setParameter("sku", getSku()).
    			setFirstResult(0).setMaxResults(1).getResultList().size() == 1;
    }
    
    public static String generateSku(){
    	return PriceListConstants.GENERATED_SKUS_PREFIX + System.currentTimeMillis();
    }
    
    /*
     * Getters and setters
     * */
   
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUnitOfMeasure() {
        return this.unitOfMeasure;
    }
    
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
    
    public BigDecimal getTax() {
        return this.tax;
    }
    
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    
    public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public boolean isService() {
		return service;
	}

	public void setService(boolean service) {
		this.service = service;
	}

	public Set<Price> getPrices() {
		return prices;
	}

	public void setPrices(Set<Price> prices) {
		this.prices = prices;
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
        EntityManager em = new Commodity().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countCommodities() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Commodity o", Long.class).getSingleResult();
    }
    
    public static List<Commodity> findAllCommodities() {
        return entityManager().createQuery("SELECT o FROM Commodity o", Commodity.class).getResultList();
    }
    
    public static Commodity findCommodity(Long id) {
        if (id == null) return null;
        return entityManager().find(Commodity.class, id);
    }
    
    public static List<Commodity> findCommodityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Commodity o", Commodity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Commodity attached = Commodity.findCommodity(this.id);
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
    public Commodity merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Commodity merged = this.entityManager.merge(this);
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
    	return String.format("<id: %d, sku: %s>", id, sku);
    }
	
}
