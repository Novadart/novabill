package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvoiceDTO extends AbstractInvoiceDTO implements IsSerializable {
	
	private String paymentTypeName;
    
    private PaymentDateType paymentDateGenerator;
    
    private Integer paymentDateDelta;
    
    private PaymentDeltaType paymentDeltaType;
	
	private Integer secondaryPaymentDateDelta;
    
	private boolean createdFromTransportDocuments;
	
	private Long seenByClientTime;
	
	private MailDeliveryStatus emailedToClient;

	private boolean splitPayment;

	private boolean witholdTax;

	private BigDecimal witholdTaxPercent;

	private boolean pensionContribution;

	private BigDecimal pensionContributionPercent;

	private BigDecimal witholdTaxTotal;

	private BigDecimal pensionContributionTotal;
	
    private List<Long> transportDocumentIDs;
    
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

	
	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}


	public List<Long> getTransportDocumentIDs() {
		return transportDocumentIDs;
	}


	public void setTransportDocumentIDs(List<Long> transportDocumentIDs) {
		this.transportDocumentIDs = transportDocumentIDs;
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

	public BigDecimal getWitholdTaxPercent() {
		return witholdTaxPercent;
	}

	public void setWitholdTaxPercent(BigDecimal witholdTaxPercent) {
		this.witholdTaxPercent = witholdTaxPercent;
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

	public void setPensionContributionPercent(BigDecimal pensionContributionPercent) {
		this.pensionContributionPercent = pensionContributionPercent;
	}

	public BigDecimal getWitholdTaxTotal() {
		return witholdTaxTotal;
	}

	public void setWitholdTaxTotal(BigDecimal witholdTaxTotal) {
		this.witholdTaxTotal = witholdTaxTotal;
	}

	public BigDecimal getPensionContributionTotal() {
		return pensionContributionTotal;
	}

	public void setPensionContributionTotal(BigDecimal pensionContributionTotal) {
		this.pensionContributionTotal = pensionContributionTotal;
	}
}
