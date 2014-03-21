package com.novadart.novabill.service;

import java.io.IOException;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.EmailPasswordHolder;
import com.novadart.novabill.domain.Endpoint;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;


//@Service
public class DBUtilitiesService {
	
	private String blmDBPath = "/tmp/DATI.mdb";
	
	@PersistenceContext
	private EntityManager em;
	
	private PaymentType[] paymentTypes = new PaymentType[]{
			new PaymentType("Rimessa Diretta", "Pagamento in Rimessa Diretta", PaymentDateType.IMMEDIATE, 0, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Bonifico Bancario 30GG", "Pagamento con bonifico bancario entro 30 giorni", PaymentDateType.IMMEDIATE, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Bonifico Bancario 60GG", "Pagamento con bonifico bancario entro 60 giorni", PaymentDateType.IMMEDIATE, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Bonifico Bancario 90GG", "Pagamento con bonifico bancario entro 90 giorni", PaymentDateType.IMMEDIATE, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Bonifico Bancario 30GG d.f. f.m.", "Pagamento con bonifico bancario entro 30 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Bonifico Bancario 60GG d.f. f.m.", "Pagamento con bonifico bancario entro 60 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Bonifico Bancario 90GG d.f. f.m.", "Pagamento con bonifico bancario entro 90 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Ri.Ba. 30GG", "Pagamento tramite ricevuta bancaria entro 30 giorni", PaymentDateType.IMMEDIATE, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Ri.Ba. 60GG", "Pagamento tramite ricevuta bancaria entro 60 giorni", PaymentDateType.IMMEDIATE, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Ri.Ba. 90GG", "Pagamento tramite ricevuta bancaria entro 90 giorni", PaymentDateType.IMMEDIATE, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Ri.Ba. 30GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 30 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Ri.Ba. 60GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 60 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType("Ri.Ba. 90GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 90 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0)
			};
	
	private void setPrivateFieldForRegistration(Registration target, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		java.lang.reflect.Field field = EmailPasswordHolder.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
		
	}
	
	private Principal createPrincipal() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		Registration registration = new Registration();
		registration.setEmail("info@manufattiblm.it");
		setPrivateFieldForRegistration(registration, "password", "30a385db4f43cff8fdcc3f00633fd294a4c1a1a42eab56e721f242b12d018309"); //avoid password hashing
		Principal principal = new Principal(registration);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		principal.setPassword("m4tte0:590");
		return principal.merge();
	}
	
	private Business createBusiness(Principal principal) throws com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		Business business = new Business();
		for(PaymentType pType: paymentTypes){
			PaymentType paymentType = null;
			try {
				paymentType = pType.clone();
			} catch (CloneNotSupportedException e) {
				throw new com.novadart.novabill.shared.client.exception.CloneNotSupportedException();
			}
			paymentType.setBusiness(business);
			business.getPaymentTypes().add(paymentType);
		}
		business.setName("BLM MANUFATTI IN CEMENTO di Bruseghin Matteo");
		business.setAddress("Via Leonardo da Vinci 39");
		business.setCity("Campo San Martino");
		business.setPostcode("35010");
		business.setProvince("PD");
		business.getSettings().setDefaultLayoutType(LayoutType.DENSE);
		business.setVatID("IT03971280288");
		business.setSsn("BRSMTT75P10B563Y");
		business.setCountry("IT");
		business.getPrincipals().add(principal);
		principal.setBusiness(business);
		PriceList publicPriceList = new PriceList(PriceListConstants.DEFAULT);
		publicPriceList.setBusiness(business);
		business.getPriceLists().add(publicPriceList);
		return business.merge();
	}
	
