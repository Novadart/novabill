package com.novadart.novabill.domain;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.bridge.builtin.LongBridge;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.utils.fts.TermValueFilterFactory;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Business implements Serializable, Taxable {

	private static final long serialVersionUID = 261999997691744944L;
	
	@Size(max = 255)
	@NotNull
    private String name = "";

    @Size(max = 255)
    @NotNull
    private String address = "";

    @Size(max = 10)
    @NotNull
    private String postcode = "";

    @Size(max = 60)
    @NotNull
    private String city = "";

    @Size(max = 2)
    //@NotNull
    private String province = "";

    @Size(max = 3)
    private String country;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 25)
    private String phone;

    @Size(max = 25)
    private String mobile;

    @Size(max = 25)
    private String fax;

    @Size(max = 255)
    private String web;

    @Size(max = 25)
    //@Pattern(regexp = RegularExpressionConstants.VAT_ID_REGEX)
    private String vatID;

    @Size(max = 25)
    //@Pattern(regexp = RegularExpressionConstants.SSN_REGEX)
    private String ssn;

    private Long nonFreeAccountExpirationTime; 
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Item> items = new HashSet<Item>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<BankAccount> accounts = new HashSet<BankAccount>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Invoice> invoices = new HashSet<Invoice>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Estimation> estimations = new HashSet<Estimation>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<CreditNote> creditNotes = new HashSet<CreditNote>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<TransportDocument> transportDocuments = new HashSet<TransportDocument>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Client> clients = new HashSet<Client>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Principal> principals = new HashSet<Principal>();
    
    public List<Invoice> getAllInvoicesInRange(int start, int length){
    	String query = "select invoice from Invoice invoice where invoice.business.id = :id order by invoice.accountingDocumentYear desc, invoice.documentID desc";
    	return entityManager().createQuery(query, Invoice.class).setParameter("id", getId()).setFirstResult(start).setMaxResults(length).getResultList();
    }
    
    public List<Estimation> getAllEstimationsInRange(int start, int length){
    	String query = "select estimation from Estimation estimation where estimation.business.id = :id order by estimation.accountingDocumentDate desc";
    	return entityManager().createQuery(query, Estimation.class).setParameter("id", getId()).setFirstResult(start).setMaxResults(length).getResultList();
    }
    
    public List<CreditNote> getAllCreditNotesInRange(int start, int length){
    	String query = "select creditNote from CreditNote creditNote where creditNote.business.id = :id order by creditNote.accountingDocumentYear desc, creditNote.documentID desc";
    	return entityManager().createQuery(query, CreditNote.class).setParameter("id", getId()).setFirstResult(start).setMaxResults(length).getResultList();
    }
    
    public List<TransportDocument> getAllTransportDocsInRange(int start, int length){
    	String query = "select tranDoc from TransportDocument tranDoc where tranDoc.business.id = :id order by tranDoc.accountingDocumentDate desc";
    	return entityManager().createQuery(query, TransportDocument.class).setParameter("id", getId()).setFirstResult(start).setMaxResults(length).getResultList();
    }
    
    private <T extends AccountingDocument> Long getNextAccountingDocDocumentID(Class<T> cls){
    	String query = String.format("select max(o.documentID) from %s o where o.business.id = :businessId and o.accountingDocumentYear = :year",
    									cls.getSimpleName());
    	Long id = entityManager.createQuery(query, Long.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", Calendar.getInstance().get(Calendar.YEAR)).getSingleResult(); 
    	return (id == null)? 1: id + 1;
    }
    
    public Long getNextInvoiceDocumentID(){
    	return getNextAccountingDocDocumentID(Invoice.class);
    }
    
    public Long getNextCreditNoteDocumentID(){
    	return getNextAccountingDocDocumentID(CreditNote.class);
    }
    
    public Long getNextEstimationDocumentID(){
    	return getNextAccountingDocDocumentID(Estimation.class);
    }
    
    public Long getNextTransportDocDocumentID(){
    	return getNextAccountingDocDocumentID(TransportDocument.class);
    }
    
    private <T extends AccountingDocument> List<T> fetchAccountingDocsEagerly(Class<T> cls){
    	String query = String.format("select doc from %s doc join fetch doc.accountingDocumentItems where doc.business.id = :id", cls.getSimpleName());
    	return entityManager.createQuery(query, cls).setParameter("id", getId()).getResultList();
    }
    
    public List<Invoice> fetchInvoicesEagerly(){
    	return fetchAccountingDocsEagerly(Invoice.class);
    }
    
    public List<CreditNote> fetchCreditNotesEagerly(){
    	return fetchAccountingDocsEagerly(CreditNote.class);
    }
    
    public List<Estimation> fetchEstimationsEagerly(){
    	return fetchAccountingDocsEagerly(Estimation.class);
    }
    
    public List<TransportDocument> fetchTransportDocumentsEagerly(){
    	return fetchAccountingDocsEagerly(TransportDocument.class);
    }
    
    public List<Long> getCurrentYearInvoicesDocumentIDs(){
    	String query = "select invoice.documentID from Invoice as invoice where invoice.business.id = :businessId and invoice.accountingDocumentYear = :year order by invoice.documentID";
    	return entityManager.createQuery(query, Long.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", Calendar.getInstance().get(Calendar.YEAR)).getResultList();
    }
    
    public List<Invoice> getInvoiceByIdInYear(Long documentID, Integer year){
    	String query = "select invoice from Invoice invoice where invoice.business.id = :businessId and invoice.accountingDocumentYear = :year and invoice.documentID = :id";
    	return entityManager().createQuery(query, Invoice.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", year)
    			.setParameter("id", documentID).getResultList();
    }
    
    private static interface ClientQueryPreparator{
    	Query prepareQuery(List<String> queryTokens);
    }
    
    @Transient
    private static ClientQueryPreparator fuzzyQueryPreparator = new ClientQueryPreparator() {
		@Override
		public Query prepareQuery(List<String> queryTokens) {
			BooleanQuery luceneQuery = new BooleanQuery();
	    	for(String queryToken: queryTokens){
	    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.NAME, queryToken), 0.7f, 1), Occur.SHOULD);
	    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.ADDRESS, queryToken), 0.7f, 1), Occur.SHOULD);
	    		luceneQuery.add(new TermQuery(new Term(FTSNamespace.POSTCODE, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.CITY, queryToken), 0.8f, 1), Occur.SHOULD);
	    		luceneQuery.add(new TermQuery(new Term(FTSNamespace.PROVINCE, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.EMAIL, queryToken), 0.7f, 1), Occur.SHOULD);
	    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.CONTACT_PREFIX + FTSNamespace.FIRST_NAME, queryToken), 0.7f, 1), Occur.SHOULD);
	    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.CONTACT_PREFIX + FTSNamespace.LAST_NAME, queryToken), 0.7f, 1), Occur.SHOULD);
	    	}
	    	return luceneQuery;
		}
	};
	
	@Transient
	private static ClientQueryPreparator prefixQueryPreparator = new ClientQueryPreparator() {
		@Override
		public Query prepareQuery(List<String> queryTokens) {
			BooleanQuery luceneQuery = new BooleanQuery();
	    	for(String queryToken: queryTokens){
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.NAME, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.ADDRESS, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.POSTCODE, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.CITY, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.PROVINCE, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.EMAIL, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.CONTACT_PREFIX + FTSNamespace.FIRST_NAME, queryToken)), Occur.SHOULD);
	    		luceneQuery.add(new PrefixQuery(new Term(FTSNamespace.CONTACT_PREFIX + FTSNamespace.LAST_NAME, queryToken)), Occur.SHOULD);
	    	}
	    	return luceneQuery;
		}
	};
    
    @SuppressWarnings("unchecked")
	private PageDTO<Client> searchClients(String query, int start, int length, ClientQueryPreparator queryPreparator) throws ParseException, IOException{
    	FullTextEntityManager ftEntityManager = Search.getFullTextEntityManager(entityManager);
    	Analyzer analyzer = ftEntityManager.getSearchFactory().getAnalyzer(FTSNamespace.DEFAULT_CLIENT_ANALYZER);
    	List<String> queryTokens = new ArrayList<String>();
    	TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(query));
    	while(tokenStream.incrementToken())
    		queryTokens.add(tokenStream.getAttribute(CharTermAttribute.class).toString());
    	FullTextQuery ftQuery = ftEntityManager.createFullTextQuery(queryPreparator.prepareQuery(queryTokens), Client.class);
    	ftQuery.enableFullTextFilter(FTSNamespace.CLIENT_BY_BUSINESS_ID_FILTER)
    		.setParameter(TermValueFilterFactory.FIELD_NAME, StringUtils.join(new Object[]{FTSNamespace.BUSINESS, FTSNamespace.ID}, "."))
    		.setParameter(TermValueFilterFactory.FIELD_VALUE, new LongBridge().objectToString(getId()));
    	PageDTO<Client> pageDTO = new PageDTO<Client>(null, start, length, null);
    	pageDTO.setTotal(new Long(ftQuery.getResultSize()));
    	pageDTO.setItems(ftQuery.setFirstResult(start).setMaxResults(length).getResultList());
    	return pageDTO;
    }
    
    public PageDTO<Client> fuzzyClientSearch(String query, int start, int length)  throws ParseException, IOException{
    	return searchClients(query, start, length, fuzzyQueryPreparator);
    }
    
    public PageDTO<Client> prefixClientSearch(String query, int start, int length)  throws ParseException, IOException{
    	return searchClients(query, start, length, prefixQueryPreparator);
    }
    
    private <T extends AccountingDocument> List<T> getAccountingDocumentForYear(Iterator<T> iter, int year){
    	List<T> documents = new LinkedList<T>();
    	T document;
		while(iter.hasNext()){
			document = iter.next();
			if(document.getAccountingDocumentYear().intValue() == year)
				documents.add(document);
		}
		return documents;
    }
    
    @Transactional(readOnly = true)
	public List<Invoice> getInvoicesForYear(int year) {
		return getAccountingDocumentForYear(getInvoices().iterator(), year);
	}
    
    @Transactional(readOnly = true)
    public List<Estimation> getEstimationsForYear(int year){
    	return getAccountingDocumentForYear(getEstimations().iterator(), year);
    }
    
    @Transactional(readOnly = true)
    public List<CreditNote> getCreditNotesForYear(int year){
    	return getAccountingDocumentForYear(getCreditNotes().iterator(), year);
    }
    
    @Transactional(readOnly = true)
    public List<TransportDocument> getTransportDocsForYear(int year){
    	return getAccountingDocumentForYear(getTransportDocuments().iterator(), year);
    }
    
    public Long getNonFreeExpirationDelta(TimeUnit timeUnit){
    	Long now = System.currentTimeMillis();
    	if(nonFreeAccountExpirationTime == null || nonFreeAccountExpirationTime < now)
    		return null;
    	return timeUnit.convert(nonFreeAccountExpirationTime, TimeUnit.MILLISECONDS);
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
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPostcode() {
        return this.postcode;
    }
    
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getProvince() {
        return this.province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(String fax) {
        this.fax = fax;
    }
    
    public String getWeb() {
        return this.web;
    }
    
    public void setWeb(String web) {
        this.web = web;
    }
    
    public String getVatID() {
        return this.vatID;
    }
    
    public void setVatID(String vatID) {
        this.vatID = vatID;
    }
    
    public String getSsn() {
        return this.ssn;
    }
    
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    
	public Long getNonFreeAccountExpirationTime() {
		return nonFreeAccountExpirationTime;
	}

	public void setNonFreeAccountExpirationTime(Long nonFreeAccountExpirationTime) {
		this.nonFreeAccountExpirationTime = nonFreeAccountExpirationTime;
	}

	public Set<Item> getItems() {
        return this.items;
    }
    
    public void setItems(Set<Item> items) {
        this.items = items;
    }
    
    public Set<BankAccount> getAccounts() {
        return this.accounts;
    }
    
    public void setAccounts(Set<BankAccount> accounts) {
        this.accounts = accounts;
    }
    
    public Set<Invoice> getInvoices() {
        return this.invoices;
    }
    
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }
    
    public Set<Estimation> getEstimations() {
        return this.estimations;
    }
    
    public void setEstimations(Set<Estimation> estimations) {
        this.estimations = estimations;
    }
    
    public Set<CreditNote> getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(Set<CreditNote> creditNotes) {
		this.creditNotes = creditNotes;
	}

	public Set<TransportDocument> getTransportDocuments() {
		return transportDocuments;
	}

	public void setTransportDocuments(Set<TransportDocument> transportDocuments) {
		this.transportDocuments = transportDocuments;
	}

	public Set<Client> getClients() {
        return this.clients;
    }
    
    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }
    
    /*
     * End of getters and setters section
     * */
    
    /*
     * Active record functionality
     * */
    
    public Set<Principal> getPrincipals() {
		return principals;
	}

	public void setPrincipals(Set<Principal> principals) {
		this.principals = principals;
	}

	@PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Business().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countBusinesses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Business o", Long.class).getSingleResult();
    }
    
    public static List<Business> findAllBusinesses() {
        return entityManager().createQuery("SELECT o FROM Business o", Business.class).getResultList();
    }
    
    public static Business findBusiness(Long id) {
        if (id == null) return null;
        return entityManager().find(Business.class, id);
    }
    
    public static List<Business> findBusinessEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Business o", Business.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Business attached = Business.findBusiness(this.id);
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
    public Business merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Business merged = this.entityManager.merge(this);
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
    
    @javax.persistence.Version
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
