package com.novadart.novabill.domain.dto.factory.android;

import com.novadart.novabill.android.shared.dto.ClientDTO;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Contact;

public class ClientDTOFactory {
	
	public static ClientDTO toDTO(Client client){
		if(client == null)
			return null;
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setId(client.getId());
		clientDTO.setName(client.getName());
		clientDTO.setAddress(client.getAddress());
		clientDTO.setCity(client.getCity());
		clientDTO.setProvince(client.getProvince());
		clientDTO.setCountry(client.getCountry());
		clientDTO.setPostcode(client.getPostcode());
		clientDTO.setPhone(client.getPhone());
		clientDTO.setMobile(client.getMobile());
		clientDTO.setFax(client.getFax());
		clientDTO.setEmail(client.getEmail());
		clientDTO.setWeb(client.getWeb());
		clientDTO.setVatID(client.getVatID());
		clientDTO.setSsn(client.getSsn());
		clientDTO.setDefaultPaymentTypeID(client.getDefaultPaymentType() == null? null: client.getDefaultPaymentType().getId());
//		clientDTO.setDefaultPriceListID(client.getDefaultPriceList() == null? null: client.getDefaultPriceList().getId());
		clientDTO.setNote(client.getNote());
		clientDTO.setContact(ContactDTOFactory.toDTO(client.getContact()));
		return clientDTO;
	}
	
	public static void copyFromDTO(Client client, ClientDTO clientDTO){
		if(client == null || clientDTO == null)
			return;
		client.setName(clientDTO.getName());
		client.setAddress(clientDTO.getAddress());
		client.setCity(clientDTO.getCity());
		client.setProvince(clientDTO.getProvince());
		client.setCountry(clientDTO.getCountry());
		client.setPostcode(clientDTO.getPostcode());
		client.setPhone(clientDTO.getPhone());
		client.setMobile(clientDTO.getMobile());
		client.setFax(clientDTO.getFax());
		client.setEmail(clientDTO.getEmail());
		client.setWeb(clientDTO.getWeb());
		client.setVatID(clientDTO.getVatID());
		client.setSsn(clientDTO.getSsn());
		client.setContact(new Contact());
		ContactDTOFactory.copyFromDTO(client.getContact(), clientDTO.getContact());
		client.setNote(clientDTO.getNote());
	}

}
