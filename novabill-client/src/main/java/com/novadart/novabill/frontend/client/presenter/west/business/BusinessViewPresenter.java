package com.novadart.novabill.frontend.client.presenter.west.business;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HistoryPrefix;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.west.configuration.ConfigurationWestView;

public class BusinessViewPresenter extends AbstractPresenter<ConfigurationWestView> implements ConfigurationWestView.Presenter {

	public BusinessViewPresenter(PlaceController placeController, EventBus eventBus, ConfigurationWestView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(ConfigurationWestView view) {
		view.setPresenter(this);
	}

	@Override
	public void onLoad() {
		getView().getGeneral().setText(I18N.INSTANCE.settingsGeneral());
		getView().getGeneral().setTargetHistoryToken(HistoryPrefix.BUSINESS+":");
		
		getView().getPayment().setText(I18N.INSTANCE.settingsPayments());
		getView().getPayment().setTargetHistoryToken(HistoryPrefix.PAYMENT+":");
	}

}
