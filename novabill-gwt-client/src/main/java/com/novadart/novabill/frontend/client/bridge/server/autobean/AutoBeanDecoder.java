package com.novadart.novabill.frontend.client.bridge.server.autobean;

import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;
import com.novadart.novabill.shared.client.dto.SettingsDTO;

public class AutoBeanDecoder {


	public static ClientDTO decode(Client client) {
		ClientDTO c = new ClientDTO();
		c.setAddress(client.getAddress());
		c.setCity(client.getCity());
		c.setContact(client.getContact() != null ? decode(client.getContact()) : null);
		c.setCountry(client.getCountry());
		c.setDefaultPaymentTypeID(client.getDefaultPaymentTypeID());
		c.setDefaultPriceListID(client.getDefaultPriceListID());
		c.setEmail(client.getEmail());
		c.setFax(client.getFax());
		c.setId(client.getId());
		c.setMobile(client.getMobile());
		c.setName(client.getName());
		c.setNote(client.getNote());
		c.setPhone(client.getPhone());
		c.setPostcode(client.getPostcode());
		c.setProvince(client.getProvince());
		c.setSsn(client.getSsn());
		c.setVatID(client.getVatID());
		c.setWeb(client.getWeb());
		return c;
	}
	
	
	public static ContactDTO decode(Contact contact) {
		ContactDTO c = new ContactDTO();
		c.setEmail(contact.getEmail());
		c.setFax(contact.getFax());
		c.setFirstName(contact.getFirstName());
		c.setLastName(contact.getLastName());
		c.setMobile(contact.getMobile());
		c.setPhone(contact.getPhone());
		return c;
	}
	
	
	public static SettingsDTO decode(Settings s) {
		SettingsDTO c = new SettingsDTO();
		c.setDefaultLayoutType(s.getDefaultLayoutType());
		c.setIncognitoEnabled(s.isIncognitoEnabled());
		c.setPriceDisplayInDocsMonolithic(s.isPriceDisplayInDocsMonolithic());
		c.setCreditNoteFooterNote(s.getCreditNoteFooterNote());
		c.setEstimationFooterNote(s.getEstimationFooterNote());
		c.setInvoiceFooterNote(s.getInvoiceFooterNote());
		c.setTransportDocumentFooterNote(s.getTransportDocumentFooterNote());
		return c;
	}
	
	
	public static BusinessDTO decode(Business bu) {
		BusinessDTO b = new BusinessDTO();
		b.setAddress(bu.getAddress());
		b.setCity(bu.getCity());
		b.setCountry(bu.getCountry());
		b.setEmail(bu.getEmail());
		b.setFax(bu.getFax());
		b.setId(bu.getId());
		b.setMobile(bu.getMobile());
		b.setName(bu.getName());
		b.setPhone(bu.getPhone());
		b.setPostcode(bu.getPostcode());
		b.setProvince(bu.getProvince());
		b.setSsn(bu.getSsn());
		b.setVatID(bu.getVatID());
		b.setWeb(bu.getWeb());
		b.setSettings(decode(bu.getSettings()));
		return b;
	}
	
	public static ClientAddressDTO decode(ClientAddress c) {
		ClientAddressDTO ca = new ClientAddressDTO();
		ca.setAddress(c.getAddress());
		ca.setCity(c.getCity());
		ca.setClient(decode(c.getClient()));
		ca.setCompanyName(c.getCompanyName());
		ca.setCountry(c.getCountry());
		ca.setId(c.getId());
		ca.setName(c.getName());
		ca.setPostcode(c.getPostcode());
		ca.setProvince(c.getProvince());
		return ca;
	}
}
