package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;

public class HomeActivity extends AbstractCenterActivity {

	public HomeActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}
	
	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);
		
//		getClientFactory().getHomeView(new AsyncCallback<HomeView>() {
//			
//			@Override
//			public void onSuccess(HomeView hv) {
//				HomePresenter p = new HomePresenter(getClientFactory().getPlaceController(), getClientFactory().getEventBus(), hv);
//				p.go(panel);
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				
//			}
//		});
	}

}
