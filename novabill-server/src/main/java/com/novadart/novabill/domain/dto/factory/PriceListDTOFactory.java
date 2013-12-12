package com.novadart.novabill.domain.dto.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public static PriceListDTO mergePriceListDTOs(PriceListDTO defaultPL, PriceListDTO customPL){
		Map<Long, PriceDTO> customPrices = new HashMap<Long, PriceDTO>();
		for(PriceDTO dto: customPL.getPrices())
			customPrices.put(dto.getCommodityID(), dto);
		for(PriceDTO dto: defaultPL.getPrices()){
			if(customPrices.containsKey(dto.getCommodityID())){
				PriceDTO custDto = customPrices.get(dto.getCommodityID());
				dto.setPriceType(custDto.getPriceType());
				dto.setPriceValue(custDto.getPriceValue());
				dto.setPriceListID(custDto.getPriceListID());
			}
		}
		return defaultPL;
	}
	
	public static void copyFromDTO(PriceList priceList, PriceListDTO priceListDTO){
		if(priceList == null || priceListDTO == null)
			return;
		priceList.setName(priceListDTO.getName());
	}

}
