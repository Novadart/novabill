package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Price;
import com.novadart.novabill.shared.client.dto.PriceDTO;

public class PriceDTOFactory {
	
	public static PriceDTO toDTO(Price price){
		if(price == null)
			return null;
		PriceDTO priceDTO = new PriceDTO();
		priceDTO.setId(price.getId());
		priceDTO.setPriceType(price.getPriceType());
		priceDTO.setPriceValue(price.getPriceValue());
		priceDTO.setCommodityID(price.getCommodity().getId());
		priceDTO.setPriceListID(price.getPriceList().getId());
		return priceDTO;
	}
	
	public static void copyFromDTO(Price price, PriceDTO priceDTO){
		if(priceDTO == null || price == null)
			return;
		price.setPriceType(priceDTO.getPriceType());
		price.setPriceValue(priceDTO.getPriceValue());
	}

}
