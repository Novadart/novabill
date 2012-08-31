package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Item;
import com.novadart.novabill.shared.client.dto.ItemDTO;


public class ItemDTOFactory {
	
	public static ItemDTO toDTO(Item item){
		if(item == null)
			return null;
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setId(item.getId());
		itemDTO.setName(item.getName());
		itemDTO.setPrice(item.getPrice());
		itemDTO.setDescription(item.getDescription());
		itemDTO.setUnitOfMeasure(item.getUnitOfMeasure());
		itemDTO.setTax(item.getTax());
		return itemDTO;
	}

}
