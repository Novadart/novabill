package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Endpoint;
import com.novadart.novabill.shared.client.dto.EndpointDTO;

public class EndpointDTOFactory {
	
	public static EndpointDTO toDTO(Endpoint endpoint){
		if(endpoint == null)
			return null;
		EndpointDTO endpointDTO = new EndpointDTO();
		endpointDTO.setCompanyName(endpoint.getCompanyName());
		endpointDTO.setStreet(endpoint.getStreet());
		endpointDTO.setPostcode(endpoint.getPostcode());
		endpointDTO.setCity(endpoint.getCity());
		endpointDTO.setProvince(endpoint.getProvince());
		endpointDTO.setCountry(endpoint.getCountry());
		return endpointDTO;
	}
	
	public static void copyFromDTO(Endpoint endpoint, EndpointDTO endpointDTO){
		endpoint.setCompanyName(endpointDTO.getCompanyName());
		endpoint.setStreet(endpointDTO.getStreet());
		endpoint.setPostcode(endpointDTO.getPostcode());
		endpoint.setCity(endpointDTO.getCity());
		endpoint.setProvince(endpointDTO.getProvince());
		endpoint.setCountry(endpointDTO.getCountry());
	}

}
