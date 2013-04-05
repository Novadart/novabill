package com.novadart.novabill.frontend.client.presenter;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.view.View;

public abstract class AbstractPresenter<V extends View<?>> implements Presenter {
	
	private final EventBus eventBus;
	private final PlaceController placeController;
	private final V view;
	
	public AbstractPresenter(PlaceController placeController, EventBus eventBus, V view) {
		this.placeController = placeController;
		this.eventBus = eventBus;
		this.view = view;
		setPresenterInView(view);
	}
	
	protected EventBus getEventBus() {
		return eventBus;
	}
	
	protected V getView() {
		return view;
	}
	
	@Override
	public void go(AcceptsOneWidget panel) {
		panel.setWidget(getView());
	}

	@Override
	public void goTo(Place place) {
		this.placeController.goTo(place);
	}
	
	protected abstract void setPresenterInView(V view);

}
