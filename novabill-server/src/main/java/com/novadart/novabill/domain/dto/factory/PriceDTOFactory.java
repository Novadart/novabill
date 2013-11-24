package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Price;
import com.novadart.novabill.shared.client.dto.PriceDTO;

public class PriceDTOFactory {
	
	public static PriceDTO toDTO(Price price){
		if(price == null)
			return null;
		PriceDTO priceDTO = new PriceDTO();
		priceDTO.setPriceType(price.getPriceType());
		priceDTO.setQuantity(price.getQuantity());
		priceDTO.setCommodityID(price.getCommodity().getId());
		return priceDTO;
	}

}
