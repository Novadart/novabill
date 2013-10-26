package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;

public class HomeActivity extends BasicActivity {


	public HomeActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
//		getClientFactory().getStandardWestView(new AsyncCallback<StandardWestView>() {
//			
//			@Override
//			public void onSuccess(StandardWestView wv) {
//				StandardViewPresenter p = new StandardViewPresenter(getClientFactory().getPlaceController(), eventBus, wv);
//				p.go(panel);
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//			}
//		});
	}

}
