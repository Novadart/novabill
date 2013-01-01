package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

public abstract class WrappedAsyncCallback<T> implements AsyncCallback<T> {

	private static boolean authDialogVisible = false;

	@Override
	public final void onFailure(Throwable caught) {
		if(caught instanceof NotAuthenticatedException){
			
			if(!authDialogVisible) {
				showAuthDialog();
			}
			
		} else if(caught instanceof AuthorizationException){
//			TODO reenable when premium is enabled
//			if(!GoPremiumDialog.getInstance().isShowing()) {
//				GoPremiumDialog.getInstance().showCentered();
//			}
			
		} else if(caught instanceof ConcurrentAccessException) {
			
			Notification.showMessage(I18N.INSTANCE.errorConcurrentAccess());
			Window.Location.reload();
			
		} else {
			
			onException(caught);
			
		}
	}



	private void showAuthDialog(){
		ReloadWorkspaceDialog dialog = new ReloadWorkspaceDialog();
		dialog.showCentered();
		authDialogVisible = true;
	}

	public abstract void onException(Throwable caught);

}
