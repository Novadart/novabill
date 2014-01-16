package com.novadart.novabill.frontend.client.demo.facade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.user.client.Random;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;

@SuppressWarnings("deprecation")
class Data {
	
	private static final Comparator<AccountingDocumentDTO> DOCS_COMPARATOR = new Comparator<AccountingDocumentDTO>() {
		@Override
		public int compare(AccountingDocumentDTO o1, AccountingDocumentDTO o2) {
			return o1.getId().compareTo(o2.getId());
		}
	};
	
	private static final Date REFERENCE_DATE;
	
	private static long id = 1;
	
	private static BusinessDTO business = new BusinessDTO();
	
	private static final Map<Long, PaymentTypeDTO> PAYMENTS = new HashMap<Long, PaymentTypeDTO>();
	
	private static final Map<Long, ClientDTO> CLIENTS = new HashMap<Long, ClientDTO>();
	
	private static final Map<Long, Set<InvoiceDTO>> INVOICES = new HashMap<Long, Set<InvoiceDTO>>();
	private static final Map<Long, Set<EstimationDTO>> ESTIMATIONS = new HashMap<Long, Set<EstimationDTO>>();
	private static final Map<Long, Set<CreditNoteDTO>> CREDIT_NOTES = new HashMap<Long, Set<CreditNoteDTO>>();
	private static final Map<Long, Set<TransportDocumentDTO>> TRANSPORT_DOCS = new HashMap<Long, Set<TransportDocumentDTO>>();
	
	static {
		REFERENCE_DATE = new Date(new Date().getYear(), 0, 1, 0, 0);
		
		populateBusiness();
		populatePaymentTypes();
		populateClients();
	}
	
	public static BusinessDTO getBusiness() {
		return business;
	}
	
	public static void setBusiness(BusinessDTO business) {
		Data.business = business;
	}
	

	public static List<PaymentTypeDTO> getPayments() {
		return new ArrayList<PaymentTypeDTO>(PAYMENTS.values());
	}
	
	public static void removePayment(Long id) {
		PAYMENTS.remove(id);
		for (ClientDTO cl : CLIENTS.values()) {
			if(id.equals(cl.getDefaultPaymentTypeID())){
				cl.setDefaultPaymentTypeID(null);
			}
		}
	}
	
	public static PaymentTypeDTO getPayment(Long id) {
		return PAYMENTS.get(id);
	}
	
	public static ClientDTO getClient(long id) throws NoSuchObjectException{
		if(CLIENTS.containsKey(id)){
			return CLIENTS.get(id);
		}
		throw new NoSuchObjectException();
	}
	
