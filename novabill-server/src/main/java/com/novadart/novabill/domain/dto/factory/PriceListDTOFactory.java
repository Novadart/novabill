package com.novadart.novabill.domain.dto.factory;

import java.util.ArrayList;
import java.util.List;

import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;

public class PriceListDTOFactory {
	
	public static PriceListDTO toDTO(PriceList priceList, boolean copyPrices){
		if(priceList == null)
			return null;
		PriceListDTO priceListDTO = new PriceListDTO();
		priceListDTO.setId(priceList.getId());
		priceListDTO.setName(priceList.getName());
		if(copyPrices){
			List<PriceDTO> prices = new ArrayList<>(priceList.getPrices().size());
			for(Price price: priceList.getPrices())
				prices.add(PriceDTOFactory.toDTO(price));
			priceListDTO.setPrices(prices);
		}
		return priceListDTO;
	}
	
	public static void copyFromDTO(PriceList priceList, PriceListDTO priceListDTO){
		if(priceList == null || priceListDTO == null)
			return;
		priceList.setName(priceListDTO.getName());
	}

}
