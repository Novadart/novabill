package com.novadart.novabill.frontend.client.presenter.west.standard;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.west.standard.StandardWestView;
import com.novadart.novabill.frontend.client.widget.dialog.client.ClientDialog;

public class StandardViewPresenter extends AbstractPresenter<StandardWestView> implements StandardWestView.Presenter {

	public StandardViewPresenter(PlaceController placeController, EventBus eventBus, StandardWestView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(StandardWestView view) {
		view.setPresenter(this);
	}

	
	@Override
	public void onAddClientClicked() {
		ClientDialog.getInstance(getEventBus()).showCentered();
	}
	
	@Override
	public void onLoad() {
		getView().getClientSearch().setEventBus(getEventBus());
		
	}

}
