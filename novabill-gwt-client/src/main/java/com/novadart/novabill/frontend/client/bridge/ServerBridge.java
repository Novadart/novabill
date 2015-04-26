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

			client : {
				add : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::add(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				update : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::update(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				searchClients : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::searchClients(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				get : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::get(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				addClientAddress : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::addClientAddress(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				getClientAddresses : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::getClientAddresses(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				removeClientAddress : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::removeClientAddress(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				updateClientAddress : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::updateClientAddress(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)
			},
			
			invoice : {
				getAllForClient : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::getAllForClient(*),
				getAllInRange : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::getAllInRange(*),
				remove : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				setPayed : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::setPayed(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/google/gwt/core/client/JavaScriptObject;),
				getAllUnpaidInDateRange : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::getAllUnpaidInDateRange(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)
			},
			
			estimation : {
				getAllForClient : @com.novadart.novabill.frontend.client.bridge.server.EstimationServiceJS::getAllForClient(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				getAllInRange : @com.novadart.novabill.frontend.client.bridge.server.EstimationServiceJS::getAllInRange(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.EstimationServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			},

			creditNote : {
				getAllForClient : @com.novadart.novabill.frontend.client.bridge.server.CreditNoteServiceJS::getAllForClient(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				getAllInRange : @com.novadart.novabill.frontend.client.bridge.server.CreditNoteServiceJS::getAllInRange(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.CreditNoteServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			},
			
			transportDocument : {
				getAllForClient : @com.novadart.novabill.frontend.client.bridge.server.TransportDocumentServiceJS::getAllForClient(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				getAllInRange : @com.novadart.novabill.frontend.client.bridge.server.TransportDocumentServiceJS::getAllInRange(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.TransportDocumentServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			}

		}

	}-*/;

}