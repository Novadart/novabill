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
				get : @com.novadart.novabill.frontend.client.bridge.server.BusinessServiceJS::get(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)

			}

		}

	}-*/;



}
