package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.ui.center.BusinessView;

public class BusinessActivity extends BasicActivity {

	public BusinessActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getBusinessView(new AsyncCallback<BusinessView>() {
			
			@Override
			public void onSuccess(BusinessView bv) {
				bv.setPresenter(BusinessActivity.this);
				panel.setWidget(bv);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

}
