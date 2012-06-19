package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;

public class InvoiceActivity extends BasicActivity {

//	private final InvoicePlace place;

	public InvoiceActivity(InvoicePlace place, ClientFactory clientFactory) {
		super(clientFactory);
//		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getWestView(new AsyncCallback<WestView>() {
			
			@Override
			public void onSuccess(final WestView wv) {
				wv.setPresenter(InvoiceActivity.this);

				wv.setClient(null);
				panel.setWidget(wv);
				
//				if(place.getInvoiceId() == 0){
//
//					if(place.getClient() == null){
//					
//						goTo(new HomePlace());
//					
//					} else {
//						
//						wv.setClient(place.getClient());
//						panel.setWidget(wv);
//						
//					}
//
//				} else {
//
//					ServerFacade.client.getFromInvoiceId(place.getInvoiceId(), new WrappedAsyncCallback<ClientDTO>() {
//
//						@Override
//						public void onSuccess(ClientDTO result) {
//							wv.setClient(result);
//							panel.setWidget(wv);
//						}
//
//						@Override
//						public void onException(Throwable caught) {
//							Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
//							goTo(new HomePlace());
//						}
//					});
//
//				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});

	}

}
