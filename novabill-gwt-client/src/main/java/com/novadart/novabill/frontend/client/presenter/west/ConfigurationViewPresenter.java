package com.novadart.novabill.frontend.client.presenter.west;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.west.configuration.ConfigurationWestView;

public abstract class ConfigurationViewPresenter extends AbstractPresenter<ConfigurationWestView> implements ConfigurationWestView.Presenter {

	public ConfigurationViewPresenter(PlaceController placeController, EventBus eventBus, ConfigurationWestView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(ConfigurationWestView view) {
		view.setPresenter(this);
	}
	
}
