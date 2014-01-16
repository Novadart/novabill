package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanEncoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.PriceList;
import com.novadart.novabill.frontend.client.bridge.server.autobean.PriceListList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.tuple.Pair;


public class BatchDataFetcherServiceJS extends ServiceJS {

	
	public static void fetchSelectCommodityForDocItemOpData(String clientID, final JavaScriptObject callback){
		SERVER_FACADE.getBatchfetcherService().fetchSelectCommodityForDocItemOpData(Long.parseLong(clientID), 
				new ManagedAsyncCallback<Pair<PriceListDTO,List<PriceListDTO>>>() {

			@Override
			public void onSuccess(Pair<PriceListDTO, List<PriceListDTO>> result) {
				com.novadart.novabill.frontend.client.bridge.server.autobean.Pair p = AutoBeanMaker.INSTANCE.makePair().as();
				
				p.setFirst(AutoBeanEncoder.encode(result.getFirst()).as());
				
				PriceListList pll = AutoBeanMaker.INSTANCE.makePriceListList().as();
				List<PriceList> plist = new ArrayList<PriceList>();
				for (PriceListDTO priceList : result.getSecond()) {
					plist.add(AutoBeanEncoder.encode(priceList).as());
				}
				pll.setList(plist);
				p.setSecond(pll);
				
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(p), callback);
			}
		});
	}
	
}
