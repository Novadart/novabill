package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

public class ServerBridge implements ApiBridge {

	@Override
	public void inject(final AsyncCallback<Void> callback){
		ServerFacade.INSTANCE.setupXsrfProtection(new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				injectNative();
				callback.onSuccess(null);
			}
		});
	}

	private native void injectNative()/*-{
		$wnd.GWT_Server = {

			business : {
				get : @com.novadart.novabill.frontend.client.bridge.server.BusinessServiceJS::get(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				getClients : @com.novadart.novabill.frontend.client.bridge.server.BusinessServiceJS::getClients(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			},
			
			client : {
				searchClients : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::searchClients(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				get : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::get(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)
			},
			
			invoice : {
				getAllForClient : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::getAllForClient(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			}

		}

	}-*/;



}
