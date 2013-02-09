package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.presenter.center.business.BusinessPresenter;
import com.novadart.novabill.frontend.client.view.center.business.BusinessView;

public class BusinessActivity extends AbstractCenterActivity {

	public BusinessActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);
		
		getClientFactory().getBusinessView(new AsyncCallback<BusinessView>() {
			
			@Override
			public void onSuccess(BusinessView bv) {
				BusinessPresenter presenter = new BusinessPresenter(getClientFactory().getPlaceController(), eventBus, bv);
				presenter.go(panel);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

}
