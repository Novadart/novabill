package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class AutoBeanConverter {
	
	public static AutoBean<Business> convert(BusinessDTO b){
		Business ab = AutoBeanMaker.INSTANCE.makeBusiness().as();
		ab.setAddress(b.getAddress());
		ab.setCity(b.getCity());
		ab.setCountry(b.getCountry());
		ab.setEmail(b.getEmail());
		ab.setFax(b.getFax());
		ab.setId(b.getId());
		ab.setMobile(b.getMobile());
		ab.setName(b.getName());
		ab.setPhone(b.getPhone());
		ab.setPostcode(b.getPostcode());
		ab.setPremium(b.isPremium());
		ab.setProvince(b.getProvince());
		ab.setSsn(b.getSsn());
		ab.setVatID(b.getVatID());
		ab.setWeb(b.getWeb());
		return AutoBeanUtils.getAutoBean(ab);
	}
	
	
	public static AutoBean<Contact> convert(ContactDTO c){
		if(c == null){
			return null;
		}
		Contact ac = AutoBeanMaker.INSTANCE.makeContact().as();
		ac.setEmail(c.getEmail());
		ac.setFax(c.getFax());
		ac.setFirstName(c.getFirstName());
		ac.setLastName(c.getLastName());
		ac.setMobile(c.getMobile());
		ac.setPhone(c.getPhone());
		return AutoBeanUtils.getAutoBean(ac);
	}
	
	
	public static AutoBean<Client> convert(ClientDTO c){
		if(c == null){
			return null;
		}
		Client ac = AutoBeanMaker.INSTANCE.makeClient().as();
		ac.setAddress(c.getAddress());
		ac.setCity(c.getCity());
		ac.setContact(convert(c.getContact()).as());
		ac.setCountry(c.getCountry());
		ac.setDefaultPaymentTypeID(c.getDefaultPaymentTypeID());
		ac.setEmail(c.getEmail());
		ac.setFax(c.getFax());
		ac.setId(c.getId());
		ac.setMobile(c.getMobile());
		ac.setName(c.getName());
		ac.setNote(c.getNote());
		ac.setPhone(c.getPhone());
		ac.setPostcode(c.getPostcode());
		ac.setProvince(c.getProvince());
		ac.setSsn(c.getSsn());
		ac.setVatID(c.getVatID());
		ac.setWeb(c.getWeb());
		return AutoBeanUtils.getAutoBean(ac);
	}
	
	
	public static AutoBean<Page<Client>> convert(PageDTO<ClientDTO> p) {
		Page<Client> ap = AutoBeanMaker.INSTANCE.makeClientPage().as();
		
		List<Client> l = new ArrayList<Client>();
		for (ClientDTO c : p.getItems()) {
			l.add(convert(c).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}
	
	public static List<AccountingDocumentItem> convert(List<AccountingDocumentItemDTO> items){
		List<AccountingDocumentItem> result = new ArrayList<AccountingDocumentItem>();
		AccountingDocumentItem ai;
		
		for (AccountingDocumentItemDTO i : items) {
			ai = AutoBeanMaker.INSTANCE.makeAccountingDocumentItem().as();
			ai.setDescription(i.getDescription());
			ai.setId(i.getId());
			ai.setPrice(i.getPrice());
			ai.setQuantity(i.getQuantity());
			ai.setTax(i.getTax());
			ai.setTotal(i.getTotal());
			ai.setTotalBeforeTax(i.getTotalBeforeTax());
			ai.setTotalTax(i.getTotalTax());
			ai.setUnitOfMeasure(i.getUnitOfMeasure());
			result.add(ai);
		}
		
		return result;
	}
	
	public static Invoice convert(InvoiceDTO invoice) {
		Invoice ai = AutoBeanMaker.INSTANCE.makeInvoice().as();
		
		ai.setAccountingDocumentDate(invoice.getAccountingDocumentDate());
		ai.setItems(convert(invoice.getItems()));
		ai.setBusiness(convert(invoice.getBusiness()).as());
		ai.setClient(convert(invoice.getClient()).as());
		ai.setDocumentID(invoice.getDocumentID());
		ai.setId(invoice.getId());
		ai.setLayoutType(invoice.getLayoutType());
		ai.setNote(invoice.getNote());
		ai.setPayed(invoice.getPayed());
		ai.setPaymentDateDelta(invoice.getPaymentDateDelta());
		ai.setPaymentDateGenerator(invoice.getPaymentDateGenerator());
		ai.setPaymentDueDate(invoice.getPaymentDueDate());
		ai.setPaymentNote(invoice.getPaymentNote());
		ai.setPaymentTypeName(invoice.getPaymentTypeName());
		ai.setTotal(invoice.getTotal());
		ai.setTotalBeforeTax(invoice.getTotalBeforeTax());
		ai.setTotalTax(invoice.getTotalTax());

		return ai;
	}


}
