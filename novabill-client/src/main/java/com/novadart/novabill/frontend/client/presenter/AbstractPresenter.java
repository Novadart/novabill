package com.novadart.novabill.frontend.client.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

public abstract class AbstractPresenter<View> implements Presenter {
	
	private final EventBus eventBus;
	private final PlaceController placeController;
	private final View view;
	
	public AbstractPresenter(PlaceController placeController, EventBus eventBus, View view) {
		this.placeController = placeController;
		this.eventBus = eventBus;
		this.view = view;
	}
	
	protected EventBus getEventBus() {
		return eventBus;
	}
	
	protected View getView() {
		return view;
	}

	@Override
	public void goTo(Place place) {
		this.placeController.goTo(place);
	}

}
