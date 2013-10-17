package com.novadart.novabill.frontend.client.bridge.server;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

abstract class ServiceJS {

	protected static final ServerFacade SERVER_FACADE = GWT.create(ServerFacade.class);

	protected static <T> void invokeJSCallback(AutoBean<T> bean, JavaScriptObject callback){
		invokeJSCallbackNative(AutoBeanCodex.encode(bean).getPayload(), callback);
	}
	
	protected static <T> void invokeJSCallback(JavaScriptObject callback){
		invokeJSCallbackNative(callback);
	}

	private static native void invokeJSCallbackNative(String json, JavaScriptObject callback)/*-{
		callback.onSuccess(json!=null ? $wnd.$.parseJSON(json) : null);
	}-*/;
	
	private static native void invokeJSCallbackNative(JavaScriptObject callback)/*-{
		callback.onSuccess();
	}-*/;
}
