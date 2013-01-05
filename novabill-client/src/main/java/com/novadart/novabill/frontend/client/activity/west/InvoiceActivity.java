package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.place.invoice.InvoicePlace;
import com.novadart.novabill.frontend.client.view.west.WestView;

public class InvoiceActivity extends BasicActivity {

	public InvoiceActivity(InvoicePlace place, ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getWestView(new AsyncCallback<WestView>() {
			
			@Override
			public void onSuccess(final WestView wv) {
				wv.setPresenter(InvoiceActivity.this);
				wv.setClient(null);
				panel.setWidget(wv);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});

	}

}
