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
	
	public String getDefaultTermsAndConditionsForEstimation();
	
	public void setDefaultTermsAndConditionsForEstimation(String tac);
	
	public String getEmailSubject();

	public void setEmailSubject(String emailSubject);

	public String getEmailText();

	public void setEmailText(String emailText);

	public String getEmailReplyTo();

	public void setEmailReplyTo(String emailReplyTo);

}
