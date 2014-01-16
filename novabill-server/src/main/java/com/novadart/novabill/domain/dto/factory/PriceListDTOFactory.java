package com.novadart.novabill.domain.dto.factory;

import java.util.List;

import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;

public class PriceListDTOFactory {
	
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
