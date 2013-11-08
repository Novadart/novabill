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
import com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.BusinessGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.ClientGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.CreditNoteGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.EstimationGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.InvoiceGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.PaymentTypeGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.TransportDocumentGwtServiceAsync;

public class DemoServerFacadeImpl implements ServerFacade {
	
	private static final InvoiceGwtServiceAsync INVOICE_SERVICE = new DemoInvoiceService();
	
	private static final ClientGwtServiceAsync CLIENT_SERVICE = new DemoClientService();
	
	private static final EstimationGwtServiceAsync ESTIMATION_SERVICE = new DemoEstimationService();
	
	private static final BusinessGwtServiceAsync BUSINESS_SERVICE = new DemoBusinessService();
	
	private static final TransportDocumentGwtServiceAsync TRANSPORT_DOCUMENT_SERVICE = new DemoTransportDocumentService();
	
	private static final CreditNoteGwtServiceAsync CREDIT_NOTE_SERVICE = new DemoCreditNoteService();
	
	private static final PaymentTypeGwtServiceAsync PAYMENT_TYPE_SERVICE = new DemoPaymentTypeService();

	private static final BatchDataFetcherGwtServiceAsync BATCH_DATA_FETCHER_SERVICE = new DemoBatchDataFetcherService();
	
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
	public InvoiceGwtServiceAsync getInvoiceService() {
		return INVOICE_SERVICE;
	}

	@Override
	public ClientGwtServiceAsync getClientService() {
		return CLIENT_SERVICE;
	}

	@Override
	public BusinessGwtServiceAsync getBusinessService() {
		return BUSINESS_SERVICE;
	}

	@Override
	public EstimationGwtServiceAsync getEstimationService() {
		return ESTIMATION_SERVICE;
	}

	@Override
	public CreditNoteGwtServiceAsync getCreditNoteService() {
		return CREDIT_NOTE_SERVICE;
	}

	@Override
	public TransportDocumentGwtServiceAsync getTransportdocumentService() {
		return TRANSPORT_DOCUMENT_SERVICE;
	}

	@Override
	public PaymentTypeGwtServiceAsync getPaymentService() {
		return PAYMENT_TYPE_SERVICE;
	}

	@Override
	public BatchDataFetcherGwtServiceAsync getBatchfetcherService() {
		return BATCH_DATA_FETCHER_SERVICE;
	}

}
