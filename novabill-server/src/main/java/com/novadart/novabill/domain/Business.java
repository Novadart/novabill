package com.novadart.novabill.domain;

import com.novadart.novabill.domain.security.RoleTypes;
import com.novadart.utils.fts.TermValueFilterFactory;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.search.bridge.builtin.LongBridge;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.validator.constraints.Email;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class Business implements Serializable {

	private static final long serialVersionUID = 261999997691744944L;

	@Size(max = 255)
    private String name;

    @Size(max = 255)
    private String address;

    @Size(max = 10)
    private String postcode;

    @Size(max = 60)
    private String city;

    @Size(max = 2)
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
    private String vatID;

    @Size(max = 25)
    private String ssn;

    private String password;
    
    private Long creationTime;
    
    @Column(precision = 29, scale = 0)
    private BigInteger logoId;
    
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
    
    @PostPersist
    protected void onCreate(){
    	creationTime = System.currentTimeMillis();
    }
    
    public List<Invoice> getAllInvoicesInRange(int start, int length){
    	String query = "select invoice from Invoice invoice where invoice.business.id = :id order by invoice.invoiceYear desc, invoice.invoiceID desc";
    	return entityManager().createQuery(query, Invoice.class).setParameter("id", getId()).setFirstResult(start).setMaxResults(length).getResultList();
    }
    
    public Long getNextInvoiceId(){
    	String query = "select max(invoice.invoiceID) from Invoice as invoice where invoice.business.id = :businessId and invoice.invoiceYear = :year";
    	Long id = entityManager.createQuery(query, Long.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", Calendar.getInstance().get(Calendar.YEAR)).getSingleResult(); 
    	return (id == null)? 1: id + 1;
    }
    
    public List<Long> getCurrentYearInvoicesIds(){
    	String query = "select invoice.invoiceID from Invoice as invoice where invoice.business.id = :businessId and invoice.invoiceYear = :year order by invoice.invoiceID";
    	return entityManager.createQuery(query, Long.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", Calendar.getInstance().get(Calendar.YEAR)).getResultList();
    }
    
    public Invoice getInvoiceByIdInYear(Long invoiceId, Integer year){
    	String query = "select invoice from Invoice invoice where invoice.business.id = :businessId and invoice.invoiceYear = :year and invoice.invoiceID = :id";
    	List<Invoice> result =  entityManager().createQuery(query, Invoice.class)
    			.setParameter("businessId", getId())
    			.setParameter("year", year)
    			.setParameter("id", invoiceId).getResultList();
    	return result.size() == 0? null: result.get(0);
    }
    
    @SuppressWarnings("unchecked")
	public List<Client> searchClients(String query) throws ParseException{
    	FullTextEntityManager ftEntityManager = Search.getFullTextEntityManager(entityManager);
    	Analyzer analyzer = ftEntityManager.getSearchFactory().getAnalyzer(FTSNamespace.DEFAULT_CLIENT_ANALYZER);
    	MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_32, new String[]{FTSNamespace.NAME, FTSNamespace.ADDRESS, FTSNamespace.POSTCODE, FTSNamespace.CITY,
    			FTSNamespace.PROVINCE, FTSNamespace.COUNTRY, FTSNamespace.EMAIL}, analyzer);
    	org.apache.lucene.search.Query luceneQuery = null;
		luceneQuery = parser.parse(query);
    	FullTextQuery ftQuery = ftEntityManager.createFullTextQuery(luceneQuery, Client.class);
    	ftQuery.enableFullTextFilter(FTSNamespace.CLIENT_BY_BUSINESS_ID_FILTER)
    		.setParameter(TermValueFilterFactory.FIELD_NAME, StringUtils.join(new Object[]{FTSNamespace.BUSINESS, FTSNamespace.ID}, "."))
    		.setParameter(TermValueFilterFactory.FIELD_VALUE, new LongBridge().objectToString(getId()));
    	return ftQuery.getResultList();
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
    
}
