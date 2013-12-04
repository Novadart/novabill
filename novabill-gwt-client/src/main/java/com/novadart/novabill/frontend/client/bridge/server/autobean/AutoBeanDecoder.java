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
		c.setTax(commodity.getTax() != null ? BigDecimal.valueOf(commodity.getTax()) : null);
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
		p.setPriceType(price.getPriceType() != null ? PriceType.valueOf(price.getPriceType()) : null);
		p.setPriceValue(price.getPriceValue() != null ? BigDecimal.valueOf(price.getPriceValue()) : null);
		return p;
	}

}
