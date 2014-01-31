package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.ModifyPriceList.CommodityPrice;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.LogRecordDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.tuple.Pair;

public class AutoBeanEncoder {
	
	
	public static AutoBean<ModifyPriceList> encode(Pair<PriceListDTO, Map<String, Pair<String, PriceDTO>>> data){
		if(data == null){
			return null;
		}
		
		ModifyPriceList mpl = AutoBeanMaker.INSTANCE.makeModifyPriceList().as();
		
		PriceList pl = encode(data.getFirst()).as();
		mpl.setPriceList(pl);
		
		CommodityPrice cp;
		Pair<String, PriceDTO> p;
		List<CommodityPrice> prices = new ArrayList<ModifyPriceList.CommodityPrice>();
		
		for (String sku : data.getSecond().keySet()) {
			p = data.getSecond().get(sku);
			
			cp = AutoBeanMaker.INSTANCE.makeCommodityPrice().as();
			cp.setDescription(p.getFirst());
			cp.setPrice(encode(p.getSecond()).as());
			cp.setSku(sku);
			
			prices.add(cp);
		}
		
		mpl.setCommodityPriceList(prices);
		
		return AutoBeanUtils.getAutoBean(mpl);
	}
	

	public static AutoBean<PriceList> encode(PriceListDTO c){
		if(c == null){
			return null;
		}

		PriceList pl = AutoBeanMaker.INSTANCE.makePriceList().as();

		AutoBean<Business> b = encode(c.getBusiness());
		pl.setBusiness(b==null ? null : b.as());
		pl.setId(c.getId());
		pl.setName(c.getName());

		if(c.getCommodities() != null) {
			CommodityList cl = AutoBeanMaker.INSTANCE.makeCommodityList().as();
			List<Commodity> commList = new ArrayList<Commodity>();
			
			for (CommodityDTO cd : c.getCommodities()) {
				commList.add(encode(cd).as());
			}
			cl.setCommodities(commList);
			pl.setCommodityList(cl);
		}
		
		return AutoBeanUtils.getAutoBean(pl);
	}


	public static AutoBean<BusinessStats> encode(BusinessStatsDTO c){
		if(c == null){
			return null;
		}

		BusinessStats b = AutoBeanMaker.INSTANCE.makeBusinessStats().as();
		b.setClientsCount(c.getClientsCount());
		b.setCommoditiesCount(c.getCommoditiesCount());
		b.setInvoicesCountForYear(c.getInvoicesCountForYear());
		b.setTotalAfterTaxesForYear(c.getTotalAfterTaxesForYear().doubleValue());
		b.setTotalBeforeTaxesForYear(c.getTotalBeforeTaxesForYear().doubleValue());
		
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

		InvoiceCountsPerMonthList il = AutoBeanMaker.INSTANCE.makeInvoiceCountsPerMonthList().as();
		il.setList(c.getInvoiceCountsPerMonth());
		b.setInvoiceCountsPerMonth(il);

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


	public static AutoBean<Commodity> encode(CommodityDTO c){
		if(c == null){
			return null;
		}
		Commodity cb = AutoBeanMaker.INSTANCE.makeCommodity().as();
		cb.setDescription(c.getDescription());
		cb.setId(c.getId());
		cb.setService(c.isService());
		cb.setTax(c.getTax() != null ? String.valueOf(c.getTax().doubleValue()) : null);
		cb.setUnitOfMeasure(c.getUnitOfMeasure());
		cb.setSku(c.getSku());
		cb.setWeight(c.getWeight() != null ? String.valueOf(c.getWeight().doubleValue()) : null);

		if(c.getPrices() != null) {
			PricesMap pricesMap = AutoBeanMaker.INSTANCE.makePricesMap().as();
			Map<String, Price> prices = new HashMap<String, Price>();

			AutoBean<Price> p;
			for (String plName : c.getPrices().keySet()) {
				p = encode(c.getPrices().get(plName));
				prices.put(plName, p == null ? null : p.as());
			}

			pricesMap.setPrices(prices);
			cb.setPricesMap(pricesMap);
		}

		return AutoBeanUtils.getAutoBean(cb);
	}


	public static AutoBean<Price> encode(PriceDTO price) {
		if(price == null) {
			return null;
		}

		Price p = AutoBeanMaker.INSTANCE.makePrice().as();
		p.setCommodityID(price.getCommodityID());
		p.setId(price.getId());
		p.setPriceListID(price.getPriceListID());
		p.setPriceType(price.getPriceType() != null ? price.getPriceType().name() : null);
		p.setPriceValue(price.getPriceValue() != null ? String.valueOf(price.getPriceValue().doubleValue()) : null);
		return AutoBeanUtils.getAutoBean(p);
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
		ab.setPremium(b.isPremium());
		ab.setProvince(b.getProvince());
		ab.setSsn(b.getSsn());
		ab.setVatID(b.getVatID());
		ab.setWeb(b.getWeb());
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
