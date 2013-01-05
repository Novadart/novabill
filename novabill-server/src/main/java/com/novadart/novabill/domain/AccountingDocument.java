package com.novadart.novabill.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AccountingDocument {
	
	protected Long documentID;

	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    protected Date accountingDocumentDate;
    
    protected Integer accountingDocumentYear;
    
    @Type(type = "text")
    @Size(max = 1500)
    protected String note;
    
    @NotNull
    protected BigDecimal total;
    
    @NotNull
    protected BigDecimal totalTax;
    
    @NotNull
    protected BigDecimal totalBeforeTax;
    
    @Type(type = "text")
    @Size(max = 1500)
    protected String paymentNote;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountingDocument", orphanRemoval = true)
    @OrderBy("id")
    @Valid
    protected List<AccountingDocumentItem> accountingDocumentItems = new LinkedList<AccountingDocumentItem>();
    
    protected static int getYear(Date date){
    	Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
    }
    
	protected void setAccountingDocumentYear(Integer year) {
        this.accountingDocumentYear = year;
    }
	
	protected static <T> Long countForClient(Class<T> cls, Long id){
		String query = String.format("select count(o) FROM %s o where o.client.id = :clientID", cls.getSimpleName()); 
    	return entityManager().createQuery(query, Long.class).setParameter("clientID", id).getSingleResult();
	}
	
	@Transient
    private static final Comparator<AccountingDocument> ACCOUNTING_DOCUMENT_COMPARATOR = new AccountingDocumentComparator();
	
	public static <T extends AccountingDocument> List<T> sortAccountingDocuments(Collection<T> collection){
    	SortedSet<T> sortedSet = new TreeSet<T>(ACCOUNTING_DOCUMENT_COMPARATOR);
    	sortedSet.addAll(collection);
    	return new ArrayList<T>(sortedSet);
    }
	
	public static <T extends AccountingDocument> T getAccountingDocument(Class<T> cls, Long documentID, int year){
		String query = String.format("select o from %s o where o.documentID = :docID and o.accountingDocumentYear = :year", cls.getSimpleName());
		List<T> result = entityManager().createQuery(query, cls).setParameter("docID", documentID).setParameter("year", year).getResultList();
		return result.size() > 0? result.get(0): null;
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
    
    public String getPaymentNote() {
        return this.paymentNote;
    }
    
    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }
    
    public List<AccountingDocumentItem> getAccountingDocumentItems() {
        return this.accountingDocumentItems;
    }
    
    public void setAccountingDocumentItems(List<AccountingDocumentItem> accountingDocumentItems) {
        this.accountingDocumentItems = accountingDocumentItems;
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
    
    protected static <T> long count(Class<T> cls){
    	String query = String.format("SELECT COUNT(o) FROM %s o", cls.getSimpleName());
    	return entityManager().createQuery(query, Long.class).getSingleResult();
    }
    
    public static long countAccountingDocuments() {
        return count(AccountingDocument.class);
    }
    
    protected static <T> List<T> findAll(Class<T> cls){
    	String query = String.format("SELECT o FROM %s o", cls.getSimpleName());
    	return entityManager().createQuery(query, cls).getResultList();
    }
    
    public static List<AccountingDocument> findAllAccountingDocuments() {
        return findAll(AccountingDocument.class);
    }
    
    protected static <T> T find(Class<T> cls, Long id){
    	if (id == null) return null;
        return entityManager().find(cls, id);
    }
    
    public static AccountingDocument findAccountingDocument(Long id) {
        return find(AccountingDocument.class, id);
    }
    
    protected static <T> List<T> findInRange(Class<T> cls, int  firstResult, int maxResults){
    	String query = String.format("SELECT o FROM %s o", cls.getSimpleName());
    	return entityManager().createQuery(query, cls).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<AccountingDocument> findAccountingDocumentEntries(int firstResult, int maxResults) {
        return findInRange(AccountingDocument.class, firstResult, maxResults);
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
    protected <T> T merge(T o){
    	if (this.entityManager == null) this.entityManager = entityManager();
        T merged = this.entityManager.merge(o);
        this.entityManager.flush();
        return merged;
    }
    
    @Transactional
    public AccountingDocument merge() {
        return merge(this);
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
    
    /*End of entity section*/
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
}
