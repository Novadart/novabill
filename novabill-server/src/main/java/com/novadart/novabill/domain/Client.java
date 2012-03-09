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
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.validator.constraints.Email;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import com.novadart.utils.fts.TermValueFilterFactory;
import java.io.Serializable;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
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
    private static final Comparator<AbstractInvoice> ABSTRACT_INVOICE_COMPARATOR = new AbstractInvoiceComparator();
    
    private <T extends AbstractInvoice> List<T> sortAbstractInvoices(Collection<T> collection){
    	SortedSet<T> sortedSet = new TreeSet<T>(ABSTRACT_INVOICE_COMPARATOR);
    	sortedSet.addAll(collection);
    	return new ArrayList<T>(sortedSet);
    }
    
    public List<Invoice> getSortedInvoices(){
    	return sortAbstractInvoices(getInvoices()); 
    }
	
    public List<Estimation> getSortedEstimations(){
    	return sortAbstractInvoices(getEstimations());
    }
    
    public List<Invoice> getAllInvoicesInRange(Integer start, Integer length){
    	String query = "select invoice from Invoice as invoice where invoice.client.id = :clientId order by invoice.invoiceYear desc, invoice.invoiceID desc";
    	return entityManager.createQuery(query, Invoice.class)
    			.setParameter("clientId", getId())
    			.setFirstResult(start)
    			.setMaxResults(length).getResultList();
    }
    
}
