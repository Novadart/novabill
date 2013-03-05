package com.novadart.novabill.frontend.client.presenter.west.payment;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.west.ConfigurationViewPresenter;
import com.novadart.novabill.frontend.client.view.west.configuration.ConfigurationWestView;

public class PaymentViewPresenter extends ConfigurationViewPresenter {

	public PaymentViewPresenter(PlaceController placeController,EventBus eventBus, ConfigurationWestView view) {
		super(placeController, eventBus, view);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		getView().getPayment().addStyleName(getView().getStyle().selectedMenuItem());
	}

}
