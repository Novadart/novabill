package com.novadart.novabill.domain;

import java.io.Serializable;
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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.novadart.novabill.annotation.Trimmed;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
@javax.persistence.Table(name = "accounting_document_item")
@Table(appliesTo = "accounting_document_item",
		indexes = @Index(columnNames = "accounting_document", name = "accoutinting_document_fkey_index"))
public class AccountingDocumentItem implements Serializable {
	
	private static final long serialVersionUID = -1072295560395962907L;

	private BigDecimal price;

    @Type(type = "text")
    @Size(max = 500)
    @NotNull
    @Trimmed
    private String description;

    @Size(max = 255)
    private String unitOfMeasure;

    private BigDecimal tax;
    
    private BigDecimal quantity;
    
    private BigDecimal totalBeforeTax;
    
    private BigDecimal totalTax;
    
    private BigDecimal total;
    
    private BigDecimal discount;
    
    private BigDecimal weight;
    
    @Size(max = 50)
	@Trimmed
	private String sku;
    
    @JoinColumn(name = "accounting_document")
    @ManyToOne
    private AccountingDocument accountingDocument;
    
    /*
     * Getters and setters
     * */
    
    public BigDecimal getPrice() {
        return this.price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
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
    
    public BigDecimal getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getTotalBeforeTax() {
        return this.totalBeforeTax;
    }
    
    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }
    
    public BigDecimal getTotalTax() {
        return this.totalTax;
    }
    
    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }
    
    public BigDecimal getTotal() {
        return this.total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public AccountingDocument getAccountingDocument() {
        return this.accountingDocument;
    }
    
    public void setAccountingDocument(AccountingDocument accountingDocument) {
        this.accountingDocument = accountingDocument;
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
        EntityManager em = new AccountingDocumentItem().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countAccountingDocumentItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AccountingDocumentItem o", Long.class).getSingleResult();
    }
    
    public static List<AccountingDocumentItem> findAllAccountingDocumentItems() {
        return entityManager().createQuery("SELECT o FROM AccountingDocumentItem o", AccountingDocumentItem.class).getResultList();
    }
    
    public static AccountingDocumentItem findAccountingDocumentItem(Long id) {
        if (id == null) return null;
        return entityManager().find(AccountingDocumentItem.class, id);
    }
    
    public static List<AccountingDocumentItem> findAccountingDocumentItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AccountingDocumentItem o", AccountingDocumentItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AccountingDocumentItem attached = AccountingDocumentItem.findAccountingDocumentItem(this.id);
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
    public AccountingDocumentItem merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AccountingDocumentItem merged = this.entityManager.merge(this);
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
    	return String.format("<id: %d, sku: %s>", id, Strings.nullToEmpty(sku));
    }
    
}
