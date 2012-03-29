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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractInvoice {
	
	protected Long invoiceID;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    protected Date invoiceDate;
    
    protected Integer invoiceYear;
    
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
    
    @SuppressWarnings("unused")
	@PreUpdate
    protected void onUpdate(){
    	if(invoiceDate != null)
    		invoiceYear = getYear(invoiceDate);
    }
    
    @SuppressWarnings("unused")
    @PrePersist
    protected void onPersist(){
    	if(invoiceDate != null)
    		invoiceYear = getYear(invoiceDate);
    }

	protected void setInvoiceYear(Integer invoiceYear) {
        this.invoiceYear = invoiceYear;
    }
	
	/*
	 * Getters and setters
	 * *
	 */
	
	public Long getInvoiceID() {
        return this.invoiceID;
    }
    
    public void setInvoiceID(Long invoiceID) {
        this.invoiceID = invoiceID;
    }
    
    public Date getInvoiceDate() {
        return this.invoiceDate;
    }
    
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    public Integer getInvoiceYear() {
        return this.invoiceYear;
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
        EntityManager em = new AbstractInvoice() {
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countAbstractInvoices() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AbstractInvoice o", Long.class).getSingleResult();
    }
    
    public static List<AbstractInvoice> findAllAbstractInvoices() {
        return entityManager().createQuery("SELECT o FROM AbstractInvoice o", AbstractInvoice.class).getResultList();
    }
    
    public static AbstractInvoice findAbstractInvoice(Long id) {
        if (id == null) return null;
        return entityManager().find(AbstractInvoice.class, id);
    }
    
    public static List<AbstractInvoice> findAbstractInvoiceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AbstractInvoice o", AbstractInvoice.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AbstractInvoice attached = AbstractInvoice.findAbstractInvoice(this.id);
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
    public AbstractInvoice merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AbstractInvoice merged = this.entityManager.merge(this);
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
    @GeneratedValue(strategy = GenerationType.TABLE)
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
