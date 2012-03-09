package com.novadart.novabill.frontend.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.ui.center.BusinessView;
import com.novadart.novabill.frontend.client.ui.center.ClientView;
import com.novadart.novabill.frontend.client.ui.center.HomeView;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.west.WestView;

public interface ClientFactory {
	PlaceController getPlaceController();
	EventBus getEventBus();
	
	//center
	HomeView getHomeView();
	InvoiceView getInvoiceView();
	BusinessView getBusinessView();
	ClientView getClientView();
	
	//west
	WestView getWestView();
}
