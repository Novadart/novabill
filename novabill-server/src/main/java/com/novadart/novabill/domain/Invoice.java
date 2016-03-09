package com.novadart.novabill.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.novadart.novabill.shared.client.dto.MailDeliveryStatus;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
import com.novadart.novabill.shared.client.tuple.Triple;


/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
@javax.persistence.Table(name = "invoice")
@Table(appliesTo = "invoice",
	indexes = @Index(columnNames = "business", name = "invoice_business_fkey_index"))
public class Invoice extends AbstractInvoice implements Serializable {
	
	private static final long serialVersionUID = 3369941491294470750L;
	
	@Size(max = 255)
	@NotNull
	private String paymentTypeName;

	@NotNull
	private PaymentDateType paymentDateGenerator;

	private Integer paymentDateDelta;
	
    private PaymentDeltaType paymentDeltaType;
	
	private Integer secondaryPaymentDateDelta;
	
	@Column(columnDefinition = "boolean default false")
	private boolean createdFromTransportDocuments = false;
	
	private Long seenByClientTime;

	private MailDeliveryStatus emailedToClient = MailDeliveryStatus.NOT_SENT;

	@Column(columnDefinition = "boolean default false")
	private boolean splitPayment = false;

	private boolean witholdTax;

	private BigDecimal witholdTaxPercentFirstLevel;

	private BigDecimal witholdTaxPercentSecondLevel;

	private boolean pensionContribution;

	private BigDecimal pensionContributionPercent;

	private BigDecimal witholdTaxTotal;

	private BigDecimal pensionContributionTotal;

    @ManyToOne
    @JoinColumn(name = "business")
    protected Business business;

