package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.ClientView;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientActivity extends AbstractCenterActivity {

	private final ClientPlace place;

	public ClientActivity(ClientPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);
		
		getClientFactory().getClientView(new AsyncCallback<ClientView>() {
			
			@Override
			public void onSuccess(final ClientView cv) {
				if(place.getDocs() != null){
					cv.setDocumentsListing(place.getDocs());
				}
				
				ServerFacade.client.get(place.getClientId(), new ManagedAsyncCallback<ClientDTO>() {

					@Override
					public void onSuccess(ClientDTO result) {
						cv.setClient(result);
						cv.setEventBus(eventBus);
						cv.setPresenter(ClientActivity.this);
						MainWidget.getInstance().setStandardView();
						panel.setWidget(cv);
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

}
