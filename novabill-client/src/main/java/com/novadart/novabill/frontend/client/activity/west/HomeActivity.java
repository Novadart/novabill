package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.ui.west.WestView;

public class HomeActivity extends BasicActivity {


	public HomeActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getWestView(new AsyncCallback<WestView>() {
			
			@Override
			public void onSuccess(WestView wv) {
				wv.setPresenter(HomeActivity.this);
				panel.setWidget(wv);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

}
