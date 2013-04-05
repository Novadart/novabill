package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfBatchDataFetcherServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfBusinessServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfClientServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfCreditNoteServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfEstimationServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfInvoiceServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfPaymentTypeServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfTransportDocumentServiceAsync;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherServiceAsync;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;
import com.novadart.novabill.shared.client.facade.CreditNoteServiceAsync;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;
import com.novadart.novabill.shared.client.facade.PaymentTypeServiceAsync;
import com.novadart.novabill.shared.client.facade.TransportDocumentServiceAsync;

public class ServerFacade {

	public static final InvoiceServiceAsync invoice = new XsrfInvoiceServiceAsync();

	public static final ClientServiceAsync client = new XsrfClientServiceAsync();

	public static final BusinessServiceAsync business = new XsrfBusinessServiceAsync();

	public static final EstimationServiceAsync estimation = new XsrfEstimationServiceAsync();

	public static final CreditNoteServiceAsync creditNote = new XsrfCreditNoteServiceAsync();

	public static final TransportDocumentServiceAsync transportDocument = new XsrfTransportDocumentServiceAsync();

	public static final PaymentTypeServiceAsync payment = new XsrfPaymentTypeServiceAsync();
	
	public static final BatchDataFetcherServiceAsync batchFetcher = new XsrfBatchDataFetcherServiceAsync();

	private static final RequestBuilder FEEDBACK_REQUEST =
			new RequestBuilder(RequestBuilder.POST, Const.POST_FEEDBACK_URL);


	static {
		FEEDBACK_REQUEST.setHeader("Content-type", "application/x-www-form-urlencoded");
	}

	public static void sendFeedback(String subject, String name, String email, String message, String category, final AsyncCallback<Boolean> callback){

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

	public static void deleteLogo(final AsyncCallback<Boolean> callback){
		ServerFacade.business.generateLogoOpToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				try {
					RequestBuilder deleteLogoReq =
							new RequestBuilder(RequestBuilder.DELETE, Const.DELETE_LOGO+result);
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

}
