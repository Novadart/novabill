package com.novadart.novabill.frontend.client.presenter.west.business;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.west.ConfigurationViewPresenter;
import com.novadart.novabill.frontend.client.view.west.configuration.ConfigurationWestView;

public class BusinessViewPresenter extends ConfigurationViewPresenter {

	public BusinessViewPresenter(PlaceController placeController, EventBus eventBus, ConfigurationWestView view) {
		super(placeController, eventBus, view);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		getView().getGeneral().addStyleName(getView().getStyle().selectedMenuItem());
	}

}
