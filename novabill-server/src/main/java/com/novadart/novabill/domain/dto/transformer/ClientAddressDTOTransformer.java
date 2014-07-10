package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.ClientAddress;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;

public class ClientAddressDTOTransformer {

	public static ClientAddressDTO toDTO(ClientAddress clientAddress){
		if(clientAddress == null) return null;
		ClientAddressDTO clientAddressDTO = new ClientAddressDTO();
		clientAddressDTO.setId(clientAddress.getId());
		clientAddressDTO.setName(clientAddress.getName());
		clientAddressDTO.setCompanyName(clientAddress.getCompanyName());
		clientAddressDTO.setAddress(clientAddress.getAddress());
		clientAddressDTO.setPostcode(clientAddress.getPostcode());
		clientAddressDTO.setCity(clientAddress.getCity());
		clientAddressDTO.setProvince(clientAddress.getProvince());
		clientAddressDTO.setCountry(clientAddress.getCountry());
		return clientAddressDTO;
	}
	
	public static void copyFromDTO(ClientAddress clientAddress, ClientAddressDTO clientAddressDTO){
		if(clientAddress == null || clientAddressDTO == null) return;
		clientAddress.setName(clientAddressDTO.getName());
		clientAddress.setCompanyName(clientAddressDTO.getCompanyName());
		clientAddress.setAddress(clientAddressDTO.getAddress());
		clientAddress.setPostcode(clientAddressDTO.getPostcode());
		clientAddress.setCity(clientAddressDTO.getCity());
		clientAddress.setProvince(clientAddressDTO.getProvince());
		clientAddress.setCountry(clientAddressDTO.getCountry());
	}
	
}
