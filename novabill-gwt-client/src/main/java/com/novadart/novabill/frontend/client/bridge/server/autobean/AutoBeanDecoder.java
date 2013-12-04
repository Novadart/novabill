package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;

public class AutoBeanDecoder {
	
	public static CommodityDTO decode(Commodity commodity){
		CommodityDTO c = new CommodityDTO();
		c.setDescription(commodity.getDescription());
		c.setId(commodity.getId());
		c.setService(commodity.isService());
		c.setSku(commodity.getSku());
		c.setTax(BigDecimal.valueOf(commodity.getTax()));
		c.setUnitOfMeasure(commodity.getUnitOfMeasure());
		c.setBusiness(Configuration.getBusiness());
		
		Map<String, PriceDTO> prices = new HashMap<String, PriceDTO>();
		Map<String, Price> tmp = commodity.getPricesMap().getPrices();
		for (String plName : tmp.keySet()) {
			prices.put(plName, decode(tmp.get(plName)));
		}
		c.setPrices(prices);
		
		return c;
	}
	
	public static PriceDTO decode(Price price) {
		PriceDTO p = new PriceDTO();
		p.setCommodityID(price.getCommodityID());
		p.setId(price.getId());
		p.setPriceListID(price.getPriceListID());
		p.setPriceType(PriceType.valueOf(price.getPriceType()));
		p.setQuantity(BigDecimal.valueOf(price.getPriceValue()));
		return p;
	}

}
