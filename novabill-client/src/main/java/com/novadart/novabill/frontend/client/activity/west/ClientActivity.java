package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientActivity extends BasicActivity {

	private final ClientPlace place;
	
	public ClientActivity(ClientPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getWestView(new AsyncCallback<WestView>() {
			
			@Override
			public void onSuccess(final WestView wv) {
				ServerFacade.client.get(place.getClientId(), new WrappedAsyncCallback<ClientDTO>() {

					@Override
					public void onException(Throwable caught) {
						Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
						goTo(new HomePlace());
					}

					@Override
					public void onSuccess(ClientDTO result) {
						wv.setPresenter(ClientActivity.this);
						panel.setWidget(wv);
						
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

}
