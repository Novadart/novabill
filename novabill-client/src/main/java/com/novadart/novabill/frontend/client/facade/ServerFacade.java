package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfBusinessServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfClientServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfCreditNoteServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfEstimationServiceAsync;
import com.novadart.novabill.frontend.client.facade.xsrf.XsrfInvoiceServiceAsync;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;
import com.novadart.novabill.shared.client.facade.CreditNoteServiceAsync;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;

public class ServerFacade {
	
	public static final InvoiceServiceAsync invoice = new XsrfInvoiceServiceAsync();

	public static final ClientServiceAsync client = new XsrfClientServiceAsync();

	public static final BusinessServiceAsync business = new XsrfBusinessServiceAsync();

	public static final EstimationServiceAsync estimation = new XsrfEstimationServiceAsync();
	
	public static final CreditNoteServiceAsync creditNote = new XsrfCreditNoteServiceAsync();
	
	
	private static final RequestBuilder AUTH_REQUEST =
			new RequestBuilder(RequestBuilder.POST, GWT.getHostPageBaseURL()+"resources/j_spring_security_check");

	static {
		AUTH_REQUEST.setHeader("Content-type", "application/x-www-form-urlencoded");
	}

	
	public static void authenticate(String username, String password, final AsyncCallback<Boolean> callback){

		String payload = "j_username="+URL.encodeQueryString(username)
				+"&j_password="+URL.encodeQueryString(password)
				+"&gwt=1";
		
		try {
			AUTH_REQUEST.sendRequest(payload, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					callback.onSuccess(response.getStatusCode() == 200);
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

}
