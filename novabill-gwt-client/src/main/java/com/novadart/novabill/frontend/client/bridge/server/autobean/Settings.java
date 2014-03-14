package com.novadart.novabill.frontend.client.bridge.server.autobean;

import com.novadart.novabill.shared.client.data.LayoutType;



public interface Settings {

	public LayoutType getDefaultLayoutType();

	public void setDefaultLayoutType(LayoutType defaultLayoutType);

	public boolean isPriceDisplayInDocsMonolithic();

	public void setPriceDisplayInDocsMonolithic(boolean priceDisplayInDocsMonolithic);

	public boolean isIncognitoEnabled();

	public void setIncognitoEnabled(boolean incognitoEnabled);
	
	public String getInvoiceFooterNote();

	public void setInvoiceFooterNote(String invoiceFooterNote);

	public String getCreditNoteFooterNote();

	public void setCreditNoteFooterNote(String creditNoteFooterNote);

	public String getEstimationFooterNote();

	public void setEstimationFooterNote(String estimationFooterNote);

	public String getTransportDocumentFooterNote();

	public void setTransportDocumentFooterNote(String transportDocumentFooterNote);

}
