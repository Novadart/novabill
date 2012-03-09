package com.novadart.novabill.frontend.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.ui.View.Presenter;

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
	
}
