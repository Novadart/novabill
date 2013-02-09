package com.novadart.novabill.frontend.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.view.center.business.BusinessView;
import com.novadart.novabill.frontend.client.view.center.client.ClientView;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.view.center.home.HomeView;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.frontend.client.view.west.empty.EmptyWestView;
import com.novadart.novabill.frontend.client.view.west.standard.StandardWestView;

public interface ClientFactory {
	PlaceController getPlaceController();
	EventBus getEventBus();
	
	//center
	void getHomeView(AsyncCallback<HomeView> callback);
	void getInvoiceView(AsyncCallback<InvoiceView> callback);
	void getEstimationView(AsyncCallback<EstimationView> callback);
	void getBusinessView(AsyncCallback<BusinessView> callback);
	void getClientView(AsyncCallback<ClientView> callback);
	void getCreditNoteView(AsyncCallback<CreditNoteView> callback);
	void getTransportDocumentView(AsyncCallback<TransportDocumentView> asyncCallback);
	
	//west
	void getStandardWestView(AsyncCallback<StandardWestView> callback);
	void getEmptyWestView(AsyncCallback<EmptyWestView> callback);
}
