package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.rpc.*;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.shared.client.facade.*;

public class ServerFacadeImpl implements ServerFacade {

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
				((HasRpcToken) getCreditNoteService()).setRpcToken(xsrfToken);
				((HasRpcToken) getTransportdocumentService()).setRpcToken(xsrfToken);
				((HasRpcToken) getPaymentService()).setRpcToken(xsrfToken);
				((HasRpcToken) getBatchfetcherService()).setRpcToken(xsrfToken);
				((HasRpcToken) getCommodityGwtService()).setRpcToken(xsrfToken);
				((HasRpcToken) getPriceListGwtService()).setRpcToken(xsrfToken);
				
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
		return InvoiceGwtServiceAsync.Util.getInstance();
	}


	@Override
	public ClientGwtServiceAsync getClientService() {
		return ClientGwtServiceAsync.Util.getInstance();
	}


	@Override
	public BusinessGwtServiceAsync getBusinessService() {
		return BusinessGwtServiceAsync.Util.getInstance();
	}


	@Override
	public EstimationGwtServiceAsync getEstimationService() {
		return EstimationGwtServiceAsync.Util.getInstance();
	}


	@Override
	public CreditNoteGwtServiceAsync getCreditNoteService() {
		return CreditNoteGwtServiceAsync.Util.getInstance();
	}


	@Override
	public TransportDocumentGwtServiceAsync getTransportdocumentService() {
		return TransportDocumentGwtServiceAsync.Util.getInstance();
	}


	@Override
	public PaymentTypeGwtServiceAsync getPaymentService() {
		return PaymentTypeGwtServiceAsync.Util.getInstance();
	}


	@Override
	public BatchDataFetcherGwtServiceAsync getBatchfetcherService() {
		return BatchDataFetcherGwtServiceAsync.Util.getInstance();
	}
	
	
	@Override
	public CommodityGwtServiceAsync getCommodityGwtService() {
		return CommodityGwtServiceAsync.Util.getInstance();
	}
	
	@Override
	public PriceListGwtServiceAsync getPriceListGwtService() {
		return PriceListGwtServiceAsync.Util.getInstance();
	}

}