	private boolean containsAlphanumericChar(String s){
		for(char ch:s.toCharArray())
			if(Character.isAlphabetic(ch))
				return true;
		return false;
	}
	
//	private <T> T safeGet(Row r, String column, T defaultVal){
//		Object res = r.get(column);
//		return res == null? defaultVal: (T)res;
//	}
	
//	private Set<Client> createClients(Business business) throws IOException{
//		Set<Client> faultyClients = new HashSet<>();
//		PriceList defaultPL = business.getPriceLists().iterator().next();
//		Table table = DatabaseBuilder.open(new File(blmDBPath)).getTable("CLIENTI");
//		for(Row row: table){
//			
////			Integer id = safeGet(row, "CodCli", -1);
////			System.out.println(safeGet(row, "CodCli", -1l));
//			
//			Client client = new Client();
//			client.setName(safeGet(row, "RagSoc", "")); //Name
//			String[] addressTkns = safeGet(row, "Indirizzo", "").split("\r\n");
//			client.setAddress(addressTkns[0]); // Address
//			if(addressTkns.length > 1){
//				addressTkns = addressTkns[addressTkns.length - 1].split(" ");
//				client.setPostcode(addressTkns[0]);
//				client.setCity(Joiner.on(" ").join(Arrays.copyOfRange(addressTkns, 1, addressTkns.length - 1)).trim()); //City
//				String provinceToken = addressTkns[addressTkns.length - 1];
//				String provinceStr = provinceToken.substring(1, provinceToken.length() - 1).toUpperCase();
//				if(provinceStr.length() > 2) //Province
//					client.setProvince("XX");
//				else
//					client.setProvince(provinceStr);
//				//System.out.println(provinceStr);
//			}
//			if(StringUtils.isEmpty(client.getCity())){
//				client.setCity("BOGUS CITY");
//				faultyClients.add(client);
//			}
//			if(StringUtils.isEmpty(client.getProvince())){
//				client.setProvince("XX");
//				faultyClients.add(client);
//			}
//				
//			String piva = safeGet(row, "CF_PIVA", ""); //Vat and Ssn
//			if(piva.contains("-")){
//				String[] pivaTkns = piva.split("-");
//				client.setVatID("IT" + Strings.nullToEmpty(pivaTkns[0]));
//				client.setSsn(Joiner.on("").join(Strings.nullToEmpty(pivaTkns[1]).split(" ")));
//			}else{
//				String nPiva = Strings.nullToEmpty(Joiner.on("").join(piva.split(" ")));
//				if(Strings.isNullOrEmpty(nPiva)){
//					client.setVatID(Strings.padEnd("IT", 13, '0'));
//					faultyClients.add(client);
//				}else if(StringUtils.isNumeric(nPiva))
//					client.setVatID("IT" + nPiva);
//				else
//					client.setSsn(nPiva);
//			}
//			String nphoneFax = Strings.nullToEmpty(safeGet(row, "Tel_Fax", ""));
//			if(!Strings.isNullOrEmpty(nphoneFax) && !containsAlphanumericChar(nphoneFax)){
//				if(nphoneFax.contains("-"))
//					client.setPhone(nphoneFax.split("-")[0].trim());
//				else
//					client.setPhone(nphoneFax);
//			}
//				
//			
//			client.setCountry("IT");
//			
//			client.getContact().setEmail("");
//			
//			//System.out.println(client.getPhone());
//			
//			client.setBusiness(business);
//			business.getClients().add(client);
//			client.setDefaultPriceList(defaultPL);
//			defaultPL.getClients().add(client);
//		}
//		return faultyClients;
//	}
//	
//	private void createCommodities(Business business) throws IOException{
//		Map<String, Commodity> comMap = new HashMap<>();
//		Table table = DatabaseBuilder.open(new File(blmDBPath)).getTable("ARTICOLI");
//		for(Row row: table){
//			Commodity commodity = new Commodity();
//			String sku = safeGet(row, "CodArt", "");
//			commodity.setSku(sku);
//			String descr = safeGet(row, "Descr", "");
//			String dim = safeGet(row, "Dim", "");
//			commodity.setService(false);
//			commodity.setUnitOfMeasure("pz.");
//			commodity.setDescription(descr + ". Dimensioni: " + dim);
//			commodity.setTax(new BigDecimal("22.00"));
//			commodity.setBusiness(business);
//			business.getCommodities().add(commodity);
//			comMap.put(sku, commodity);
//		}
//		table = DatabaseBuilder.open(new File(blmDBPath)).getTable("RIGHE_LIST");
//		PriceList defaultPL = business.getPriceLists().iterator().next();
//		Set<String> skus = new HashSet<>();
//		for(Row row:table){
//			Integer id = safeGet(row, "CodListino", -1);
//			if(id == 1){
//				String sku = safeGet(row, "CodArt", "");
//				Commodity commodity = comMap.get(sku);
//				BigDecimal prezzo = safeGet(row, "Prezzo", new BigDecimal("0.00"));
//				Price price = new Price();
//				price.setPriceValue(prezzo);
//				price.setPriceType(PriceType.FIXED);
//				price.setCommodity(commodity);
//				commodity.getPrices().add(price);
//				price.setPriceList(defaultPL);
//				defaultPL.getPrices().add(price);
//				skus.add(sku);
//			}
//		}
//		for(String sku: comMap.keySet()){
//			if(!skus.contains(sku)){
//				Price price = new Price();
//				price.setPriceValue(new BigDecimal("0.0"));
//				price.setPriceType(PriceType.FIXED);
//				Commodity commodity = comMap.get(sku); 
//				price.setCommodity(commodity);
//				commodity.getPrices().add(price);
//				price.setPriceList(defaultPL);
//				defaultPL.getPrices().add(price);
//			}
//		}
//	}
	
