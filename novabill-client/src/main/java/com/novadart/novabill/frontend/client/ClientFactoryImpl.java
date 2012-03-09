package com.novadart.novabill.frontend.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.novadart.novabill.frontend.client.ui.View;
import com.novadart.novabill.frontend.client.ui.center.BusinessView;
import com.novadart.novabill.frontend.client.ui.center.ClientView;
import com.novadart.novabill.frontend.client.ui.center.HomeView;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.center.business.BusinessViewImpl;
import com.novadart.novabill.frontend.client.ui.center.client.ClientViewImpl;
import com.novadart.novabill.frontend.client.ui.center.home.HomeViewImpl;
import com.novadart.novabill.frontend.client.ui.center.invoice.InvoiceViewImpl;
import com.novadart.novabill.frontend.client.ui.west.WestView;
import com.novadart.novabill.frontend.client.ui.west.WestViewImpl;

public class ClientFactoryImpl implements ClientFactory
{
	private static final EventBus eventBus = 
			new SimpleEventBus();
	private static final PlaceController placeController = 
			new PlaceController(eventBus);
	
	private static final Map<Class<?>, View> views =
			new HashMap<Class<?>, View>();

	@Override
	public PlaceController getPlaceController()	{
		return placeController;
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends View> T getView(Class<?> cl, View view){
		views.put(cl, view);
		view.setClean();
		return (T)view;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends View> T getView(Class<?> cl){
		View view = views.get(cl);
		view.setClean();
		return (T)view;
	}
	
	@Override
	public ClientView getClientView() {
		if(views.containsKey(ClientView.class)){
			return getView(ClientView.class);
		} else {
			return getView(ClientView.class, new ClientViewImpl());
		}
	}
	
	@Override
	public HomeView getHomeView() {
		if(views.containsKey(HomeView.class)){
			return getView(HomeView.class);
		} else {
			return getView(HomeView.class, new HomeViewImpl());
		}
	}
	
	@Override
	public InvoiceView getInvoiceView() {
		if(views.containsKey(InvoiceView.class)){
			return (InvoiceView)getView(InvoiceView.class);
		} else {
			return (InvoiceView)getView(InvoiceView.class, new InvoiceViewImpl());
		}
	}
	
	@Override
	public BusinessView getBusinessView() {
		if(views.containsKey(BusinessView.class)){
			return getView(BusinessView.class);
		} else {
			return getView(BusinessView.class, new BusinessViewImpl());
		}
	}
	
	
	@Override
	public WestView getWestView() {
		if(views.containsKey(WestView.class)){
			return getView(WestView.class);
		} else {
			return getView(WestView.class, new WestViewImpl());
		}
	}
	
	

}
