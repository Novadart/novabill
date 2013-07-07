package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.presenter.west.payment.PaymentViewPresenter;
import com.novadart.novabill.frontend.client.view.west.configuration.ConfigurationWestView;

public class PaymentActivity extends BasicActivity {

	public PaymentActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		getClientFactory().getConfigurationWestView(new AsyncCallback<ConfigurationWestView>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ConfigurationWestView result) {
				PaymentViewPresenter p = new PaymentViewPresenter(getClientFactory().getPlaceController(), eventBus, result);
				p.go(panel);
			}
		});
	}

}
