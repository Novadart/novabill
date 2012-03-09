package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.ui.center.ClientView;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientActivity extends BasicActivity {

	private final ClientPlace place;

	public ClientActivity(ClientPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		final ClientView cv = getClientFactory().getClientView();
		
		ServerFacade.client.get(place.getClientId(), new AuthAwareAsyncCallback<ClientDTO>() {

			@Override
			public void onException(Throwable caught) {
				Window.alert(I18N.get.errorServerCommunication());
			}

			@Override
			public void onSuccess(ClientDTO result) {
				cv.setClient(result);
				cv.setPresenter(ClientActivity.this);
				panel.setWidget(cv);
			}
		});


	}

}
