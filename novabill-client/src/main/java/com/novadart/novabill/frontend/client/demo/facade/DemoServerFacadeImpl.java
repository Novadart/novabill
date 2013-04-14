package com.novadart.novabill.frontend.client.demo.facade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoBatchDataFetcherService;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoBusinessService;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoClientService;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoCreditNoteService;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoEstimationService;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoInvoiceService;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoPaymentTypeService;
import com.novadart.novabill.frontend.client.demo.facade.service.DemoTransportDocumentService;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherServiceAsync;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;
import com.novadart.novabill.shared.client.facade.CreditNoteServiceAsync;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;
import com.novadart.novabill.shared.client.facade.PaymentTypeServiceAsync;
import com.novadart.novabill.shared.client.facade.TransportDocumentServiceAsync;

public class DemoServerFacadeImpl implements ServerFacade {
	
	private static final InvoiceServiceAsync INVOICE_SERVICE = new DemoInvoiceService();
	
	private static final ClientServiceAsync CLIENT_SERVICE = new DemoClientService();
	
	private static final EstimationServiceAsync ESTIMATION_SERVICE = new DemoEstimationService();
	
	private static final BusinessServiceAsync BUSINESS_SERVICE = new DemoBusinessService();
	
	private static final TransportDocumentServiceAsync TRANSPORT_DOCUMENT_SERVICE = new DemoTransportDocumentService();
	
	private static final CreditNoteServiceAsync CREDIT_NOTE_SERVICE = new DemoCreditNoteService();
	
	private static final PaymentTypeServiceAsync PAYMENT_TYPE_SERVICE = new DemoPaymentTypeService();

	private static final BatchDataFetcherServiceAsync BATCH_DATA_FETCHER_SERVICE = new DemoBatchDataFetcherService();
	
	@Override
	public void setupXsrfProtection(AsyncCallback<Void> callback) {
		callback.onSuccess(null);
	}

	@Override
	public void sendFeedback(String subject, String name, String email,
			String message, String category, AsyncCallback<Boolean> callback) {
		callback.onSuccess(Boolean.TRUE);
	}

	@Override
	public void deleteLogo(AsyncCallback<Boolean> callback) {
		callback.onSuccess(Boolean.TRUE);
	}

	@Override
	public InvoiceServiceAsync getInvoiceService() {
		return INVOICE_SERVICE;
	}

	@Override
	public ClientServiceAsync getClientService() {
		return CLIENT_SERVICE;
	}

	@Override
	public BusinessServiceAsync getBusinessService() {
		return BUSINESS_SERVICE;
	}

	@Override
	public EstimationServiceAsync getEstimationService() {
		return ESTIMATION_SERVICE;
	}

	@Override
	public CreditNoteServiceAsync getCreditnoteService() {
		return CREDIT_NOTE_SERVICE;
	}

	@Override
	public TransportDocumentServiceAsync getTransportdocumentService() {
		return TRANSPORT_DOCUMENT_SERVICE;
	}

	@Override
	public PaymentTypeServiceAsync getPaymentService() {
		return PAYMENT_TYPE_SERVICE;
	}

	@Override
	public BatchDataFetcherServiceAsync getBatchfetcherService() {
		return BATCH_DATA_FETCHER_SERVICE;
	}

}
