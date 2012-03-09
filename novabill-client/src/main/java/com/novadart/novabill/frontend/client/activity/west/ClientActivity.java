package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientActivity extends BasicActivity {

	private final ClientPlace place;
	
	public ClientActivity(ClientPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		final WestView cv = getClientFactory().getWestView();
		ServerFacade.client.get(place.getClientId(), new AuthAwareAsyncCallback<ClientDTO>() {

			@Override
			public void onException(Throwable caught) {
				Window.alert(I18N.get.errorServerCommunication());
				goTo(new HomePlace());
			}

			@Override
			public void onSuccess(ClientDTO result) {
				cv.setPresenter(ClientActivity.this);
				panel.setWidget(cv);
				
			}
		});
	}

}
