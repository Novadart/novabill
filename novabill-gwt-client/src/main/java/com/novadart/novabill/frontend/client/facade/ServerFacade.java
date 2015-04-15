package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.facade.*;

public interface ServerFacade {
	
	ServerFacade INSTANCE = GWT.create(ServerFacade.class);

	void setupXsrfProtection(final AsyncCallback<Void> callback);

	void sendFeedback(String subject, String name,
			String email, String message, String category,
			final AsyncCallback<Boolean> callback);

	void deleteLogo(final AsyncCallback<Boolean> callback);

	InvoiceGwtServiceAsync getInvoiceService();

	ClientGwtServiceAsync getClientService();

	BusinessGwtServiceAsync getBusinessService();

	EstimationGwtServiceAsync getEstimationService();

	CreditNoteGwtServiceAsync getCreditNoteService();

	TransportDocumentGwtServiceAsync getTransportdocumentService();

	PaymentTypeGwtServiceAsync getPaymentService();

	BatchDataFetcherGwtServiceAsync getBatchfetcherService();
	
	CommodityGwtServiceAsync getCommodityGwtService();
	
	PriceListGwtServiceAsync getPriceListGwtService();

	DocumentIDClassGwtServiceAsync getDocumentIdClassGwtService();

}