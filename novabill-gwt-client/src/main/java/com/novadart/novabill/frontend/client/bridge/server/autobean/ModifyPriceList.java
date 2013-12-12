package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.util.List;


public interface ModifyPriceList {
	
	public static interface CommodityPrice {
		String getSku();
		void setSku(String sku);
		String getDescription();
		void setDescription(String description);
		Price getPrice();
		void setPrice(Price price);
	}

	PriceList getPriceList();
	void setPriceList(PriceList priceList);
	List<CommodityPrice> getCommodityPriceList();
	void setCommodityPriceList(List<CommodityPrice> commodityPrice);
}
