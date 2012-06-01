package com.novadart.novabill.frontend.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	public void getHomeView(AsyncCallback<HomeView> callback) {
		if(views.containsKey(HomeView.class)){
			callback.onSuccess((HomeView) getView(HomeView.class));
		} else {
			callback.onSuccess((HomeView) getView(HomeView.class, new HomeViewImpl()));
		}
	}

	@Override
	public void getInvoiceView(final AsyncCallback<InvoiceView> callback) {
		if(views.containsKey(InvoiceView.class)){
			callback.onSuccess((InvoiceView) getView(InvoiceView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((InvoiceView) getView(BusinessView.class, new InvoiceViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}

	@Override
	public void getBusinessView(final AsyncCallback<BusinessView> callback) {
		if(views.containsKey(BusinessView.class)){
			callback.onSuccess((BusinessView) getView(BusinessView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((BusinessView) getView(BusinessView.class, new BusinessViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}

	@Override
	public void getClientView(final AsyncCallback<ClientView> callback) {
		if(views.containsKey(ClientView.class)){
			callback.onSuccess((ClientView) getView(ClientView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((ClientView) getView(ClientView.class, new ClientViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}

	@Override
	public void getWestView(final AsyncCallback<WestView> callback) {
		if(views.containsKey(WestView.class)){
			callback.onSuccess((WestView) getView(WestView.class));
		} else {
			callback.onSuccess((WestView) getView(WestView.class, new WestViewImpl()));
		}
	}
	
}
