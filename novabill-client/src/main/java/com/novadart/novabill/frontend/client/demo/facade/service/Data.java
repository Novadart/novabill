package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

class Data {
	
	private static long id = 1;
	
	private static BusinessDTO business = new BusinessDTO();
	
	private static final Map<Long, PaymentTypeDTO> PAYMENTS = new HashMap<Long, PaymentTypeDTO>();
	
	private static final Map<Long, ClientDTO> CLIENTS = new HashMap<Long, ClientDTO>();
	
	private static final Map<Long, List<InvoiceDTO>> INVOICES = new HashMap<Long, List<InvoiceDTO>>();
	private static final Map<Long, List<EstimationDTO>> ESTIMATIONS = new HashMap<Long, List<EstimationDTO>>();
	private static final Map<Long, List<CreditNoteDTO>> CREDIT_NOTES = new HashMap<Long, List<CreditNoteDTO>>();
	private static final Map<Long, List<TransportDocumentDTO>> TRANSPORT_DOCS = new HashMap<Long, List<TransportDocumentDTO>>();
	
	static {
		business.setAddress("via Novadart, 123");
		business.setCity("Padova");
		business.setCountry("IT");
		business.setEmail("info@example.org");
		business.setFax("049/123456789");
		business.setId(0L);
		business.setMobile("321 123456789");
		business.setName("Soluzioni Semplici S.r.l");
		business.setPhone("049/987654321");
		business.setPostcode("12345");
		business.setPremium(false);
		business.setProvince("PD");
		business.setSsn("AAABBB12C34D567E");
		business.setVatID("IT12345678901");
		business.setWeb("www.example.org");
		
		PaymentTypeDTO p = new PaymentTypeDTO();
		p.setBusiness(business);
		p.setDefaultPaymentNote("Rimessa Diretta");
		p.setId(nextID());
		p.setName("Rimessa diretta");
		p.setPaymentDateDelta(0);
		p.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
		PAYMENTS.put(p.getId(), p);
	}
	
	public static long nextID(){
		return id++;
	}
	
	public static BusinessDTO getBusiness() {
		return business;
	}
	
	public static void setBusiness(BusinessDTO business) {
		Data.business = business;
	}
	
	public static Map<Long, ClientDTO> getClients() {
		return CLIENTS;
	}
	
	public static Map<Long, List<InvoiceDTO>> getInvoices() {
		return INVOICES;
	}
	
	public static Map<Long, List<EstimationDTO>> getEstimations() {
		return ESTIMATIONS;
	}
	
	public static Map<Long, List<CreditNoteDTO>> getCreditNotes() {
		return CREDIT_NOTES;
	}
	
	public static Map<Long, List<TransportDocumentDTO>> getTransportDocs() {
		return TRANSPORT_DOCS;
	}

	public static Map<Long, PaymentTypeDTO> getPayments() {
		return PAYMENTS;
	}
	
}
