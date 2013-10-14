package com.novadart.novabill.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.bridge.ServerBridge;
import com.novadart.novabill.frontend.client.bridge.UiBridge;

public class Novabill implements EntryPoint {
	
	private static abstract class Callback implements AsyncCallback<Void>{
		
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public void onModuleLoad() {
		new ServerBridge().inject(new Callback() {
			
			@Override
			public void onSuccess(Void result) {
				new UiBridge().inject(new Callback() {

					@Override
					public void onSuccess(Void result) {
						notifyGWTLoaded();
					}
				});
			}
			
		});

	}

	
	private static native void notifyGWTLoaded()/*-{
		if($wnd.onGWTLoaded){
			$wnd.onGWTLoaded();
		}
	}-*/;
}