    @ManyToOne
    protected Client client;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "invoice")
    private Set<TransportDocument> transportDocuments = new HashSet<TransportDocument>();
    
    public static Long countInvocesForClient(Long id){
    	return countForClient(Invoice.class, id);
    }
    
    public static Map<String, BigDecimal> getCommodityRevenueDistrbutionForYear(Long businessID, Integer year){
		String sql = "select i.sku, sum(i.totalBeforeTax) from Invoice inv join inv.accountingDocumentItems i " +
					 "where i.sku <> '' and inv.business.id = :bid and inv.accountingDocumentYear = :year group by i.sku";
		List<Object[]> rows = entityManager().createQuery(sql, Object[].class).
								setParameter("bid", businessID).
								setParameter("year", year).getResultList();
		Map<String, BigDecimal> commodityRevenue = new HashMap<>(rows.size());
		for(Object[] row: rows)
			commodityRevenue.put((String) row[0], (BigDecimal) row[1]);
		return commodityRevenue;
    }
    
    /**
     * Returns all invoices for client ordered by accountingDocumentDate 
     */
    public static List<Invoice> getAllInvoicesForClient(Long businessID, Long clientID){
    	String sql = "select i from Invoice i where i.business.id = :bid and i.client.id = :cid order by i.accountingDocumentDate";
    	return entityManager().createQuery(sql, Invoice.class).
    			setParameter("bid", businessID).
    			setParameter("cid", clientID).
    			getResultList();
    }
    
    
    /**
     * Returns list of triples where each triple is comprised of
     * 1st - sku of commodity
     * 2nd - total before taxes invoiced for this commodity type for the given year
     * 3rd - total quantity invoiced for this commodity type for the given year
     * The list is ordered on the second argument - total before taxes
     */
    public static List<Triple<String, BigDecimal, BigDecimal>> getCommodityRevenueStatsForClientForYear(Long businessID, Long clientID, Integer year){
		String sql = "select i.sku, sum(i.totalBeforeTax) as rev, sum(i.quantity) from Invoice inv join inv.accountingDocumentItems i " +
					 "where i.sku <> '' and inv.business.id = :bid and inv.client.id = :cid and inv.accountingDocumentYear = :year group by i.sku order by rev desc";
		List<Object[]> rows = entityManager().createQuery(sql, Object[].class).
								setParameter("bid", businessID).
								setParameter("cid", clientID).
								setParameter("year", year).getResultList();
		List<Triple<String, BigDecimal, BigDecimal>> commodityRevenue = new ArrayList<>(rows.size());
		for(Object[] row: rows)
			commodityRevenue.add(new Triple<>((String)row[0], (BigDecimal)row[1], (BigDecimal)row[2]));
		return commodityRevenue;
    }
    
    /**
     * Returns all invoices for given years that contain given commodity identified by sku. Invoices are sorted by creation date. 
     */
    public static List<Invoice> getAllInvoicesContainingCommodityForYears(Long businessID, String sku, List<Integer> years){
		String sql = "select inv from Invoice inv join inv.accountingDocumentItems i " +
				"where i.sku = :sku and inv.business.id = :bid and inv.accountingDocumentYear in (:years) order by inv.accountingDocumentDate";
		return entityManager().createQuery(sql, Invoice.class).
				setParameter("bid", businessID).
				setParameter("sku", sku).
				setParameter("years", years).getResultList();
    }
    
    /**
     * Returns the total before taxes for given client 
     */
    public static BigDecimal getTotalBeforeTaxesForClient(Long businessID, Long clientID) {
    	String sql = "select sum(i.totalBeforeTax) from Invoice i where i.business.id = :bid and i.client.id = :cid";
    	return entityManager().createQuery(sql, BigDecimal.class).
    			setParameter("bid", businessID).
    			setParameter("cid", clientID).getSingleResult();
    }

	/**
	 * Returns the total before taxes for given client for given year
	 */
	public static BigDecimal getTotalBeforeTaxesForClientForYear(Long businessID, Long clientID, Integer year) {
		String sql = "select sum(i.totalBeforeTax) from Invoice i where i.business.id = :bid and i.client.id = :cid and i.accountingDocumentYear = :year";
		return entityManager().createQuery(sql, BigDecimal.class).
				setParameter("bid", businessID).
				setParameter("cid", clientID).
				setParameter("year", year).getSingleResult();
	}

    
    /*
     * Getters and setters
     * */
    
    public PaymentDateType getPaymentDateGenerator() {
		return paymentDateGenerator;
	}

	public void setPaymentDateGenerator(PaymentDateType paymentDateGenerator) {
		this.paymentDateGenerator = paymentDateGenerator;
	}

	public Integer getPaymentDateDelta() {
		return paymentDateDelta;
	}

	public void setPaymentDateDelta(Integer paymentDateDelta) {
		this.paymentDateDelta = paymentDateDelta;
	}
	
	public PaymentDeltaType getPaymentDeltaType() {
		return paymentDeltaType;
	}

	public void setPaymentDeltaType(PaymentDeltaType paymentDeltaType) {
		this.paymentDeltaType = paymentDeltaType;
	}

	public Integer getSecondaryPaymentDateDelta() {
		return secondaryPaymentDateDelta;
	}

	public void setSecondaryPaymentDateDelta(Integer secondaryPaymentDateDelta) {
		this.secondaryPaymentDateDelta = secondaryPaymentDateDelta;
	}

	public boolean isCreatedFromTransportDocuments() {
		return createdFromTransportDocuments;
	}

	public void setCreatedFromTransportDocuments(
			boolean createdFromTransportDocuments) {
		this.createdFromTransportDocuments = createdFromTransportDocuments;
	}

	public Long getSeenByClientTime() {
		return seenByClientTime;
	}

	public void setSeenByClientTime(Long seenByClientTime) {
		this.seenByClientTime = seenByClientTime;
	}

	public MailDeliveryStatus getEmailedToClient() {
		return emailedToClient;
	}

	public void setEmailedToClient(MailDeliveryStatus emailedToClient) {
		this.emailedToClient = emailedToClient;
	}

	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	public boolean isSplitPayment() {
		return splitPayment;
	}

	public void setSplitPayment(boolean splitPayment) {
		this.splitPayment = splitPayment;
	}

	public boolean isWitholdTax() {
		return witholdTax;
	}

	public void setWitholdTax(boolean witholdTax) {
		this.witholdTax = witholdTax;
	}

	public BigDecimal getWitholdTaxPercentFirstLevel() {
		return witholdTaxPercentFirstLevel;
	}

	public void setWitholdTaxPercentFirstLevel(BigDecimal witholdTaxPercentFirstLevel) {
		this.witholdTaxPercentFirstLevel = witholdTaxPercentFirstLevel;
	}

	public BigDecimal getWitholdTaxPercentSecondLevel() {
		return witholdTaxPercentSecondLevel;
	}

	public void setWitholdTaxPercentSecondLevel(BigDecimal witholdTaxRate) {
		this.witholdTaxPercentSecondLevel = witholdTaxRate;
	}

	public boolean isPensionContribution() {
		return pensionContribution;
	}

	public void setPensionContribution(boolean pensionContribution) {
		this.pensionContribution = pensionContribution;
	}

	public BigDecimal getPensionContributionPercent() {
		return pensionContributionPercent;
	}

	public void setPensionContributionPercent(BigDecimal pensionContribution) {
		this.pensionContributionPercent = pensionContribution;
	}

	public BigDecimal getWitholdTaxTotal() {
		return witholdTaxTotal;
	}

	public void setWitholdTaxTotal(BigDecimal witholdTaxRateTotal) {
		this.witholdTaxTotal = witholdTaxRateTotal;
	}

	public BigDecimal getPensionContributionTotal() {
		return pensionContributionTotal;
	}

	public void setPensionContributionTotal(BigDecimal pensionContributionTotal) {
		this.pensionContributionTotal = pensionContributionTotal;
	}

	public Business getBusiness() {
        return this.business;
    }
    
    public void setBusiness(Business business) {
        this.business = business;
    }
    
    @Override
    public Client getClient() {
        return this.client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public Set<TransportDocument> getTransportDocuments() {
		return transportDocuments;
	}

	public void setTransportDocuments(Set<TransportDocument> transportDocuments) {
		this.transportDocuments = transportDocuments;
	}
    
    /*
     * End of getters and setters section
     * */
    
    /*
     * Active record functionality
     * */
    
	public static long countInvoices() {
        return count(Invoice.class);
    }
    
    public static List<Invoice> findAllInvoices() {
        return findAll(Invoice.class);
    }
    
    public static Invoice findInvoice(Long id) {
        return find(Invoice.class, id);
    }
    
    public static List<Invoice> findInvoiceEntries(int firstResult, int maxResults) {
        return findInRange(Invoice.class, firstResult, maxResults);
    }
    
    @Transactional
    public Invoice merge() {
        return merge(this);
    }

    /*
     * End of active record functionality section
     * */
    
}
