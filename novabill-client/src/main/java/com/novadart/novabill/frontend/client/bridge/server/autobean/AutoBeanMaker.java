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
	
	AutoBean<Page<Invoice>> makeInvoicePage();
	
	AutoBean<InvoicesList> makeInvoicesList();
	
	AutoBean<Estimation> makeEstimation();
	
	AutoBean<Page<Estimation>> makeEstimationPage();
	
	AutoBean<EstimationList> makeEstimationList();

	AutoBean<CreditNote> makeCreditNote();
	
	AutoBean<Page<CreditNote>> makeCreditNotePage();
	
	AutoBean<CreditNoteList> makeCreditNotesList();
	
	AutoBean<EndPoint> makeEndPoint();
	
	AutoBean<TransportDocument> makeTransportDocument();
	
	AutoBean<Page<TransportDocument>> makeTransportDocumentPage();
	
	AutoBean<TransportDocumentList> makeTransportDocumentList();
	
	AutoBean<AccountingDocumentItem> makeAccountingDocumentItem();
	
}