	public static List<ClientDTO> getClients(){
		return new ArrayList<ClientDTO>(CLIENTS.values());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends AccountingDocumentDTO> List<T> getAllDocs(Class<T> clazz){
		Map<Long, Set<T>> docsMap = null;
		if(clazz.equals(InvoiceDTO.class)){
			docsMap = (Map)INVOICES;
		} else if(clazz.equals(EstimationDTO.class)) {
			docsMap = (Map)ESTIMATIONS;
		} else if(clazz.equals(CreditNoteDTO.class)) {
			docsMap = (Map)CREDIT_NOTES;
		} else if(clazz.equals(TransportDocumentDTO.class)) {
			docsMap = (Map)TRANSPORT_DOCS;
		}
		
		List<T> result = new ArrayList<T>();
		for (Set<T> s : docsMap.values()) {
			result.addAll(s);
		}
		return result;
	}
	
	public static long countInvoices(){
		int size = 0;
		for (Set<InvoiceDTO> si : INVOICES.values()) {
			for (InvoiceDTO i : si) {
				if(i.getAccountingDocumentDate().after(REFERENCE_DATE)){
					size++;
				}
			}
		}
		return size;
	}
	
	public static BigDecimal calcTotal(){
		BigDecimal total = BigDecimal.ZERO;
		
		for (Set<InvoiceDTO> si : INVOICES.values()) {
			for (InvoiceDTO i : si) {
				if(i.getAccountingDocumentDate().after(REFERENCE_DATE)){
					total = total.add(i.getTotal());
				}
			}
		}
		return total;
	}
	
	public static BigDecimal calcTotalBeforeTaxes(){
		BigDecimal total = BigDecimal.ZERO;
		
		for (Set<InvoiceDTO> si : INVOICES.values()) {
			for (InvoiceDTO i : si) {
				if(i.getAccountingDocumentDate().after(REFERENCE_DATE)){
					total = total.add(i.getTotalBeforeTax());
				}
			}
		}
		return total;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends AccountingDocumentDTO> T getDoc(Long docId, Class<T> clazz) throws NoSuchObjectException {
		Map<Long, Set<T>> docsMap = null;
		
		if(clazz.equals(InvoiceDTO.class)){
			docsMap = (Map)INVOICES;
		} else if(clazz.equals(EstimationDTO.class)) {
			docsMap = (Map)ESTIMATIONS;
		} else if(clazz.equals(CreditNoteDTO.class)) {
			docsMap = (Map)CREDIT_NOTES;
		} else if(clazz.equals(TransportDocumentDTO.class)) {
			docsMap = (Map)TRANSPORT_DOCS;
		}
		
		if(docsMap == null) {
			throw new NoSuchObjectException();
		}
		
		for (Set<T> ds : docsMap.values()) {
			for (T d : ds) {
				if(d.getId().equals(docId)){
					return (T)d;
				}
			}
		}
		
		throw new NoSuchObjectException();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends AccountingDocumentDTO> long nextDocID(Class<T> clazz){
		long id = 1L;
		
		Map<Long, Set<T>> docsMap = null;
		
		if(clazz.equals(InvoiceDTO.class)){
			docsMap = (Map)INVOICES;
		} else if(clazz.equals(EstimationDTO.class)) {
			docsMap = (Map)ESTIMATIONS;
		} else if(clazz.equals(CreditNoteDTO.class)) {
			docsMap = (Map)CREDIT_NOTES;
		} else if(clazz.equals(TransportDocumentDTO.class)) {
			docsMap = (Map)TRANSPORT_DOCS;
		}
		
		if(docsMap == null) {
			return 1;
		}
		
		for (Set<T> ds : docsMap.values()) {
			for (T d : ds) {
				if(d.getAccountingDocumentDate().before(REFERENCE_DATE)){
					continue;
				}
				
				if(d.getDocumentID() >= id){
					id = d.getDocumentID() + 1;
				}
				
			}
		}
		return id;
	}
	
	public static <T extends AccountingDocumentDTO> void save(T doc, Class<T> clazz){
		if(doc.getId() == null){
			doc.setId(id++);
		}
		getDocs(doc.getClient().getId(), clazz).add(doc);
	}
	
	public static <T extends AccountingDocumentDTO> void save(ClientDTO client){
		if(client.getId() == null){
			client.setId(id++);
		}
		CLIENTS.put(client.getId(), client);
	}
	
	public static <T extends AccountingDocumentDTO> void save(PaymentTypeDTO payment){
		if(payment.getId() == null){
			payment.setId(id++);
		}
		PAYMENTS.put(payment.getId(), payment);
	}
	
	public static <T extends AccountingDocumentDTO> void remove(Long clientId, Long docId, Class<T> clazz){
		Set<T> docs = getDocs(clientId, clazz);
		for (T doc : docs) {
			if(doc.getId().equals(docId)){
				docs.remove(doc);
			}
		}
	}
	
	public static void removeClient(Long clientId){
		CLIENTS.remove(clientId);
	}
	
	public static <T extends AccountingDocumentDTO> List<T> getDocsList(Class<T> clazz){
		return getDocsList(null, clazz);
	}
	
	public static <T extends AccountingDocumentDTO> List<T> getDocsList(Long clientId, Class<T> clazz){
		List<T> docs = new ArrayList<T>(getDocs(clientId, clazz));
		Collections.sort(docs, SharedComparators.DOCUMENT_COMPARATOR);
		return docs;
	}
	
	private static <T extends AccountingDocumentDTO> Set<T> getDocs(Long clientId, Map<Long, Set<T>> docsMap){
		Set<T> docs;
		if(clientId == null){
			docs = new TreeSet<T>(DOCS_COMPARATOR);
			for (Set<T> set : docsMap.values()) {
				docs.addAll(set);
			} 
		} else {
			docs = docsMap.get(clientId);
			docs = (docs==null) ? new TreeSet<T>(DOCS_COMPARATOR) : docs;
			docsMap.put(clientId, docs);
		}
		return docs;
	}
	
	public static <T extends AccountingDocumentDTO> Set<T> getDocs(Class<T> clazz){ 
		return getDocs(null, clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AccountingDocumentDTO> Set<T> getDocs(Long clientId, Class<T> clazz){
		if(clazz.equals(InvoiceDTO.class)){
			return (Set<T>)getDocs(clientId, INVOICES);
		} else if(clazz.equals(EstimationDTO.class)) {
			return (Set<T>)getDocs(clientId, ESTIMATIONS);
		} else if(clazz.equals(CreditNoteDTO.class)) {
			return (Set<T>)getDocs(clientId, CREDIT_NOTES);
		} else if(clazz.equals(TransportDocumentDTO.class)) {
			return (Set<T>)getDocs(clientId, TRANSPORT_DOCS);
		}
		return new HashSet<T>();
	}
	
	/**
	 * POPULATE DATA
	 */
	
	private static void populateBusiness(){
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
	}
	
	private static void populatePaymentTypes(){
		PaymentTypeDTO p = new PaymentTypeDTO();
		p.setBusiness(business);
		p.setDefaultPaymentNote("Rimessa Diretta");
		p.setId(id++);
		p.setName("Rimessa diretta");
		p.setPaymentDateDelta(0);
		p.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
		PAYMENTS.put(p.getId(), p);
		
		p = new PaymentTypeDTO();
		p.setBusiness(business);
		p.setDefaultPaymentNote("Pagamento con bonifico bancario entro 30 giorni");
		p.setId(id++);
		p.setName("Bonifico Bancario 30GG");
		p.setPaymentDateDelta(1);
		p.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
		PAYMENTS.put(p.getId(), p);
		
		p = new PaymentTypeDTO();
		p.setBusiness(business);
		p.setDefaultPaymentNote("Pagamento con bonifico bancario entro 30 giorni d.f. f.m.");
		p.setId(id++);
		p.setName("Bonifico Bancario 30GG d.f. f.m.");
		p.setPaymentDateDelta(1);
		p.setPaymentDateGenerator(PaymentDateType.END_OF_MONTH);
		PAYMENTS.put(p.getId(), p);
	}
	
	private static void populateClients(){
		ClientDTO c = new ClientDTO();
		c.setAddress("via del Sole");
		c.setCity("Bellavista");
		c.setContact(new ContactDTO());
		c.setCountry("IT");
		c.setEmail("email@example.org");
		c.setFax("1234567890");
		c.setId(id++);
		c.setMobile("1234567890");
		c.setName("Mario Rossi");
		c.setPhone("1234567890");
		c.setPostcode("12345");
		c.setProvince("PD");
		c.setSsn("IT04345620280");
		c.setVatID("IT04345620280");
		c.setWeb("www.example.org");
		CLIENTS.put(c.getId(), c);
		populateInvoices(c.getId());
		populateEstimations(c.getId());
		populateCreditNotes(c.getId());
		populateTransportDocs(c.getId());
		
		c = new ClientDTO();
		c.setAddress("via Semplicità, 44");
		c.setCity("Sottosopra");
		c.setContact(new ContactDTO());
		c.setCountry("IT");
		c.setEmail("posta@example.org");
		c.setFax("0987654321");
		c.setId(id++);
		c.setMobile("09876345678");
		c.setName("Super Company S.p.A");
		c.setPhone("345632744");
		c.setPostcode("94121");
		c.setProvince("AG");
		c.setSsn("BGGRTD38A81B374H");
		c.setVatID("");
		c.setWeb("");
		CLIENTS.put(c.getId(), c);
		
		c = new ClientDTO();
		c.setAddress("via del Mattino");
		c.setCity("Gotham");
		c.setContact(new ContactDTO());
		c.setCountry("IT");
		c.setEmail("yummimail@example.org");
		c.setFax("98765432456");
		c.setId(id++);
		c.setMobile("");
		c.setName("Pizzeria Napoleone Bonaparte S.r.l.");
		c.setPhone("34567543223");
		c.setPostcode("43221");
		c.setProvince("UD");
		c.setSsn("");
		c.setVatID("IT04345644380");
		c.setWeb("");
		CLIENTS.put(c.getId(), c);
	}
	
	private static void populateInvoices(Long clientId){
		InvoiceDTO i = new InvoiceDTO();
		i.setAccountingDocumentDate(new Date());
		i.setBusiness(business);
		i.setClient(CLIENTS.get(clientId));
		i.setDocumentID(1L);
		i.setId(id++);
		
		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>();
		
		AccountingDocumentItemDTO it = new AccountingDocumentItemDTO();
		it.setDescription("Configurazione di un sistema complesso");
		it.setId(id++);
		it.setPrice(new BigDecimal(50.0));
		it.setTax(new BigDecimal(21.0));
		it.setQuantity(new BigDecimal(16.0));
		it.setUnitOfMeasure("ore");
		it.setTotalBeforeTax(CalcUtils.calculateTotalBeforeTaxesForItem(it));
		items.add(it);
		
		it = new AccountingDocumentItemDTO();
		it.setDescription("Stampante laser");
		it.setId(id++);
		it.setPrice(new BigDecimal(200.0));
		it.setQuantity(new BigDecimal(1.0));
		it.setTax(new BigDecimal(21.0));
		it.setUnitOfMeasure("pezzi");
		it.setTotalBeforeTax(CalcUtils.calculateTotalBeforeTaxesForItem(it));
		items.add(it);
		
		it = new AccountingDocumentItemDTO();
		it.setDescription("Server ad alta affidabilità");
		it.setId(id++);
		it.setPrice(new BigDecimal(3240.0));
		it.setQuantity(new BigDecimal(1.0));
		it.setTax(new BigDecimal(21.0));
		it.setUnitOfMeasure("pezzi");
		it.setTotalBeforeTax(CalcUtils.calculateTotalBeforeTaxesForItem(it));
		items.add(it);
		
		i.setItems(items);
		
		i.setNote("");
		
		PaymentTypeDTO payment = PAYMENTS.values().toArray(new PaymentTypeDTO[0])[Random.nextInt(PAYMENTS.values().size())];
		i.setPaymentDateDelta(payment.getPaymentDateDelta());
		i.setPaymentDateGenerator(payment.getPaymentDateGenerator());
		i.setPaymentDueDate(CalcUtils.calculatePaymentDueDate(i.getAccountingDocumentDate(), payment));
		i.setPaymentNote(payment.getDefaultPaymentNote());
		i.setPaymentTypeName(payment.getName());
		CalcUtils.calculateTotals(items, i);
		save(i, InvoiceDTO.class);
	}
	
	private static void populateEstimations(Long clientId){
			
	}
	
	private static void populateCreditNotes(Long clientId){
		
	}
	
	private static void populateTransportDocs(Long clientId){
		
	}
}
