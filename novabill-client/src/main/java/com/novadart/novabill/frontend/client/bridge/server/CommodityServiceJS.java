package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanConverter;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Commodity;
import com.novadart.novabill.frontend.client.bridge.server.autobean.CommodityList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.CommodityDTO;

public class CommodityServiceJS extends ServiceJS {
	
	public static void getAll(String businessID, final JavaScriptObject callback){
		SERVER_FACADE.getCommodityGwtService().getAll(Long.parseLong(businessID), new ManagedAsyncCallback<List<CommodityDTO>>() {

			@Override
			public void onSuccess(List<CommodityDTO> result) {
				CommodityList il = AutoBeanMaker.INSTANCE.makeCommodityList().as();
				List<Commodity> commodities = new ArrayList<Commodity>(result.size());
				for (CommodityDTO id : result) {
					commodities.add(AutoBeanConverter.convert(id).as());
				}
				il.setCommodities(commodities);
				
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
				
			}
		});
	}

	public static void add(String commodityJson, JavaScriptObject callback){
//		AutoBean<Commodity> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, Commodity.class, commodityJson);
		
		
	}

	public static void remove(String businessID, String id, JavaScriptObject callback){}

	public static void update(String commodityJson, JavaScriptObject callback){}

}
