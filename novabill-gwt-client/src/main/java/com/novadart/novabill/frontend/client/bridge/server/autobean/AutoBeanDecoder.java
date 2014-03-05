package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.SettingsDTO;

public class AutoBeanDecoder {

	public static CommodityDTO decode(Commodity commodity){
		CommodityDTO c = new CommodityDTO();
		c.setDescription(commodity.getDescription());
		c.setId(commodity.getId());
		c.setWeight(commodity.getWeight() != null ? new BigDecimal(commodity.getWeight()) : null);
		c.setService(commodity.isService());
		c.setSku(commodity.getSku());
		c.setTax(commodity.getTax() != null ? new BigDecimal(commodity.getTax()) : null);
		c.setUnitOfMeasure(commodity.getUnitOfMeasure());
		c.setBusiness(Configuration.getBusiness());

		Map<String, PriceDTO> prices = new HashMap<String, PriceDTO>();
		Map<String, Price> tmp = commodity.getPricesMap().getPrices();
		for (String plName : tmp.keySet()) {
			prices.put(plName, decode(tmp.get(plName)));
		}
		c.setPrices(prices);

		return c;
	}


	public static PriceDTO decode(Price price) {
		PriceDTO p = new PriceDTO();
		p.setCommodityID(price.getCommodityID());
		p.setId(price.getId());
		p.setPriceListID(price.getPriceListID());
		p.setPriceType(price.getPriceType() != null ? PriceType.valueOf(price.getPriceType()) : null);
		p.setPriceValue(price.getPriceValue() != null ? new BigDecimal(price.getPriceValue()) : null);
		return p;
	}


	public static PriceListDTO decode(PriceList as) {
		PriceListDTO p = new PriceListDTO();
		p.setBusiness(Configuration.getBusiness());
		p.setId(as.getId());
		p.setName(as.getName());

		//shallow copy should be enough
//		List<CommodityDTO> commodities = new ArrayList<CommodityDTO>();
//		for (Commodity c : as.getCommodityList().getCommodities()) {
//			commodities.add(decode(c));
//		}
//		p.setCommodities(commodities);
		
		return p;
	}

	
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
		b.setPremium(bu.getPremium());
		b.setProvince(bu.getProvince());
		b.setSsn(bu.getSsn());
		b.setVatID(bu.getVatID());
		b.setWeb(bu.getWeb());
		b.setSettings(decode(bu.getSettings()));
		return b;
	}
}
