package com.novadart.novabill.domain;

import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.novadart.novabill.shared.client.data.LayoutType;

@Embeddable
public class Settings {

	@NotNull
    private LayoutType defaultLayoutType;
	
	@Column(columnDefinition = "boolean default true")
    private boolean priceDisplayInDocsMonolithic = true;
    
    @Column(columnDefinition = "boolean default false")
    private boolean incognitoEnabled = false;
    
    private Long nonFreeAccountExpirationTime;
    
    @Size(max = 255)
    private String invoiceFooterNote;
    
    @Size(max = 255)
    private String creditNoteFooterNote;
    
    @Size(max = 255)
    private String estimationFooterNote;
    
    @Size(max = 255)
    private String transportDocumentFooterNote;
    
    public Long getNonFreeExpirationDelta(TimeUnit timeUnit){
    	Long now = System.currentTimeMillis();
    	if(nonFreeAccountExpirationTime == null || nonFreeAccountExpirationTime < now)
    		return null;
    	return timeUnit.convert(nonFreeAccountExpirationTime, TimeUnit.MILLISECONDS);
    }
    
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

	public Long getNonFreeAccountExpirationTime() {
		return nonFreeAccountExpirationTime;
	}

	public void setNonFreeAccountExpirationTime(Long nonFreeAccountExpirationTime) {
		this.nonFreeAccountExpirationTime = nonFreeAccountExpirationTime;
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
	
}
