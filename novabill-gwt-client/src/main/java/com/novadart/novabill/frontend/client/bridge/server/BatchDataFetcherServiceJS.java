package com.novadart.novabill.frontend.client.bridge.server;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanEncoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.ModifyPriceList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.tuple.Pair;

public class BatchDataFetcherServiceJS extends ServiceJS {

	public static void fetchModifyPriceList(String businessID, String priceListID, final JavaScriptObject callback) {
		SERVER_FACADE.getBatchfetcherService().fetchModifyPriceList(Long.parseLong(businessID), Long.parseLong(priceListID), 
				new ManagedAsyncCallback<Pair<PriceListDTO, Map<String, Pair<String, PriceDTO>>>>() {

					@Override
					public void onSuccess(Pair<PriceListDTO, Map<String, Pair<String, PriceDTO>>> result) {
						AutoBean<ModifyPriceList> bean = AutoBeanEncoder.encode(result);
						BridgeUtils.invokeJSCallback(bean, callback);
					}
		});
	}
	
}
