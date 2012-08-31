package com.novadart.novabill.frontend.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.ui.center.BusinessView;
import com.novadart.novabill.frontend.client.ui.center.ClientView;
import com.novadart.novabill.frontend.client.ui.center.CreditNoteView;
import com.novadart.novabill.frontend.client.ui.center.EstimationView;
import com.novadart.novabill.frontend.client.ui.center.HomeView;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.west.WestView;

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
	
	//west
	void getWestView(AsyncCallback<WestView> callback);
}
