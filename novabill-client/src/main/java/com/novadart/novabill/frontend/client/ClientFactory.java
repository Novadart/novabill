package com.novadart.novabill.frontend.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;

public interface ClientFactory {
	public static final ClientFactory INSTANCE = GWT.create(ClientFactory.class);
	
	PlaceController getPlaceController();
	EventBus getEventBus();
	
	//Urls	
	String getPostFeedbackUrl();
	String getXsrfUrl();
	String getChangePasswordUrl();
	String getDeleteLogo();
	String getUpdateLogo();
	String getUrlThumb();
	String getUrlLogo();
	void refeshLogoUrl();
	String getLogoUrl();
	String getLogoutUrl();
	String getDeleteAccountUrl();
	String getExportRequest();
	String getPdfRequest();
	String getRegisterAccountUrl();
	
	//center
//	void getHomeView(AsyncCallback<HomeView> callback);
	void getInvoiceView(AsyncCallback<InvoiceView> callback);
	void getEstimationView(AsyncCallback<EstimationView> callback);
//	void getBusinessView(AsyncCallback<BusinessView> callback);
//	void getClientView(AsyncCallback<ClientView> callback);
	void getCreditNoteView(AsyncCallback<CreditNoteView> callback);
	void getTransportDocumentView(AsyncCallback<TransportDocumentView> asyncCallback);
//	void getPaymentView(AsyncCallback<PaymentView> callback);
	
	//west
//	void getStandardWestView(AsyncCallback<StandardWestView> callback);
//	void getEmptyWestView(AsyncCallback<EmptyWestView> callback);
//	void getConfigurationWestView(AsyncCallback<ConfigurationWestView> callback);
}
