package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.shared.client.dto.CommodityDTO;


public class CommodityDTOTransformer {
	
	public static CommodityDTO toDTO(Commodity commodity){
		if(commodity == null)
			return null;
		CommodityDTO commodityDTO = new CommodityDTO();
		commodityDTO.setId(commodity.getId());
		commodityDTO.setSku(commodity.getSku());
		commodityDTO.setDescription(commodity.getDescription());
		commodityDTO.setUnitOfMeasure(commodity.getUnitOfMeasure());
		commodityDTO.setTax(commodity.getTax());
		commodityDTO.setService(commodity.isService());
		commodityDTO.setWeight(commodity.getWeight());
		return commodityDTO;
	}
	
	public static void copyFromDTO(Commodity commodity, CommodityDTO commodityDTO){
		if(commodity == null || commodityDTO == null)
			return;
		commodity.setSku(commodityDTO.getSku());
		commodity.setDescription(commodityDTO.getDescription());
		commodity.setUnitOfMeasure(commodityDTO.getUnitOfMeasure());
		commodity.setTax(commodityDTO.getTax());
		commodity.setService(commodityDTO.isService());
		commodity.setWeight(commodityDTO.getWeight());
	}

}
