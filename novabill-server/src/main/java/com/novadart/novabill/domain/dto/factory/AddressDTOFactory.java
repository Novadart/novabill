package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Address;
import com.novadart.novabill.shared.client.dto.AddressDTO;

public class AddressDTOFactory {
	
	public static AddressDTO toDTO(Address address){
		if(address == null)
			return null;
		AddressDTO addressDTO = new AddressDTO(); 
		addressDTO.setStreet(address.getStreet());
		addressDTO.setPostcode(address.getPostcode());
		addressDTO.setCity(address.getCity());
		addressDTO.setProvince(address.getProvince());
		return addressDTO;
	}
	
	public static void copyFromDTO(Address address, AddressDTO addressDTO){
		address.setStreet(addressDTO.getStreet());
		address.setPostcode(addressDTO.getPostcode());
		address.setCity(addressDTO.getCity());
		address.setProvince(addressDTO.getProvince());
	}

}
