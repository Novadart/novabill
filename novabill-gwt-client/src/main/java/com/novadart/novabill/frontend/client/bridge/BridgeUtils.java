package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class BridgeUtils {
	
	public static <T> void invokeJSCallback(AutoBean<T> bean, JavaScriptObject callback){
		invokeJSCallbackOnObject(AutoBeanCodex.encode(bean).getPayload(), callback);
	}
	
	public static native void invokeJSCallback(JavaScriptObject callback)/*-{
		callback.onSuccess();
	}-*/;
	
	public static void invokeJSCallback(Integer value, JavaScriptObject callback){
		invokeJSCallbackOnValue(String.valueOf(value), callback);
	}
	
	public static void invokeJSCallback(Long value, JavaScriptObject callback){
		invokeJSCallbackOnValue(String.valueOf(value), callback);
	}
	
	public static void invokeJSCallback(Boolean value, JavaScriptObject callback){
		invokeJSCallbackOnValue(String.valueOf(value), callback);
	}
	
	public static native void invokeJSCallbackOnException(String exName, String value, JavaScriptObject callback)/*-{
		callback.onFailure({
			exception : exName,
			data : value
		});
	}-*/;

	
	private static native void invokeJSCallbackOnObject(String json, JavaScriptObject callback)/*-{
		callback.onSuccess(json!=null ? $wnd.$.parseJSON(json) : null);
	}-*/;
	
	private static native void invokeJSCallbackOnValue(String value, JavaScriptObject callback)/*-{
		callback.onSuccess(value);
	}-*/;
}
