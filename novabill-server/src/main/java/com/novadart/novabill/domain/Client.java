package com.novadart.novabill.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.utils.fts.TermValueFilterFactory;
import java.io.Serializable;

@Indexed
@AnalyzerDef(name = FTSNamespace.DEFAULT_CLIENT_ANALYZER,
	tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
	filters = {
		@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class)
	})
@Analyzer(definition = FTSNamespace.DEFAULT_CLIENT_ANALYZER)
@FullTextFilterDefs({
	@FullTextFilterDef(name = FTSNamespace.CLIENT_BY_BUSINESS_ID_FILTER, impl = TermValueFilterFactory.class)
})
@Configurable
@Entity
public class Client implements Serializable {
	
	private static final long serialVersionUID = 8383909226336873374L;
	
	@Field(name = FTSNamespace.NAME)
	@Size(max = 255)
    private String name;

	@Field(name = FTSNamespace.ADDRESS)
    @Size(max = 255)
    private String address;

	@Field(name = FTSNamespace.POSTCODE)
    @Size(max = 10)
    private String postcode;

	@Field(name = FTSNamespace.CITY)
    @Size(max = 60)
    private String city;

	@Field(name = FTSNamespace.PROVINCE)
    @Size(max = 2)
    private String province;

	@Field(name = FTSNamespace.COUNTRY)
    @Size(max = 200)
    private String country;

	@Field(name = FTSNamespace.EMAIL)
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
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    private Set<Invoice> invoices = new HashSet<Invoice>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    private Set<Estimation> estimations = new HashSet<Estimation>();
    
    @ManyToOne
    @IndexedEmbedded
    private Business business;
    
    @Transient
    private static final Comparator<AccountingDocument> ACCOUNTING_DOCUMENT_COMPARATOR = new AccountingDocumentComparator();
    
    private <T extends AccountingDocument> List<T> sortAccountingDocuments(Collection<T> collection){
    	SortedSet<T> sortedSet = new TreeSet<T>(ACCOUNTING_DOCUMENT_COMPARATOR);
    	sortedSet.addAll(collection);
    	return new ArrayList<T>(sortedSet);
    }
    
    public List<Invoice> getSortedInvoices(){
    	return sortAccountingDocuments(getInvoices()); 
    }
	
    public List<Estimation> getSortedEstimations(){
    	return sortAccountingDocuments(getEstimations());
    }
    
    public List<Invoice> getAllInvoicesInRange(Integer start, Integer length){
    	String query = "select invoice from Invoice as invoice where invoice.client.id = :clientId order by invoice.invoiceYear desc, invoice.documentID desc";
    	return entityManager.createQuery(query, Invoice.class)
    			.setParameter("clientId", getId())
    			.setFirstResult(start)
    			.setMaxResults(length).getResultList();
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
        EntityManager em = new Client().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countClients() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Client o", Long.class).getSingleResult();
    }
    
    public static List<Client> findAllClients() {
        return entityManager().createQuery("SELECT o FROM Client o", Client.class).getResultList();
    }
    
    public static Client findClient(Long id) {
        if (id == null) return null;
        return entityManager().find(Client.class, id);
    }
    
    public static List<Client> findClientEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Client o", Client.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Client attached = Client.findClient(this.id);
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
    public Client merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Client merged = this.entityManager.merge(this);
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
