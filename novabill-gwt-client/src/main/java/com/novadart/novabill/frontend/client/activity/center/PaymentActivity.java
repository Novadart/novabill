package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.presenter.center.payment.PaymentPresenter;
import com.novadart.novabill.frontend.client.view.center.payment.PaymentView;

public class PaymentActivity extends AbstractCenterActivity {

	public PaymentActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);
		
		getClientFactory().getPaymentView(new AsyncCallback<PaymentView>() {
			
			@Override
			public void onSuccess(PaymentView result) {
				PaymentPresenter p = new PaymentPresenter(getClientFactory().getPlaceController(), eventBus, result);
				p.go(panel);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

}
