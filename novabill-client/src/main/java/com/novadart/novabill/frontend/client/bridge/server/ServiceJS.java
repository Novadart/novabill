package com.novadart.novabill.frontend.client.bridge.server;

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
	
	
	private static native void invokeJSCallbackNative(String json, JavaScriptObject callback)/*-{
		callback.onSuccess(json);
	}-*/;
}
