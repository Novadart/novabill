package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.data.LayoutType;

public class SettingsDTO implements IsSerializable {
	
	private LayoutType defaultLayoutType;
	
	private boolean priceDisplayInDocsMonolithic;
	
	private boolean incognitoEnabled;
	
    private String invoiceFooterNote;
    
    private String creditNoteFooterNote;
    
    private String estimationFooterNote;
    
    private String transportDocumentFooterNote;

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
	
}
