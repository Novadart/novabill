package com.novadart.novabill.domain;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.bridge.builtin.LongBridge;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.Hash;
import com.novadart.novabill.domain.security.RoleTypes;
import com.novadart.novabill.shared.client.validation.RegularExpressionConstants;
import com.novadart.utils.fts.TermValueFilterFactory;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@Configurable
@Entity
public class Business implements Serializable {

	private static final long serialVersionUID = 261999997691744944L;
	
	public Business(){}
	
	public Business(Registration registration){
		email = registration.getEmail();
		password = registration.getPassword();
		creationTime = registration.getCreationTime();
	}

	@Size(max = 255)
	@NotBlank
    private String name;

    @Size(max = 255)
    @NotBlank
    private String address;

    @Size(max = 10)
    @NotBlank
    private String postcode;

    @Size(max = 60)
    @NotBlank
    private String city;

    @Size(max = 2)
    @NotBlank
    private String province;

    @Size(max = 200)
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

    private String password;
    
    private Long creationTime = System.currentTimeMillis();
    
    @Column(precision = 29, scale = 0)
    private BigInteger logoId;
    
    private Long lastLogin;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Item> items = new HashSet<Item>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<BankAccount> accounts = new HashSet<BankAccount>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Invoice> invoices = new HashSet<Invoice>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Estimation> estimations = new HashSet<Estimation>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<Client> clients = new HashSet<Client>();

    @ElementCollection
    private Set<RoleTypes> grantedRoles = new HashSet<RoleTypes>();
    
    public List<Invoice> getAllInvoicesInRange(int start, int length){
    	String query = "select invoice from Invoice invoice where invoice.business.id = :id order by invoice.accountingDocumentYear desc, invoice.documentID desc";
    	return entityManager().createQuery(query, Invoice.class).setParameter("id", getId()).setFirstResult(start).setMaxResults(length).getResultList();
    }
    
    public Long getNextInvoiceDocumentID(){
    	String query = "select max(invoice.documentID) from Invoice as invoice where invoice.business.id = :businessId and invoice.accountingDocumentYear = :year";
    	Long id = entityManager.createQuery(query, Long.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", Calendar.getInstance().get(Calendar.YEAR)).getSingleResult(); 
    	return (id == null)? 1: id + 1;
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
    
    public Long getNextEstimationDocumentID(){
    	String query = "select max(estimation.documentID) from Estimation as estimation where estimation.business.id = :businessId and estimation.accountingDocumentYear = :year";
    	Long id = entityManager.createQuery(query, Long.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", Calendar.getInstance().get(Calendar.YEAR)).getSingleResult();
    	return (id == null)? 1: id + 1;
    }
    
    @SuppressWarnings("unchecked")
	public List<Client> searchClients(String query) throws ParseException, IOException{
    	FullTextEntityManager ftEntityManager = Search.getFullTextEntityManager(entityManager);
    	Analyzer analyzer = ftEntityManager.getSearchFactory().getAnalyzer(FTSNamespace.DEFAULT_CLIENT_ANALYZER);
    	List<String> queryTokens = new ArrayList<String>();
    	TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(query));
    	while(tokenStream.incrementToken())
    		queryTokens.add(tokenStream.getAttribute(CharTermAttribute.class).toString());
    	BooleanQuery luceneQuery = new BooleanQuery();
    	for(String queryToken: queryTokens){
    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.NAME, queryToken), 0.7f, 1), Occur.SHOULD);
    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.ADDRESS, queryToken), 0.7f, 1), Occur.SHOULD);
    		luceneQuery.add(new TermQuery(new Term(FTSNamespace.POSTCODE, queryToken)), Occur.SHOULD);
    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.CITY, queryToken), 0.8f, 1), Occur.SHOULD);
    		luceneQuery.add(new TermQuery(new Term(FTSNamespace.PROVINCE, queryToken)), Occur.SHOULD);
    		luceneQuery.add(new FuzzyQuery(new Term(FTSNamespace.EMAIL, queryToken), 0.7f, 1), Occur.SHOULD);
    	}
    	FullTextQuery ftQuery = ftEntityManager.createFullTextQuery(luceneQuery, Client.class);
    	ftQuery.enableFullTextFilter(FTSNamespace.CLIENT_BY_BUSINESS_ID_FILTER)
    		.setParameter(TermValueFilterFactory.FIELD_NAME, StringUtils.join(new Object[]{FTSNamespace.BUSINESS, FTSNamespace.ID}, "."))
    		.setParameter(TermValueFilterFactory.FIELD_VALUE, new LongBridge().objectToString(getId()));
    	return ftQuery.getResultList();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationTime == null) ? 0 : creationTime.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Business other = (Business) obj;
		if (creationTime == null) {
			if (other.creationTime != null)
				return false;
		} else if (!creationTime.equals(other.creationTime))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
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
    
    public String getPassword() {
        return this.password;
    }
    
    @Hash(saltMethod = "getCreationTime")
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Long getCreationTime() {
        return this.creationTime;
    }
    
    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }
    
    public BigInteger getLogoId() {
        return this.logoId;
    }
    
    public void setLogoId(BigInteger logoId) {
        this.logoId = logoId;
    }
    
    public Long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
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
    
    public Set<Client> getClients() {
        return this.clients;
    }
    
    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }
    
    public Set<RoleTypes> getGrantedRoles() {
        return this.grantedRoles;
    }
    
    public void setGrantedRoles(Set<RoleTypes> grantedRoles) {
        this.grantedRoles = grantedRoles;
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
