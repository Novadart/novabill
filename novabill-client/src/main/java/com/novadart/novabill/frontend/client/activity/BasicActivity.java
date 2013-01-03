package com.novadart.novabill.frontend.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;

public abstract class BasicActivity extends AbstractActivity implements Presenter {
	
	private final ClientFactory clientFactory;
	
	public BasicActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	protected ClientFactory getClientFactory() {
		return clientFactory;
	}
	
	@Override
	public void goTo(Place place) {
		this.clientFactory.getPlaceController().goTo(place);
		
	}
	
	protected void manageError(){
		Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
		goTo(new HomePlace());
	}
	
}
