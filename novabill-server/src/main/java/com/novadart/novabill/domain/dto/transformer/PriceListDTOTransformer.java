package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;

import java.util.List;

public class PriceListDTOTransformer {
	
	public static PriceListDTO toDTO(PriceList priceList, List<CommodityDTO> commodities){
		if(priceList == null)
			return null;
		PriceListDTO priceListDTO = new PriceListDTO();
		priceListDTO.setId(priceList.getId());
		priceListDTO.setName(priceList.getName());
		if(commodities != null)
			priceListDTO.setCommodities(commodities);
		return priceListDTO;
	}
	
	public static void copyFromDTO(PriceList priceList, PriceListDTO priceListDTO){
		if(priceList == null || priceListDTO == null)
			return;
		priceList.setName(priceListDTO.getName());
	}

}
