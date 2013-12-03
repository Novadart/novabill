package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;

import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.shared.client.dto.CommodityDTO;

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
		return c;
	}

}
