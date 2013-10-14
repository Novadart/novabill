package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

abstract class ServiceJS {

	protected static final ServerFacade SERVER_FACADE = GWT.create(ServerFacade.class);
	protected static final DomainFactory DOMAIN_FACTORY = GWT.create(DomainFactory.class);

	protected static <T> void invokeJSCallback(Class<T> clazz, T domainObject, JavaScriptObject callback){
		AutoBean<T> autobean = DOMAIN_FACTORY.create(clazz, domainObject);
		invokeJSCallbackNative(AutoBeanCodex.encode(autobean).getPayload(), callback);
	}
	
	protected static <T> void invokeJSCallback(JavaScriptObject callback){
		invokeJSCallbackNative(callback);
	}

	protected static <T, E extends T> List<T> convertList(Class<T> clazz, List<E> list){
		// Using workaround detailed here https://groups.google.com/forum/#!msg/google-web-toolkit/nvIotNHy-Io/YcbECPWd-v4J
		List<T> abList = new ArrayList<T>();
		for (E ab : list) {
			abList.add(DOMAIN_FACTORY.create(clazz, ab).as());
		}
		return abList;
	}

	private static native void invokeJSCallbackNative(String json, JavaScriptObject callback)/*-{
		callback.onSuccess(json!=null ? $wnd.$.parseJSON(json) : null);
	}-*/;
	
	private static native void invokeJSCallbackNative(JavaScriptObject callback)/*-{
		callback.onSuccess();
	}-*/;
}
