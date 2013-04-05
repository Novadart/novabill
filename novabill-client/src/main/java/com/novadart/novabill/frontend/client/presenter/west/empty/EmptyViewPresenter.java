package com.novadart.novabill.frontend.client.presenter.west.empty;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.west.empty.EmptyWestView;

public class EmptyViewPresenter extends AbstractPresenter<EmptyWestView> implements EmptyWestView.Presenter {

	public EmptyViewPresenter(PlaceController placeController, EventBus eventBus, EmptyWestView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(EmptyWestView view) {
		view.setPresenter(this);
	}

}
