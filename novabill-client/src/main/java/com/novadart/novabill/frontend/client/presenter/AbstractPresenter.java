package com.novadart.novabill.frontend.client.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.novadart.novabill.frontend.client.view.View;

public abstract class AbstractPresenter<V extends View<?>> implements Presenter {
	
	private final EventBus eventBus;
	private final PlaceController placeController;
	private final V view;
	
	public AbstractPresenter(PlaceController placeController, EventBus eventBus, V view) {
		this.placeController = placeController;
		this.eventBus = eventBus;
		this.view = view;
	}
	
	protected EventBus getEventBus() {
		return eventBus;
	}
	
	protected V getView() {
		return view;
	}

	@Override
	public void goTo(Place place) {
		this.placeController.goTo(place);
	}

}
