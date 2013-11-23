package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class AutoBeanConverter {
	
	
	public static AutoBean<Commodity> convert(CommodityDTO c){
		if(c == null){
			return null;
		}
		Commodity cb = AutoBeanMaker.INSTANCE.makeCommodity().as();
		cb.setDescription(c.getDescription());
		cb.setId(c.getId());
		cb.setService(c.isService());
		cb.setTax(c.getTax());
		cb.setUnitOfMeasure(c.getUnitOfMeasure());
		cb.setCustomPrices(c.getCustomPrices());
		return AutoBeanUtils.getAutoBean(cb);
	}
	
	
	public static AutoBean<Business> convert(BusinessDTO b){
		if(b == null){
			return null;
		}
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
	
	
	public static AutoBean<Page<Client>> convertClientPage(PageDTO<ClientDTO> p) {
		if(p == null){
			return null;
		}
		
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
		if(items == null){
			return null;
		}
		
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
	
	public static AutoBean<Invoice> convert(InvoiceDTO invoice) {
		if(invoice == null){
			return null;
		}
		
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

		return AutoBeanUtils.getAutoBean(ai);
	}
	
	
	public static AutoBean<Page<Invoice>> convertInvoicePage(PageDTO<InvoiceDTO> p) {
		if(p == null){
			return null;
		}
		Page<Invoice> ap = AutoBeanMaker.INSTANCE.makeInvoicePage().as();
		
		List<Invoice> l = new ArrayList<Invoice>();
		for (InvoiceDTO i : p.getItems()) {
			l.add(convert(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}
	
	
	public static AutoBean<Estimation> convert(EstimationDTO estimation) {
		if(estimation == null){
			return null;
		}
		
		Estimation ei = AutoBeanMaker.INSTANCE.makeEstimation().as();
		
		ei.setLimitations(estimation.getLimitations());
		ei.setValidTill(estimation.getValidTill());
		
		ei.setAccountingDocumentDate(estimation.getAccountingDocumentDate());
		ei.setItems(convert(estimation.getItems()));
		ei.setBusiness(convert(estimation.getBusiness()).as());
		ei.setClient(convert(estimation.getClient()).as());
		ei.setDocumentID(estimation.getDocumentID());
		ei.setId(estimation.getId());
		ei.setLayoutType(estimation.getLayoutType());
		ei.setNote(estimation.getNote());
		ei.setPaymentNote(estimation.getPaymentNote());
		ei.setTotal(estimation.getTotal());
		ei.setTotalBeforeTax(estimation.getTotalBeforeTax());
		ei.setTotalTax(estimation.getTotalTax());

		return AutoBeanUtils.getAutoBean(ei);
	}
	
	
	public static AutoBean<Page<Estimation>> convertEstimationPage(PageDTO<EstimationDTO> p) {
		if(p == null){
			return null;
		}
		
		Page<Estimation> ap = AutoBeanMaker.INSTANCE.makeEstimationPage().as();
		
		List<Estimation> l = new ArrayList<Estimation>();
		for (EstimationDTO i : p.getItems()) {
			l.add(convert(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}
	
	
	public static AutoBean<CreditNote> convert(CreditNoteDTO creditNote) {
		if(creditNote == null){
			return null;
		}
		
		CreditNote cni = AutoBeanMaker.INSTANCE.makeCreditNote().as();
		
		cni.setAccountingDocumentDate(creditNote.getAccountingDocumentDate());
		cni.setItems(convert(creditNote.getItems()));
		cni.setBusiness(convert(creditNote.getBusiness()).as());
		cni.setClient(convert(creditNote.getClient()).as());
		cni.setDocumentID(creditNote.getDocumentID());
		cni.setId(creditNote.getId());
		cni.setLayoutType(creditNote.getLayoutType());
		cni.setNote(creditNote.getNote());
		cni.setPayed(creditNote.getPayed());
		cni.setPaymentDueDate(creditNote.getPaymentDueDate());
		cni.setPaymentNote(creditNote.getPaymentNote());
		cni.setTotal(creditNote.getTotal());
		cni.setTotalBeforeTax(creditNote.getTotalBeforeTax());
		cni.setTotalTax(creditNote.getTotalTax());

		return AutoBeanUtils.getAutoBean(cni);
	}
	
	
	public static AutoBean<Page<CreditNote>> convertCreditNotePage(PageDTO<CreditNoteDTO> p) {
		if(p == null){
			return null;
		}
		
		Page<CreditNote> ap = AutoBeanMaker.INSTANCE.makeCreditNotePage().as();
		
		List<CreditNote> l = new ArrayList<CreditNote>();
		for (CreditNoteDTO i : p.getItems()) {
			l.add(convert(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}

	
	public static AutoBean<EndPoint> convert(EndpointDTO p) {
		if(p == null){
			return null;
		}
		
		EndPoint ep = AutoBeanMaker.INSTANCE.makeEndPoint().as();
		
		ep.setCity(p.getCity());
		ep.setCompanyName(p.getCompanyName());
		ep.setCountry(p.getCountry());
		ep.setPostcode(p.getPostcode());
		ep.setProvince(p.getProvince());
		ep.setStreet(p.getStreet());
		
		return AutoBeanUtils.getAutoBean(ep);
	}
	
	public static AutoBean<TransportDocument> convert(TransportDocumentDTO transportDocument) {
		if(transportDocument == null){
			return null;
		}
		
		TransportDocument tdai = AutoBeanMaker.INSTANCE.makeTransportDocument().as();
		tdai.setNumberOfPackages(transportDocument.getNumberOfPackages());
		tdai.setFromEndpoint(convert(transportDocument.getFromEndpoint()).as());
		tdai.setToEndpoint(convert(transportDocument.getToEndpoint()).as());
		tdai.setTransporter(transportDocument.getTransporter());
		tdai.setTransportationResponsibility(transportDocument.getTransportationResponsibility());
		tdai.setTradeZone(transportDocument.getTradeZone());
		tdai.setTransportStartDate(transportDocument.getTransportStartDate());
		tdai.setCause(transportDocument.getCause());
		
		tdai.setAccountingDocumentDate(transportDocument.getAccountingDocumentDate());
		tdai.setItems(convert(transportDocument.getItems()));
		tdai.setBusiness(convert(transportDocument.getBusiness()).as());
		tdai.setClient(convert(transportDocument.getClient()).as());
		tdai.setDocumentID(transportDocument.getDocumentID());
		tdai.setId(transportDocument.getId());
		tdai.setLayoutType(transportDocument.getLayoutType());
		tdai.setNote(transportDocument.getNote());
		tdai.setPaymentNote(transportDocument.getPaymentNote());
		tdai.setTotal(transportDocument.getTotal());
		tdai.setTotalBeforeTax(transportDocument.getTotalBeforeTax());
		tdai.setTotalTax(transportDocument.getTotalTax());

		return AutoBeanUtils.getAutoBean(tdai);
	}
	
	
	public static AutoBean<Page<TransportDocument>> convertTransportDocumentPage(PageDTO<TransportDocumentDTO> p) {
		if(p == null){
			return null;
		}
		
		Page<TransportDocument> ap = AutoBeanMaker.INSTANCE.makeTransportDocumentPage().as();
		
		List<TransportDocument> l = new ArrayList<TransportDocument>();
		for (TransportDocumentDTO i : p.getItems()) {
			l.add(convert(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}

}
