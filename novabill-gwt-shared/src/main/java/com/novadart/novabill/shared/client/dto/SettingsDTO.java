package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.data.LayoutType;

import java.math.BigDecimal;

public class SettingsDTO implements IsSerializable {
	
	private LayoutType defaultLayoutType;
	
	private boolean priceDisplayInDocsMonolithic;
	
	private boolean incognitoEnabled;
	
    private String invoiceFooterNote;
    
    private String creditNoteFooterNote;
    
    private String estimationFooterNote;
    
    private String transportDocumentFooterNote;
    
    private String defaultTermsAndConditionsForEstimation;
    
    private String emailSubject;
    
    private String emailText;
    
    private String emailReplyTo;
    
    private Long nonFreeAccountExpirationTime;

	private boolean witholdTax;

	private BigDecimal witholdTaxPercentFirstLevel;

	private BigDecimal witholdTaxPercentSecondLevel;

	private boolean pensionContribution;

	private BigDecimal pensionContributionTax;

	public LayoutType getDefaultLayoutType() {
		return defaultLayoutType;
	}

	public void setDefaultLayoutType(LayoutType defaultLayoutType) {
		this.defaultLayoutType = defaultLayoutType;
	}

	public boolean isPriceDisplayInDocsMonolithic() {
		return priceDisplayInDocsMonolithic;
	}

	public void setPriceDisplayInDocsMonolithic(boolean priceDisplayInDocsMonolithic) {
		this.priceDisplayInDocsMonolithic = priceDisplayInDocsMonolithic;
	}

	public boolean isIncognitoEnabled() {
		return incognitoEnabled;
	}

	public void setIncognitoEnabled(boolean incognitoEnabled) {
		this.incognitoEnabled = incognitoEnabled;
	}

	public String getInvoiceFooterNote() {
		return invoiceFooterNote;
	}

	public void setInvoiceFooterNote(String invoiceFooterNote) {
		this.invoiceFooterNote = invoiceFooterNote;
	}

	public String getCreditNoteFooterNote() {
		return creditNoteFooterNote;
	}

	public void setCreditNoteFooterNote(String creditNoteFooterNote) {
		this.creditNoteFooterNote = creditNoteFooterNote;
	}

	public String getEstimationFooterNote() {
		return estimationFooterNote;
	}

	public void setEstimationFooterNote(String estimationFooterNote) {
		this.estimationFooterNote = estimationFooterNote;
	}

	public String getTransportDocumentFooterNote() {
		return transportDocumentFooterNote;
	}

	public void setTransportDocumentFooterNote(String transportDocumentFooterNote) {
		this.transportDocumentFooterNote = transportDocumentFooterNote;
	}
	
	public String getDefaultTermsAndConditionsForEstimation() {
		return defaultTermsAndConditionsForEstimation;
	}
	
	public void setDefaultTermsAndConditionsForEstimation(
			String defaultTermsAndConditionsForEstimation) {
		this.defaultTermsAndConditionsForEstimation = defaultTermsAndConditionsForEstimation;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailText() {
		return emailText;
	}

	public void setEmailText(String emailText) {
		this.emailText = emailText;
	}

	public String getEmailReplyTo() {
		return emailReplyTo;
	}

	public void setEmailReplyTo(String emailReplyTo) {
		this.emailReplyTo = emailReplyTo;
	}
	
	public Long getNonFreeAccountExpirationTime() {
		return nonFreeAccountExpirationTime;
	}

	public void setNonFreeAccountExpirationTime(Long nonFreeAccountExpirationTime) {
		this.nonFreeAccountExpirationTime = nonFreeAccountExpirationTime;
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

	public void setWitholdTaxPercentSecondLevel(BigDecimal witholdTaxPercentSecondLevel) {
		this.witholdTaxPercentSecondLevel = witholdTaxPercentSecondLevel;
	}

	public boolean isPensionContribution() {
		return pensionContribution;
	}

	public void setPensionContribution(boolean pensionContribution) {
		this.pensionContribution = pensionContribution;
	}

	public BigDecimal getPensionContributionTax() {
		return pensionContributionTax;
	}

	public void setPensionContributionTax(BigDecimal pensionContributionTax) {
		this.pensionContributionTax = pensionContributionTax;
	}
}
