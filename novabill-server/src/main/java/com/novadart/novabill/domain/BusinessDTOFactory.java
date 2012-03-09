package com.novadart.novabill.domain;

import com.novadart.novabill.shared.client.dto.BusinessDTO;

public class BusinessDTOFactory {
	
	public static BusinessDTO toDTO(Business business){
		if(business == null)
			return null;
		BusinessDTO businessDTO = new BusinessDTO();
		businessDTO.setId(business.getId());
		businessDTO.setName(business.getName());
		businessDTO.setAddress(business.getAddress());
		businessDTO.setCity(business.getCity());
		businessDTO.setProvince(business.getProvince());
		businessDTO.setCountry(business.getProvince());
		businessDTO.setPostcode(business.getPostcode());
		businessDTO.setPhone(business.getPhone());
		businessDTO.setMobile(business.getMobile());
		businessDTO.setFax(business.getFax());
		businessDTO.setEmail(business.getEmail());
		businessDTO.setWeb(business.getWeb());
		businessDTO.setVatID(business.getVatID());
		businessDTO.setSsn(business.getSsn());
		return businessDTO;
	}
	
	public static void copyFromDTO(Business business, BusinessDTO businessDTO){
		if(business == null || businessDTO == null)
			return;
		business.setName(businessDTO.getName());
		business.setAddress(businessDTO.getAddress());
		business.setCity(businessDTO.getCity());
		business.setProvince(businessDTO.getProvince());
		business.setCountry(businessDTO.getCountry());
		business.setPostcode(businessDTO.getPostcode());
		business.setPhone(businessDTO.getPhone());
		business.setMobile(businessDTO.getMobile());
		business.setFax(businessDTO.getFax());
		business.setEmail(businessDTO.getEmail());
		business.setWeb(businessDTO.getWeb());
		business.setVatID(businessDTO.getVatID());
		business.setSsn(businessDTO.getSsn());
	}

}
