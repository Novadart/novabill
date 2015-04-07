package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.facade.*;

public interface ServerFacade {
	
	public static final ServerFacade INSTANCE = GWT.create(ServerFacade.class);

	public void setupXsrfProtection(final AsyncCallback<Void> callback);

	public void sendFeedback(String subject, String name,
			String email, String message, String category,
			final AsyncCallback<Boolean> callback);

	public void deleteLogo(final AsyncCallback<Boolean> callback);

	public InvoiceGwtServiceAsync getInvoiceService();

	public ClientGwtServiceAsync getClientService();

	public BusinessGwtServiceAsync getBusinessService();

	public EstimationGwtServiceAsync getEstimationService();

	public CreditNoteGwtServiceAsync getCreditNoteService();

	public TransportDocumentGwtServiceAsync getTransportdocumentService();

	public PaymentTypeGwtServiceAsync getPaymentService();

	public BatchDataFetcherGwtServiceAsync getBatchfetcherService();
	
	public CommodityGwtServiceAsync getCommodityGwtService();
	
	public PriceListGwtServiceAsync getPriceListGwtService();

}