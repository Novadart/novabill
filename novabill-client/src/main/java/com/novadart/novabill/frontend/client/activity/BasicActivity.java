package com.novadart.novabill.frontend.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.CallbackUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;

public abstract class BasicActivity extends AbstractActivity {
	
	private final ClientFactory clientFactory;
	
	public BasicActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	protected ClientFactory getClientFactory() {
		return clientFactory;
	}
	
	protected void manageError(){
		Notification.showMessage(I18N.INSTANCE.errorServerCommunication(), new NotificationCallback<Void>() {
			
			@Override
			public void onNotificationClosed(Void value) {
				clientFactory.getPlaceController().goTo(new HomePlace());
			}
		});
	}
	
	protected abstract class DocumentCallack<T> extends ManagedAsyncCallback<T> {
		
		@Override
		public void onFailure(Throwable caught) {
			if(CallbackUtils.isServerCommunicationException(caught)){
				manageError();
			} else {
				super.onFailure(caught);
			}
		}

	}

	
}
