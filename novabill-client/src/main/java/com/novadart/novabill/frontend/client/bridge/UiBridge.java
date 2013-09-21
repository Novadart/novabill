package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.view.bootstrap.BootstrapDialog;

public class UiBridge implements ApiBridge {
	
	@Override
	public void inject(AsyncCallback<Void> callback){
		Configuration.injectCss();
		injectNative();
		callback.onSuccess(null);
	}
	
	public native void injectNative()/*-{
		$wnd.GWT_UI = {
		
			bootstrapDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showBootstrapDialog()
		
		}
	
	}-*/;
	
	public static void showBootstrapDialog(){
		
		BootstrapDialog dialog = new BootstrapDialog();
		dialog.showCentered();
		
	}

}