	private void createDefaultPriceListForExistingBusinesses(){
		for(Business business: Business.findAllBusinesses()){
			if(business.getPrincipals().iterator().next().getUsername().equals("info@manufattiblm.it"))
				continue;
			PriceList publicPriceList = new PriceList(PriceListConstants.DEFAULT);
			publicPriceList.setBusiness(business);
			business.getPriceLists().add(publicPriceList);
			publicPriceList.persist();
			PriceList.entityManager().flush();
			for(Client client:business.getClients()){
				client.setDefaultPriceList(publicPriceList);
				publicPriceList.getClients().add(client);
			}
		}
	}
	
	public void createSecondBusiness() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		Registration registration = new Registration();
		registration.setEmail("info@prmanufatti.it");
		setPrivateFieldForRegistration(registration, "password", "64c7dba27c3e6d28957471136cc637b87aa10148fe19178f2010cb276df5aba4"); //avoid password hashing
		Principal principal = new Principal(registration);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		principal.setPassword("p3rr3:12oi");
		Business business = new Business();
		for(PaymentType pType: paymentTypes){
			PaymentType paymentType = null;
			try {
				paymentType = pType.clone();
			} catch (CloneNotSupportedException e) {
				throw new com.novadart.novabill.shared.client.exception.CloneNotSupportedException();
			}
			paymentType.setBusiness(business);
			business.getPaymentTypes().add(paymentType);
		}
		business.setName("P.R. Manufatti in Cemento");
		business.setAddress("Via Levà, 85");
		business.setCity("Conselve ");
		business.setPostcode("35026");
		business.setProvince("PD");
		business.getSettings().setDefaultLayoutType(LayoutType.DENSE);
		business.setVatID("IT04254930284");
		business.setCountry("IT");
		business.getPrincipals().add(principal);
		principal.setBusiness(business);
		business.persist();
		
	}
	
	private void createThirdBusiness() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		Registration registration = new Registration();
		registration.setEmail("info@cyclostore.it");
		setPrivateFieldForRegistration(registration, "password", "b02801478c8fa876cb8e487f78ccd20b0ac24e321e00380cfef6f6804d62cb05"); //avoid password hashing
		Principal principal = new Principal(registration);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		principal.setPassword("cyclo2014");
		Business business = new Business();
		for(PaymentType pType: paymentTypes){
			PaymentType paymentType = null;
			try {
				paymentType = pType.clone();
			} catch (CloneNotSupportedException e) {
				throw new com.novadart.novabill.shared.client.exception.CloneNotSupportedException();
			}
			paymentType.setBusiness(business);
			business.getPaymentTypes().add(paymentType);
		}
		business.setName("CYCLOSTORE");
		business.setAddress("Via Busiago, 69");
		business.setCity("Campo San Martino");
		business.setPostcode("35010");
		business.setProvince("PD");
		business.getSettings().setDefaultLayoutType(LayoutType.DENSE);
		business.setVatID("IT04782700282");
		business.setSsn("PTTRSE87B03C743C");
		business.setCountry("IT");
		business.getPrincipals().add(principal);
		principal.setBusiness(business);
		PriceList publicPriceList = new PriceList(PriceListConstants.DEFAULT);
		publicPriceList.setBusiness(business);
		business.getPriceLists().add(publicPriceList);
		business.persist();
	}
	
	private void fixPrices(){
		for(Price price: Price.findAllPrices()){
			if(PriceType.DISCOUNT_PERCENT.equals(price.getPriceType()))
				price.setPriceType(PriceType.FIXED);
			else if(PriceType.FIXED.equals(price.getPriceType())){
				if(price.getPriceValue().compareTo(BigDecimal.ZERO) == -1){ //less than zero
					price.setPriceType(PriceType.DISCOUNT_PERCENT);
					price.setPriceValue(price.getPriceValue().negate());
				}else
					price.setPriceType(PriceType.OVERCHARGE_PERCENT);
			}
		}
	}
	
	private void fixPaymentTypes(){
		for(PaymentType pType: PaymentType.findAllPaymentTypes()){
			switch (pType.getPaymentDateGenerator()) {
			case END_OF_MONTH:
			case IMMEDIATE:
				pType.setPaymentDeltaType(PaymentDeltaType.COMMERCIAL_MONTH);
				break;

			default:
				break;
			}
		}
	}
	
	
	private void fixInvoices(){
		for(Invoice invoice: Invoice.findAllInvoices()){
			switch (invoice.getPaymentDateGenerator()) {
			case END_OF_MONTH:
				invoice.setSecondaryPaymentDateDelta(0);
			case IMMEDIATE:
				invoice.setPaymentDeltaType(PaymentDeltaType.COMMERCIAL_MONTH);
				break;

			default:
				break;
			}
		}
	}
	
	private void fixInvoiceCreatedFromTransDocFlag(){
		for(Invoice invoice: Invoice.findAllInvoices()){
			if(invoice.getTransportDocuments().size() > 0)
				invoice.setCreatedFromTransportDocuments(true);
		}
	}
	
	private void copyToEndpoint(Endpoint endpoint, Client client){
		endpoint.setCompanyName(client.getName());
		endpoint.setStreet(client.getAddress());
		endpoint.setPostcode(client.getPostcode());
		endpoint.setCity(client.getCity());
		endpoint.setProvince(client.getProvince());
		endpoint.setCountry(client.getCountry());
	}
	
	private void migrate2_5(){
		em.createNativeQuery("update accounting_document set " +
				"to_company_name = td.to_company_name, " +
				"to_street = td.to_street, " +
				"to_postcode = td.to_postcode, " +
				"to_city = td.to_city, " +
				"to_province = td.to_province, " +
				"to_country = td.to_country " +
				"from (select * from transport_document) as td " +
				"where accounting_document.id = td.id").executeUpdate();
		for(Invoice invoice: Invoice.findAllInvoices()){
			if(invoice.getToEndpoint() == null)
				invoice.setToEndpoint(new Endpoint());
			copyToEndpoint(invoice.getToEndpoint(), invoice.getClient());
		}
		for(Estimation estimation: Estimation.findAllEstimations()){
			if(estimation.getToEndpoint() == null)
				estimation.setToEndpoint(new Endpoint());
			copyToEndpoint(estimation.getToEndpoint(), estimation.getClient());
		}
		for(CreditNote creditNote: CreditNote.findAllCreditNotes()){
			if(creditNote.getToEndpoint() == null)
				creditNote.setToEndpoint(new Endpoint());
			copyToEndpoint(creditNote.getToEndpoint(), creditNote.getClient());
		}
		fixInvoiceCreatedFromTransDocFlag();
		em.createNativeQuery("alter table transport_document " +
				"drop column to_company_name, " +
				"drop column to_street, " +
				"drop column to_postcode, " +
				"drop column to_city, " +
				"drop column to_province, " +
				"drop column to_country").executeUpdate();
	}
	
	@Scheduled(fixedDelay = 31_536_000_730l)
	@Transactional(readOnly = false)
	public void run() throws com.novadart.novabill.shared.client.exception.CloneNotSupportedException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException{
//		Principal principal = createPrincipal();
//		Business business = createBusiness(principal);
//		Set<Client> faultyClients = createClients(business);
//		createCommodities(business);
//		Client.entityManager().flush();
//		for(Client client: faultyClients)
//			System.out.println("Faulty Client id:" + client.getId() + " name:" + client.getName());
		//createSecondBusiness();
		//createThirdBusiness();
		//createDefaultPriceListForExistingBusinesses();
//		fixPrices();
		//fixPaymentTypes();
		//fixInvoices();
		migrate2_5();
	}
	
}
