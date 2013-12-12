package com.novadart.novabill.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.EmailPasswordHolder;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.PaymentDateType;


@Service
public class BLMImportService {
	
	private String blmDBPath = "/tmp/DATI.mdb";
	
	private PaymentType[] paymentTypes = new PaymentType[]{
			new PaymentType("Rimessa Diretta", "Pagamento in Rimessa Diretta", PaymentDateType.IMMEDIATE, 0),
			new PaymentType("Bonifico Bancario 30GG", "Pagamento con bonifico bancario entro 30 giorni", PaymentDateType.IMMEDIATE, 1),
			new PaymentType("Bonifico Bancario 60GG", "Pagamento con bonifico bancario entro 60 giorni", PaymentDateType.IMMEDIATE, 2),
			new PaymentType("Bonifico Bancario 90GG", "Pagamento con bonifico bancario entro 90 giorni", PaymentDateType.IMMEDIATE, 3),
			new PaymentType("Bonifico Bancario 30GG d.f. f.m.", "Pagamento con bonifico bancario entro 30 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 1),
			new PaymentType("Bonifico Bancario 60GG d.f. f.m.", "Pagamento con bonifico bancario entro 60 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 2),
			new PaymentType("Bonifico Bancario 90GG d.f. f.m.", "Pagamento con bonifico bancario entro 90 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 3),
			new PaymentType("Ri.Ba. 30GG", "Pagamento tramite ricevuta bancaria entro 30 giorni", PaymentDateType.IMMEDIATE, 1),
			new PaymentType("Ri.Ba. 60GG", "Pagamento tramite ricevuta bancaria entro 60 giorni", PaymentDateType.IMMEDIATE, 2),
			new PaymentType("Ri.Ba. 90GG", "Pagamento tramite ricevuta bancaria entro 90 giorni", PaymentDateType.IMMEDIATE, 3),
			new PaymentType("Ri.Ba. 30GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 30 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 1),
			new PaymentType("Ri.Ba. 60GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 60 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 2),
			new PaymentType("Ri.Ba. 90GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 90 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 3)
			};
	
	private void setPrivateFieldForRegistration(Registration target, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		java.lang.reflect.Field field = EmailPasswordHolder.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
		
	}
	
	private Principal createPrincipal() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		Registration registration = new Registration();
		registration.setEmail("manufattiblm@gmail.com");
		setPrivateFieldForRegistration(registration, "password", "17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187"); //avoid password hashing
		Principal principal = new Principal(registration);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		principal.setPassword("novadart");
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
		business.setName("Blm Di Bruseghin Matteo");
		business.setAddress("Via Leonardo da Vinci 39");
		business.setCity("Campo San Martino");
		business.setPostcode("35010");
		business.setProvince("PD");
		business.setVatID("IT03971280288");
		business.setSsn("BRSMTT75P10B563Y");
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
	
	private <T> T safeGet(Row r, String column, T defaultVal){
		Object res = r.get(column);
		return res == null? defaultVal: (T)res;
	}
	
	private void createClients(Business business) throws IOException{
		Table table = DatabaseBuilder.open(new File(blmDBPath)).getTable("CLIENTI");
		for(Row row: table){
			
			Integer id = safeGet(row, "CodCli", -1);
			System.out.println(safeGet(row, "CodCli", -1l));
			
			Client client = new Client();
			client.setName(safeGet(row, "RagSoc", "")); //Name
			String[] addressTkns = safeGet(row, "Indirizzo", "").split("\r\n");
			client.setAddress(addressTkns[0]); // Address
			if(addressTkns.length > 1){
				addressTkns = addressTkns[addressTkns.length - 1].split(" ");
				client.setPostcode(addressTkns[0]);
				client.setCity(Joiner.on(" ").join(Arrays.copyOfRange(addressTkns, 1, addressTkns.length - 1)).trim()); //City
				String provinceToken = addressTkns[addressTkns.length - 1];
				String provinceStr = provinceToken.substring(1, provinceToken.length() - 1).toUpperCase();
				if(provinceStr.length() > 2) //Province
					client.setProvince("XX");
				else
					client.setProvince(provinceStr);
				System.out.println(provinceStr);
			}
			if(StringUtils.isEmpty(client.getCity()))
				client.setCity("BOGUS CITY");
			if(StringUtils.isEmpty(client.getProvince()))
				client.setProvince("XX");
				
			String piva = safeGet(row, "CF_PIVA", ""); //Vat and Ssn
			if(piva.contains("-")){
				String[] pivaTkns = piva.split("-");
				client.setVatID("IT" + Strings.nullToEmpty(pivaTkns[0]));
				client.setSsn(Joiner.on("").join(Strings.nullToEmpty(pivaTkns[1]).split(" ")));
			}else{
				String nPiva = Strings.nullToEmpty(piva);
				if(Strings.isNullOrEmpty(nPiva))
					client.setVatID(Strings.padEnd("IT", 13, '0'));
				else
					client.setSsn(Joiner.on("").join(nPiva.split(" ")));
			}
			String nphoneFax = Strings.nullToEmpty(safeGet(row, "Tel_Fax", ""));
			if(!Strings.isNullOrEmpty(nphoneFax) && !containsAlphanumericChar(nphoneFax)){
				if(nphoneFax.contains("-"))
					client.setPhone(nphoneFax.split("-")[0].trim());
				else
					client.setPhone(nphoneFax);
			}
				
			
			client.setCountry("IT");
			
			client.getContact().setEmail("");
			
			System.out.println(client.getPhone());
			
			client.setBusiness(business);
			business.getClients().add(client);
		}
	}
	
	private void createCommodities(Business business) throws IOException{
		Map<String, Commodity> comMap = new HashMap<>();
		Table table = DatabaseBuilder.open(new File(blmDBPath)).getTable("ARTICOLI");
		for(Row row: table){
			Commodity commodity = new Commodity();
			String sku = safeGet(row, "CodArt", "");
			commodity.setSku(sku);
			String descr = safeGet(row, "Descr", "");
			String dim = safeGet(row, "Dim", "");
			commodity.setService(false);
			commodity.setUnitOfMeasure("Unità");
			commodity.setDescription(descr + ". Dimensioni: " + dim);
			commodity.setBusiness(business);
			business.getCommodities().add(commodity);
			comMap.put(sku, commodity);
		}
		table = DatabaseBuilder.open(new File(blmDBPath)).getTable("RIGHE_LIST");
		PriceList defaultPL = business.getPriceLists().iterator().next();
		Set<String> skus = new HashSet<>();
		for(Row row:table){
			Integer id = safeGet(row, "CodListino", -1);
			if(id == 1){
				String sku = safeGet(row, "CodArt", "");
				Commodity commodity = comMap.get(sku);
				BigDecimal prezzo = safeGet(row, "Prezzo", new BigDecimal("0.00"));
				Price price = new Price();
				price.setPriceValue(prezzo);
				price.setPriceType(PriceType.FIXED);
				price.setCommodity(commodity);
				commodity.getPrices().add(price);
				price.setPriceList(defaultPL);
				defaultPL.getPrices().add(price);
				skus.add(sku);
			}
		}
		for(String sku: comMap.keySet()){
			if(!skus.contains(sku)){
				Price price = new Price();
				price.setPriceValue(new BigDecimal("0.0"));
				price.setPriceType(PriceType.FIXED);
				Commodity commodity = comMap.get(sku); 
				price.setCommodity(commodity);
				commodity.getPrices().add(price);
				price.setPriceList(defaultPL);
				defaultPL.getPrices().add(price);
			}
		}
	}
	
	@Scheduled(fixedDelay = 31_536_000_730l)
	@Transactional(readOnly = false)
	public void run() throws com.novadart.novabill.shared.client.exception.CloneNotSupportedException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException{
		Principal principal = createPrincipal();
		Business business = createBusiness(principal);
		createClients(business);
		createCommodities(business);
	}
	
}
