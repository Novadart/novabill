package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.ui.MainWidget;
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
//				TODO
//				ServerFacade.business.get(new WrappedAsyncCallback<BusinessDTO>() {
//
//					@Override
//					public void onSuccess(BusinessDTO result) {
//						
//					}
//
//					@Override
//					public void onException(Throwable caught) {
//						Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
//					}
//				});
				bv.setPresenter(BusinessActivity.this);
				MainWidget.getInstance().setStandardView();
				panel.setWidget(bv);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

}
