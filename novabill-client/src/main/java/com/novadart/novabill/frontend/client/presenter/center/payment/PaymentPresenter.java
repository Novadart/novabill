package com.novadart.novabill.frontend.client.presenter.center.payment;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.payment.PaymentView;

public class PaymentPresenter extends AbstractPresenter<PaymentView> implements PaymentView.Presenter {

	
	public PaymentPresenter(PlaceController placeController, EventBus eventBus, PaymentView view) {
		super(placeController, eventBus, view);
	}
	
	@Override
	protected void setPresenterInView(PaymentView view) {
		view.setPresenter(this);
	}
	
	@Override
	public void go(AcceptsOneWidget panel) {
		MainWidget.getInstance().setStandardView();
		panel.setWidget(getView());
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}


}
