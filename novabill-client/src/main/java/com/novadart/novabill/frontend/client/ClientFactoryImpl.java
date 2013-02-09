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
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.view.center.business.BusinessView;
import com.novadart.novabill.frontend.client.view.center.business.BusinessViewImpl;
import com.novadart.novabill.frontend.client.view.center.client.ClientView;
import com.novadart.novabill.frontend.client.view.center.client.ClientViewImpl;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteViewImpl;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationViewImpl;
import com.novadart.novabill.frontend.client.view.center.home.HomeView;
import com.novadart.novabill.frontend.client.view.center.home.HomeViewImpl;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceViewImpl;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentViewImpl;
import com.novadart.novabill.frontend.client.view.west.WestView;
import com.novadart.novabill.frontend.client.view.west.WestViewImpl;

public class ClientFactoryImpl implements ClientFactory
{
	private static final EventBus eventBus = 
			new SimpleEventBus();
	private static final PlaceController placeController = 
			new PlaceController(eventBus);
	
	private static final Map<Class<?>, View<?>> views =
			new HashMap<Class<?>, View<?>>();

	@Override
	public PlaceController getPlaceController()	{
		return placeController;
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends View<?>> T getView(Class<?> cl, View<?> view){
		views.put(cl, view);
		view.reset();
		return (T)view;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends View<?>> T getView(Class<?> cl){
		View<?> view = views.get(cl);
		view.reset();
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
					callback.onSuccess((InvoiceView) getView(InvoiceView.class, new InvoiceViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}
	
	@Override
	public void getEstimationView(final AsyncCallback<EstimationView> callback) {
		if(views.containsKey(EstimationView.class)){
			callback.onSuccess((EstimationView) getView(EstimationView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((EstimationView) getView(EstimationView.class, new EstimationViewImpl()));
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
	public void getCreditNoteView(final AsyncCallback<CreditNoteView> callback) {
		if(views.containsKey(CreditNoteView.class)){
			callback.onSuccess((CreditNoteView) getView(CreditNoteView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((CreditNoteView) getView(CreditNoteView.class, new CreditNoteViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}
	
	@Override
	public void getTransportDocumentView(final AsyncCallback<TransportDocumentView> callback) {
		if(views.containsKey(TransportDocumentView.class)){
			callback.onSuccess((TransportDocumentView) getView(TransportDocumentView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((TransportDocumentView) getView(TransportDocumentView.class, new TransportDocumentViewImpl()));
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
