package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HistoryPrefix;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

public abstract class ManagedAsyncCallback<T> implements AsyncCallback<T> {

	private static boolean authDialogVisible = false;
	

	@Override
	public void onFailure(Throwable caught) {
		if(caught instanceof NotAuthenticatedException){
			
			if(!authDialogVisible) {
				showAuthDialog();
			}
			
		} else if(caught instanceof DataAccessException){
			
			Notification.showMessage(I18N.INSTANCE.errorDataAccessException(), new NotificationCallback<Void>() {
				
				@Override
				public void onNotificationClosed(Void value) {
					History.newItem(HistoryPrefix.HOME);
				}
			});
			
		} else if(caught instanceof NoSuchObjectException){
			
			Notification.showMessage(I18N.INSTANCE.errorDataAccessException(), new NotificationCallback<Void>() {
				
				@Override
				public void onNotificationClosed(Void value) {
					History.newItem(HistoryPrefix.HOME);
				}
			});
			
		} else if(caught instanceof AuthorizationException){
//			TODO reenable when premium is enabled
//			if(!GoPremiumDialog.getInstance().isShowing()) {
//				GoPremiumDialog.getInstance().showCentered();
//			}
			
		}  else {
			
			Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			
		}
		
	}



	private void showAuthDialog(){
		ReloadWorkspaceDialog dialog = new ReloadWorkspaceDialog();
		dialog.showCentered();
		authDialogVisible = true;
	}


}
