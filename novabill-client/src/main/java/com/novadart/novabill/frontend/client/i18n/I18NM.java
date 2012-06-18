package com.novadart.novabill.frontend.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface I18NM extends Messages {
	public static final I18NM get = GWT.create(I18NM.class);
	
	public String totalInvoices(int n);
	public String totalClients(int n);
	public String totalAmount(String n);
	public String invalidDocumentIdError(String values);
	public String invalidDocumentIdErrorMultiple(String values);
}
