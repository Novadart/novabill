package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.shared.client.facade.BusinessGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.ClientGwtService;
import com.novadart.novabill.shared.client.facade.ClientGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.CreditNoteGwtService;
import com.novadart.novabill.shared.client.facade.CreditNoteGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.EstimationGwtService;
import com.novadart.novabill.shared.client.facade.EstimationGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.InvoiceGwtService;
import com.novadart.novabill.shared.client.facade.InvoiceGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.PaymentTypeGwtService;
import com.novadart.novabill.shared.client.facade.PaymentTypeGwtServiceAsync;
import com.novadart.novabill.shared.client.facade.TransportDocumentGwtService;
import com.novadart.novabill.shared.client.facade.TransportDocumentGwtServiceAsync;

public class ServerFacadeImpl implements ServerFacade {

	private static final InvoiceGwtServiceAsync invoiceService = GWT.create(InvoiceGwtService.class);

	private static final ClientGwtServiceAsync clientService = GWT.create(ClientGwtService.class);

	private static final BusinessGwtServiceAsync businessService = GWT.create(BusinessGwtService.class);

	private static final EstimationGwtServiceAsync estimationService = GWT.create(EstimationGwtService.class);

	private static final CreditNoteGwtServiceAsync creditNoteService = GWT.create(CreditNoteGwtService.class);

	private static final TransportDocumentGwtServiceAsync transportDocumentService = GWT.create(TransportDocumentGwtService.class);

	private static final PaymentTypeGwtServiceAsync paymentService = GWT.create(PaymentTypeGwtService.class);
	
	private static final BatchDataFetcherGwtServiceAsync batchFetcherService = GWT.create(BatchDataFetcherGwtService.class);

	private static final RequestBuilder FEEDBACK_REQUEST =
			new RequestBuilder(RequestBuilder.POST, ClientFactory.INSTANCE.getPostFeedbackUrl());


	static {
		FEEDBACK_REQUEST.setHeader("Content-type", "application/x-www-form-urlencoded");
	}
	
	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.facade.ServerFacade#setupXsrfProtection(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	public void setupXsrfProtection(final AsyncCallback<Void> callback){
		XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
		((ServiceDefTarget)xsrf).setServiceEntryPoint(ClientFactory.INSTANCE.getXsrfUrl());
		
		xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
			
			@Override
			public void onSuccess(XsrfToken xsrfToken) {
				((HasRpcToken) getInvoiceService()).setRpcToken(xsrfToken);
				((HasRpcToken) getClientService()).setRpcToken(xsrfToken);
				((HasRpcToken) getBusinessService()).setRpcToken(xsrfToken);
				((HasRpcToken) getEstimationService()).setRpcToken(xsrfToken);
				((HasRpcToken) getCreditnoteService()).setRpcToken(xsrfToken);
				((HasRpcToken) getTransportdocumentService()).setRpcToken(xsrfToken);
				((HasRpcToken) getPaymentService()).setRpcToken(xsrfToken);
				((HasRpcToken) getBatchfetcherService()).setRpcToken(xsrfToken);
				
				callback.onSuccess(null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
	
	

	@Override
	public void sendFeedback(String subject, String name, String email, String message, String category, final AsyncCallback<Boolean> callback){

		String payload = "name="+URL.encodeQueryString(name)
				+"&email="+URL.encodeQueryString(email)
				+"&message="+URL.encodeQueryString(message)
				+"&subject="+URL.encodeQueryString(subject)
				+"&issue="+URL.encodeQueryString(category);

		try {
			FEEDBACK_REQUEST.sendRequest(payload, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					callback.onSuccess(response.getStatusCode() == 200 && response.getText().contains("success"));
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
				}
			});

		} catch (RequestException e) {
			callback.onFailure(e);
		}

	}


	@Override
	public void deleteLogo(final AsyncCallback<Boolean> callback){
		getBusinessService().generateLogoOpToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				try {
					RequestBuilder deleteLogoReq =
							new RequestBuilder(RequestBuilder.DELETE, ClientFactory.INSTANCE.getDeleteLogo()+result);
					deleteLogoReq.sendRequest(null, new RequestCallback() {

						@Override
						public void onResponseReceived(Request request, Response response) {
							callback.onSuccess(response.getStatusCode() == 200 && response.getText().contains("success"));
						}

						@Override
						public void onError(Request request, Throwable exception) {
							callback.onFailure(exception);
						}
					});

				} catch (RequestException e) {
					callback.onFailure(e);
				}
			}
		});
	}


	@Override
	public InvoiceGwtServiceAsync getInvoiceService() {
		return invoiceService;
	}


	@Override
	public ClientGwtServiceAsync getClientService() {
		return clientService;
	}


	@Override
	public BusinessGwtServiceAsync getBusinessService() {
		return businessService;
	}


	@Override
	public EstimationGwtServiceAsync getEstimationService() {
		return estimationService;
	}


	@Override
	public CreditNoteGwtServiceAsync getCreditnoteService() {
		return creditNoteService;
	}


	@Override
	public TransportDocumentGwtServiceAsync getTransportdocumentService() {
		return transportDocumentService;
	}


	@Override
	public PaymentTypeGwtServiceAsync getPaymentService() {
		return paymentService;
	}


	@Override
	public BatchDataFetcherGwtServiceAsync getBatchfetcherService() {
		return batchFetcherService;
	}

}
