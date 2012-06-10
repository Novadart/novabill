package com.novadart.novabill.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "document_id_sequence", initialValue = 1, allocationSize = 1, sequenceName = "document_id_sequence")
public abstract class AccountingDocument {
	
	protected Long documentID;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    protected Date accountingDocumentDate;
    
    protected Integer accountingDocumentYear;
    
    @Type(type = "text")
    protected String note;
    
    protected BigDecimal total;
    
    protected BigDecimal totalTax;
    
    protected BigDecimal totalBeforeTax;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "invoice", orphanRemoval = true)
    @OrderBy("id")
    protected List<InvoiceItem> invoiceItems = new LinkedList<InvoiceItem>();
    
    protected static int getYear(Date date){
    	Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
    }
    
	protected void setAccountingDocumentYear(Integer year) {
        this.accountingDocumentYear = year;
    }
	
	/*
	 * Getters and setters
	 * *
	 */
	
	public Long getDocumentID() {
        return this.documentID;
    }
    
    public void setDocumentID(Long documentID) {
        this.documentID = documentID;
    }
    
    public Date getAccountingDocumentDate() {
        return this.accountingDocumentDate;
    }
    
    public void setAccountingDocumentDate(Date accountingDocumentDate) {
        this.accountingDocumentDate = accountingDocumentDate;
    	accountingDocumentYear = accountingDocumentDate != null? getYear(accountingDocumentDate): null;
    }
    
    public Integer getAccountingDocumentYear() {
        return this.accountingDocumentYear;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public BigDecimal getTotal() {
        return this.total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public BigDecimal getTotalTax() {
        return this.totalTax;
    }
    
    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }
    
    public BigDecimal getTotalBeforeTax() {
        return this.totalBeforeTax;
    }
    
    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }
    
    public List<InvoiceItem> getInvoiceItems() {
        return this.invoiceItems;
    }
    
    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
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
        EntityManager em = new AccountingDocument() {
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countAccountingDocuments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AccountingDocument o", Long.class).getSingleResult();
    }
    
    public static List<AccountingDocument> findAllAccountingDocuments() {
        return entityManager().createQuery("SELECT o FROM AccountingDocument o", AccountingDocument.class).getResultList();
    }
    
    public static AccountingDocument findAccountingDocument(Long id) {
        if (id == null) return null;
        return entityManager().find(AccountingDocument.class, id);
    }
    
    public static List<AccountingDocument> findAccountingDocumentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AccountingDocument o", AccountingDocument.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AccountingDocument attached = AccountingDocument.findAccountingDocument(this.id);
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
    public AccountingDocument merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AccountingDocument merged = this.entityManager.merge(this);
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_id_sequence")
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
    
    /*End of entity section*/
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
}
