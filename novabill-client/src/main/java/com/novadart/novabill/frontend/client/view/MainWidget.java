package com.novadart.novabill.frontend.client.view;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;

public interface MainWidget {
	public static final MainWidget INSTANCE = GWT.create(MainWidget.class);

	SimplePanel getWestContainer();

	SimplePanel getCenterContainer();

	void setPlaceController(PlaceController placeController);

	void setEventBus(EventBus eventBus);

	void setStandardView();

	void setLargeView();
	
}
