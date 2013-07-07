package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherServiceAsync;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;
import com.novadart.novabill.shared.client.facade.CreditNoteServiceAsync;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;
import com.novadart.novabill.shared.client.facade.PaymentTypeServiceAsync;
import com.novadart.novabill.shared.client.facade.TransportDocumentServiceAsync;

public interface ServerFacade {
	
	public static final ServerFacade INSTANCE = GWT.create(ServerFacade.class);

	public void setupXsrfProtection(final AsyncCallback<Void> callback);

	public void sendFeedback(String subject, String name,
			String email, String message, String category,
			final AsyncCallback<Boolean> callback);

	public void deleteLogo(final AsyncCallback<Boolean> callback);

	public InvoiceServiceAsync getInvoiceService();

	public ClientServiceAsync getClientService();

	public BusinessServiceAsync getBusinessService();

	public EstimationServiceAsync getEstimationService();

	public CreditNoteServiceAsync getCreditnoteService();

	public TransportDocumentServiceAsync getTransportdocumentService();

	public PaymentTypeServiceAsync getPaymentService();

	public BatchDataFetcherServiceAsync getBatchfetcherService();

}