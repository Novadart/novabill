package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

public abstract class AuthAwareAsyncCallback<T> implements AsyncCallback<T> {

	private static boolean authDialogVisible = false;

	@Override
	public final void onFailure(Throwable caught) {
		if(caught instanceof NotAuthenticatedException){
			if(!authDialogVisible) {
				showAuthDialog();
			}
		} else {
			onException(caught);
		}
	}

	private class Callback implements AsyncCallback<Boolean> {

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
			dialog.hide();
			dialog.removeFromParent();

			if(result){
				authDialogVisible = false;
			} else {
				showAuthDialog();
			}
		}

	}

	private void showAuthDialog(){
		Callback callback = new Callback();
		AuthDialog dialog = new AuthDialog(callback);
		callback.setDialog(dialog);

		dialog.showCentered();
		authDialogVisible = true;
	}

	public abstract void onException(Throwable caught);

}
