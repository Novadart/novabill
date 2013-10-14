package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.view.bootstrap.BootstrapDialog;
import com.novadart.novabill.frontend.client.widget.dialog.client.ClientDialog;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class UiBridge implements ApiBridge {
	
	@Override
	public void inject(AsyncCallback<Void> callback){
		Configuration.injectCss();
		injectNative();
		callback.onSuccess(null);
	}
	
	public native void injectNative()/*-{
		$wnd.GWT_UI = {
		
			bootstrapDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showBootstrapDialog(),
			clientDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showNewClientDialog(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			modifyClientDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showModifyClientDialog(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)
		}
	
	}-*/;
	
	public static void showBootstrapDialog(){
		
		BootstrapDialog dialog = new BootstrapDialog();
		dialog.showCentered();
		
	}
	
	public static void showNewClientDialog(String businessId, JavaScriptObject callback){
		
		ClientDialog clientDialog = new ClientDialog(Long.parseLong(businessId), callback);
		clientDialog.showCentered();
		
	}
	
	public static void showModifyClientDialog(final String businessId, String clientId, final JavaScriptObject callback){
		ServerFacade.INSTANCE.getClientService().get(Long.parseLong(clientId), new ManagedAsyncCallback<ClientDTO>() {

			@Override
			public void onSuccess(ClientDTO result) {
				ClientDialog clientDialog = new ClientDialog(Long.parseLong(businessId), callback);
				clientDialog.setClient(result);
				clientDialog.showCentered();
			}
		});
	}

}
