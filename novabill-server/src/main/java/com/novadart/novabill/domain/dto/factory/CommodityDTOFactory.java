package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.shared.client.dto.CommodityDTO;


public class CommodityDTOFactory {
	
	public static CommodityDTO toDTO(Commodity commodity){
		if(commodity == null)
			return null;
		CommodityDTO commodityDTO = new CommodityDTO();
		commodityDTO.setId(commodity.getId());
		commodityDTO.setName(commodity.getName());
		commodityDTO.setPrice(commodity.getPrice());
		commodityDTO.setDescription(commodity.getDescription());
		commodityDTO.setUnitOfMeasure(commodity.getUnitOfMeasure());
		commodityDTO.setTax(commodity.getTax());
		return commodityDTO;
	}

}
