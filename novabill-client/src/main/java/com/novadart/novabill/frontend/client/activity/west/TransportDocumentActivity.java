package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.TransportDocumentPlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;

public class TransportDocumentActivity extends BasicActivity {

	private final TransportDocumentPlace place;

	public TransportDocumentActivity(TransportDocumentPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getWestView(new AsyncCallback<WestView>() {

			@Override
			public void onSuccess(final WestView wv) {
				wv.setPresenter(TransportDocumentActivity.this);
				
				if(place.getTransportDocumentId() == 0){

					if(place.getClient() == null){
					
						goTo(new HomePlace());
					
					} else {
						
						wv.setClient(place.getClient());
						panel.setWidget(wv);
						
					}

				} else {
					wv.setClient(place.getClient());
					panel.setWidget(wv);
					
				}

			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

}
