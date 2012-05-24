package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.ui.west.WestView;

public class HomeActivity extends BasicActivity {


	public HomeActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		WestView cv = getClientFactory().getWestView();
		cv.setPresenter(this);
		panel.setWidget(cv);
	}

}
