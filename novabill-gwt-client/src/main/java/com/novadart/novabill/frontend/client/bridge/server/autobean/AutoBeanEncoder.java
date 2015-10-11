package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.LogRecordDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.SettingsDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class AutoBeanEncoder {
	
	


	public static AutoBean<BusinessStats> encode(BusinessStatsDTO c){
		if(c == null){
			return null;
		}

		BusinessStats b = AutoBeanMaker.INSTANCE.makeBusinessStats().as();
		b.setClientsCount(c.getClientsCount());
		b.setCommoditiesCount(c.getCommoditiesCount());
		b.setInvoicesCountForYear(c.getInvoicesCountForYear());
		b.setTotalAfterTaxesForYear(c.getTotalAfterTaxesForYear().toPlainString());
		b.setTotalBeforeTaxesForYear(c.getTotalBeforeTaxesForYear().toPlainString());
		
		LogRecordList ll = AutoBeanMaker.INSTANCE.makeLogRecordList().as();
		List<LogRecord> list = new ArrayList<LogRecord>();
		AutoBean<LogRecord> lr;
		for (LogRecordDTO logRecord : c.getLogRecords()) {
			lr = encode(logRecord);
			if(lr != null){
				list.add(lr.as());
			}
		}
		ll.setLogRecords(list);
		b.setLogRecords(ll);

		InvoiceTotalsPerMonthList il = AutoBeanMaker.INSTANCE.makeInvoiceTotalsPerMonthList().as();
		List<String> totals = new ArrayList<String>();
		for (BigDecimal bd : c.getInvoiceTotalsPerMonth()) {
			totals.add(bd.toPlainString());
		}
		il.setList(totals);
		b.setInvoiceTotalsPerMonth(il);

		return AutoBeanUtils.getAutoBean(b);
	}


	public static AutoBean<LogRecord> encode(LogRecordDTO c){
		if(c == null){
			return null;
		}

		LogRecord l = AutoBeanMaker.INSTANCE.makeLogRecord().as();
		l.setDetails(c.getDetails());
		l.setEntityID(c.getEntityID());
		l.setEntityType(c.getEntityType().name());
		l.setOperationType(c.getOperationType().name());
		l.setTime(c.getTime());

		return AutoBeanUtils.getAutoBean(l);
	}
	
	
	public static AutoBean<ClientAddress> encode(ClientAddressDTO c){
		if(c == null){
			return null;
		}

		ClientAddress ca = AutoBeanMaker.INSTANCE.makeClientAddress().as();
		ca.setAddress(c.getAddress());
		ca.setCity(c.getCity());
		ca.setClient(c.getClient() != null ? encode(c.getClient()).as() : null);
		ca.setCompanyName(c.getCompanyName());
		ca.setCountry(c.getCountry());
		ca.setId(c.getId());
		ca.setName(c.getName());
		ca.setPostcode(c.getPostcode());
		ca.setProvince(c.getProvince());
		return AutoBeanUtils.getAutoBean(ca);
	}

	public static AutoBean<Business> encode(BusinessDTO b){
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
		ab.setProvince(b.getProvince());
		ab.setSsn(b.getSsn());
		ab.setVatID(b.getVatID());
		ab.setWeb(b.getWeb());
		ab.setSettings(encode(b.getSettings()).as());
		return AutoBeanUtils.getAutoBean(ab);
	}


	public static AutoBean<Contact> encode(ContactDTO c){
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
	
	
	public static AutoBean<Settings> encode(SettingsDTO c){
		if(c == null){
			return null;
		}
		Settings s = AutoBeanMaker.INSTANCE.makeSettings().as();
		s.setDefaultLayoutType(c.getDefaultLayoutType());
		s.setIncognitoEnabled(c.isIncognitoEnabled());
		s.setPriceDisplayInDocsMonolithic(c.isPriceDisplayInDocsMonolithic());
		s.setCreditNoteFooterNote(c.getCreditNoteFooterNote());
		s.setEstimationFooterNote(c.getEstimationFooterNote());
		s.setInvoiceFooterNote(c.getInvoiceFooterNote());
		s.setTransportDocumentFooterNote(c.getTransportDocumentFooterNote());
		s.setDefaultTermsAndConditionsForEstimation(c.getDefaultTermsAndConditionsForEstimation());
		s.setEmailReplyTo(c.getEmailReplyTo());
		s.setEmailSubject(c.getEmailSubject());
		s.setEmailText(c.getEmailText());
		
		return AutoBeanUtils.getAutoBean(s);
	}


	public static AutoBean<Client> encode(ClientDTO c){
		if(c == null){
			return null;
		}
		Client ac = AutoBeanMaker.INSTANCE.makeClient().as();
		ac.setAddress(c.getAddress());
		ac.setCity(c.getCity());

		AutoBean<Contact> contact = encode(c.getContact());
		ac.setContact(contact == null ? null : contact.as());
		ac.setCountry(c.getCountry());
		ac.setDefaultPaymentTypeID(c.getDefaultPaymentTypeID());
		ac.setDefaultPriceListID(c.getDefaultPriceListID());
		
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


	public static AutoBean<Page<Client>> encodeClientPage(PageDTO<ClientDTO> p) {
		if(p == null){
			return null;
		}

		Page<Client> ap = AutoBeanMaker.INSTANCE.makeClientPage().as();

		List<Client> l = new ArrayList<Client>();
		for (ClientDTO c : p.getItems()) {
			l.add(encode(c).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}

	public static List<AccountingDocumentItem> encode(List<AccountingDocumentItemDTO> items){
		if(items == null){
			return null;
		}

		List<AccountingDocumentItem> result = new ArrayList<AccountingDocumentItem>();
		AccountingDocumentItem ai;

		for (AccountingDocumentItemDTO i : items) {
			ai = AutoBeanMaker.INSTANCE.makeAccountingDocumentItem().as();
			ai.setDescription(i.getDescription());
			ai.setId(i.getId());
			ai.setSku(i.getSku());
			ai.setPrice(i.getPrice().toPlainString());
			ai.setQuantity(i.getQuantity().toPlainString());
			ai.setWeight(i.getWeight() != null ? i.getWeight().toPlainString() : null);
			ai.setTax(i.getTax().toPlainString());
			ai.setTotal(i.getTotal().toPlainString());
			ai.setTotalBeforeTax(i.getTotalBeforeTax().toPlainString());
			ai.setTotalTax(i.getTotalTax().toPlainString());
			ai.setUnitOfMeasure(i.getUnitOfMeasure());
			result.add(ai);
		}

		return result;
	}

	public static AutoBean<Invoice> encode(InvoiceDTO invoice) {
		if(invoice == null){
			return null;
		}

		Invoice ai = AutoBeanMaker.INSTANCE.makeInvoice().as();

		ai.setAccountingDocumentDate(invoice.getAccountingDocumentDate());
		ai.setItems(encode(invoice.getItems()));
		ai.setBusiness(encode(invoice.getBusiness()).as());
		ai.setClient(encode(invoice.getClient()).as());
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
		ai.setSeenByClientTime(invoice.getSeenByClientTime());
		ai.setEmailedToClient(invoice.getEmailedToClient().toString());

		LongList ll = AutoBeanMaker.INSTANCE.makeLongList().as();
		ll.setList(invoice.getTransportDocumentIDs());
		ai.setTransportDocumentIDs(ll);
		
		return AutoBeanUtils.getAutoBean(ai);
	}


	public static AutoBean<Page<Invoice>> encodeInvoicePage(PageDTO<InvoiceDTO> p) {
		if(p == null){
			return null;
		}
		Page<Invoice> ap = AutoBeanMaker.INSTANCE.makeInvoicePage().as();

		List<Invoice> l = new ArrayList<Invoice>();
		for (InvoiceDTO i : p.getItems()) {
			l.add(encode(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}


	public static AutoBean<Estimation> encode(EstimationDTO estimation) {
		if(estimation == null){
			return null;
		}

		Estimation ei = AutoBeanMaker.INSTANCE.makeEstimation().as();

		ei.setLimitations(estimation.getLimitations());
		ei.setTermsAndConditions(estimation.getTermsAndConditions());
		ei.setValidTill(estimation.getValidTill());

		ei.setAccountingDocumentDate(estimation.getAccountingDocumentDate());
		ei.setItems(encode(estimation.getItems()));
		ei.setBusiness(encode(estimation.getBusiness()).as());
		ei.setClient(encode(estimation.getClient()).as());
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


	public static AutoBean<Page<Estimation>> encodeEstimationPage(PageDTO<EstimationDTO> p) {
		if(p == null){
			return null;
		}

		Page<Estimation> ap = AutoBeanMaker.INSTANCE.makeEstimationPage().as();

		List<Estimation> l = new ArrayList<Estimation>();
		for (EstimationDTO i : p.getItems()) {
			l.add(encode(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}


	public static AutoBean<CreditNote> encode(CreditNoteDTO creditNote) {
		if(creditNote == null){
			return null;
		}

		CreditNote cni = AutoBeanMaker.INSTANCE.makeCreditNote().as();

		cni.setAccountingDocumentDate(creditNote.getAccountingDocumentDate());
		cni.setItems(encode(creditNote.getItems()));
		cni.setBusiness(encode(creditNote.getBusiness()).as());
		cni.setClient(encode(creditNote.getClient()).as());
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


	public static AutoBean<Page<CreditNote>> encodeCreditNotePage(PageDTO<CreditNoteDTO> p) {
		if(p == null){
			return null;
		}

		Page<CreditNote> ap = AutoBeanMaker.INSTANCE.makeCreditNotePage().as();

		List<CreditNote> l = new ArrayList<CreditNote>();
		for (CreditNoteDTO i : p.getItems()) {
			l.add(encode(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}


	public static AutoBean<EndPoint> encode(EndpointDTO p) {
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

	public static AutoBean<TransportDocument> encode(TransportDocumentDTO transportDocument) {
		if(transportDocument == null){
			return null;
		}

		TransportDocument tdai = AutoBeanMaker.INSTANCE.makeTransportDocument().as();
		tdai.setNumberOfPackages(transportDocument.getNumberOfPackages());
		tdai.setFromEndpoint(encode(transportDocument.getFromEndpoint()).as());
		tdai.setToEndpoint(encode(transportDocument.getToEndpoint()).as());
		tdai.setTransporter(transportDocument.getTransporter());
		tdai.setTransportationResponsibility(transportDocument.getTransportationResponsibility());
		tdai.setTradeZone(transportDocument.getTradeZone());
		tdai.setTransportStartDate(transportDocument.getTransportStartDate());
		tdai.setCause(transportDocument.getCause());
		tdai.setInvoice(transportDocument.getInvoice());

		tdai.setAccountingDocumentDate(transportDocument.getAccountingDocumentDate());
		tdai.setItems(encode(transportDocument.getItems()));
		tdai.setBusiness(encode(transportDocument.getBusiness()).as());
		tdai.setClient(encode(transportDocument.getClient()).as());
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


	public static AutoBean<Page<TransportDocument>> encodeTransportDocumentPage(PageDTO<TransportDocumentDTO> p) {
		if(p == null){
			return null;
		}

		Page<TransportDocument> ap = AutoBeanMaker.INSTANCE.makeTransportDocumentPage().as();

		List<TransportDocument> l = new ArrayList<TransportDocument>();
		for (TransportDocumentDTO i : p.getItems()) {
			l.add(encode(i).as());
		}
		ap.setItems(l);
		ap.setLength(p.getLength());
		ap.setOffset(p.getOffset());
		ap.setTotal(p.getTotal());
		return AutoBeanUtils.getAutoBean(ap);
	}

}
