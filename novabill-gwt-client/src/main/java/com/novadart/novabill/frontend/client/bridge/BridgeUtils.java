package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class BridgeUtils {
	
	public static <T> void invokeJSCallback(AutoBean<T> bean, JavaScriptObject callback){
		invokeJSCallback(AutoBeanCodex.encode(bean).getPayload(), callback);
	}
	
	public static native void invokeJSCallback(String json, JavaScriptObject callback)/*-{
		callback.onSuccess(json!=null ? $wnd.$.parseJSON(json) : null);
	}-*/;
	
	public static native void invokeJSCallback(JavaScriptObject callback)/*-{
		callback.onSuccess();
	}-*/;

}
