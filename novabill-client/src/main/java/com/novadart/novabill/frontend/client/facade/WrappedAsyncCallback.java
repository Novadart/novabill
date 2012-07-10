package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.ui.premium.GoPremiumDialog;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

public abstract class WrappedAsyncCallback<T> implements AsyncCallback<T> {

	private static boolean authDialogVisible = false;

	@Override
	public final void onFailure(Throwable caught) {
		if(caught instanceof NotAuthenticatedException){
			
			if(!authDialogVisible) {
				showAuthDialog();
			}
			
		} else if(caught instanceof AuthorizationException){
			
			if(!GoPremiumDialog.getInstance().isShowing()) {
				GoPremiumDialog.getInstance().showCentered();
			}
			
		} else {
			
			onException(caught);
			
		}
	}

	private class AuthCallback implements AsyncCallback<Boolean> {

		private AuthDialog dialog;

		public void setDialog(AuthDialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onFailure(Throwable caught) {
			dialog.hide();
			dialog.removeFromParent();

			showAuthDialog();
		}

		@Override
		public void onSuccess(Boolean result) {
			Window.Location.reload();
		}

	}

	private void showAuthDialog(){
		AuthCallback callback = new AuthCallback();
		AuthDialog dialog = new AuthDialog(callback);
		callback.setDialog(dialog);

		dialog.showCentered();
		authDialogVisible = true;
	}

	public abstract void onException(Throwable caught);

}
