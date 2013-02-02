package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.HomeView;

public class HomeActivity extends AbstractCenterActivity {

	public HomeActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}
	
	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);
		
		getClientFactory().getHomeView(new AsyncCallback<HomeView>() {
			
			@Override
			public void onSuccess(HomeView hv) {
				hv.setPresenter(HomeActivity.this);
				hv.setEventBus(eventBus);
				
				MainWidget.getInstance().setStandardView();
				panel.setWidget(hv);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
