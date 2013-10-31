package com.novadart.novabill.frontend.client.bridge.server.autobean;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface AutoBeanMaker extends AutoBeanFactory {
	public static AutoBeanMaker INSTANCE = GWT.create(AutoBeanMaker.class);

	AutoBean<Business> makeBusiness();
	
	AutoBean<Contact> makeContact();
	
	AutoBean<Client> makeClient();
	
	AutoBean<ClientsList> makeClientsList();
	
	AutoBean<Page<Client>> makeClientPage();
	
	AutoBean<Invoice> makeInvoice();
	
	AutoBean<InvoicesList> makeInvoicesList();
	
	AutoBean<AccountingDocumentItem> makeAccountingDocumentItem();
	
}
