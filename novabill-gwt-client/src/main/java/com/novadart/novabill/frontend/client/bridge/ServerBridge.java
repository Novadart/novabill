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
				getStats : @com.novadart.novabill.frontend.client.bridge.server.BusinessServiceJS::getStats(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				update : @com.novadart.novabill.frontend.client.bridge.server.BusinessServiceJS::update(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			},
			
			client : {
				add : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::add(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				searchClients : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::searchClients(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				get : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::get(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.ClientServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)
			},
			
			invoice : {
				getAllForClient : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::getAllForClient(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				getAllInRange : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::getAllInRange(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				setPayed : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::setPayed(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/google/gwt/core/client/JavaScriptObject;),
				getAllUnpaidInDateRange : @com.novadart.novabill.frontend.client.bridge.server.InvoiceServiceJS::getAllUnpaidInDateRange(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
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
			},
			
			commodity : {
				getAll : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::getAll(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				get : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::get(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				add : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::add(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				update : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::update(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				addOrUpdatePrice : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::addOrUpdatePrice(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				addOrUpdatePrices : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::addOrUpdatePrices(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				removePrice : @com.novadart.novabill.frontend.client.bridge.server.CommodityServiceJS::removePrice(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			},
			
			priceList : {
				getAll : @com.novadart.novabill.frontend.client.bridge.server.PriceListServiceJS::getAll(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				get : @com.novadart.novabill.frontend.client.bridge.server.PriceListServiceJS::get(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				remove : @com.novadart.novabill.frontend.client.bridge.server.PriceListServiceJS::remove(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				add : @com.novadart.novabill.frontend.client.bridge.server.PriceListServiceJS::add(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				update : @com.novadart.novabill.frontend.client.bridge.server.PriceListServiceJS::update(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
				clonePriceList : @com.novadart.novabill.frontend.client.bridge.server.PriceListServiceJS::clonePriceList(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			},
			
			batchDataFetcher : {
				fetchSelectCommodityForDocItemOpData : @com.novadart.novabill.frontend.client.bridge.server.BatchDataFetcherServiceJS::fetchSelectCommodityForDocItemOpData(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			}
			
		}

	}-*/;

}