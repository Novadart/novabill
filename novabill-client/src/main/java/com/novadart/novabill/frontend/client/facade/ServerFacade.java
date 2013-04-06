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
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherService;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherServiceAsync;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;
import com.novadart.novabill.shared.client.facade.ClientService;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;
import com.novadart.novabill.shared.client.facade.CreditNoteService;
import com.novadart.novabill.shared.client.facade.CreditNoteServiceAsync;
import com.novadart.novabill.shared.client.facade.EstimationService;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;
import com.novadart.novabill.shared.client.facade.InvoiceService;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;
import com.novadart.novabill.shared.client.facade.PaymentTypeService;
import com.novadart.novabill.shared.client.facade.PaymentTypeServiceAsync;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;
import com.novadart.novabill.shared.client.facade.TransportDocumentServiceAsync;

public class ServerFacade {

	public static final InvoiceServiceAsync invoice = GWT.create(InvoiceService.class);

	public static final ClientServiceAsync client = GWT.create(ClientService.class);

	public static final BusinessServiceAsync business = GWT.create(BusinessService.class);

	public static final EstimationServiceAsync estimation = GWT.create(EstimationService.class);

	public static final CreditNoteServiceAsync creditNote = GWT.create(CreditNoteService.class);

	public static final TransportDocumentServiceAsync transportDocument = GWT.create(TransportDocumentService.class);

	public static final PaymentTypeServiceAsync payment = GWT.create(PaymentTypeService.class);
	
	public static final BatchDataFetcherServiceAsync batchFetcher = GWT.create(BatchDataFetcherService.class);

	private static final RequestBuilder FEEDBACK_REQUEST =
			new RequestBuilder(RequestBuilder.POST, Const.POST_FEEDBACK_URL);


	static {
		FEEDBACK_REQUEST.setHeader("Content-type", "application/x-www-form-urlencoded");
	}
	
	public static void setupXsrfProtection(final AsyncCallback<Void> callback){
		XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
		((ServiceDefTarget)xsrf).setServiceEntryPoint(Const.XSRF_URL);
		
		xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
			
			@Override
			public void onSuccess(XsrfToken xsrfToken) {
				((HasRpcToken) invoice).setRpcToken(xsrfToken);
				((HasRpcToken) client).setRpcToken(xsrfToken);
				((HasRpcToken) business).setRpcToken(xsrfToken);
				((HasRpcToken) estimation).setRpcToken(xsrfToken);
				((HasRpcToken) creditNote).setRpcToken(xsrfToken);
				((HasRpcToken) transportDocument).setRpcToken(xsrfToken);
				((HasRpcToken) payment).setRpcToken(xsrfToken);
				((HasRpcToken) batchFetcher).setRpcToken(xsrfToken);
				
				callback.onSuccess(null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
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
