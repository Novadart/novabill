package com.novadart.novabill.test.suite;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.novadart.novabill.domain.AbstractInvoice;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Endpoint;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.validation.Field;

public class TestUtils {
	
	public static Map<String, Field> accountingDocumentValidationFieldsMap; 
	
	public static Map<String, Field> abstractInvoiceValidationFieldsMap;
	
	public static Map<String, Field> estimationValidationFieldsMap;
	
	public static Map<String, Field> transportDocValidationFieldsMap;
	
	static{
		accountingDocumentValidationFieldsMap = new HashMap<String, Field>(){{
			//Accounting doc
			put("documentID", Field.documentID); put("accountingDocumentDate", Field.accountingDocumentDate);
			put("accountingDocumentYear", Field.accountingDocumentYear); put("note", Field.note); put("paymentNote", Field.paymentNote);
			put("total", Field.total); put("totalTax", Field.totalTax); put("totalBeforeTax", Field.totalBeforeTax);
			
			//Accounting doc item
			put("accountingDocumentItems_description", Field.accountingDocumentItems_description); 
			put("accountingDocumentItems_unitOfMeasure", Field.accountingDocumentItems_unitOfMeasure); 
			put("accountingDocumentItems_tax", Field.accountingDocumentItems_tax);
			put("accountingDocumentItems_quantity", Field.accountingDocumentItems_quantity);
			put("accountingDocumentItems_totalBeforeTax", Field.accountingDocumentItems_totalBeforeTax);
			put("accountingDocumentItems_totalTax", Field.accountingDocumentItems_totalTax);
			put("accountingDocumentItems_total", Field.accountingDocumentItems_total);
			put("accountingDocumentItems_price", Field.accountingDocumentItems_price);
		}};
		
		abstractInvoiceValidationFieldsMap = new HashMap<String, Field>(accountingDocumentValidationFieldsMap);
		abstractInvoiceValidationFieldsMap.putAll(new HashMap<String, Field>(){{
			put("paymentType", Field.paymentType); put("paymentDueDate", Field.paymentDueDate); put("payed", Field.payed);
		}});
		
		estimationValidationFieldsMap = new HashMap<String, Field>(accountingDocumentValidationFieldsMap);
		estimationValidationFieldsMap.putAll(new HashMap<String, Field>(){{
			put("limitations", Field.limitations); put("validTill", Field.validTill);
		}});
		
		transportDocValidationFieldsMap = new HashMap<String, Field>(accountingDocumentValidationFieldsMap);
		transportDocValidationFieldsMap.putAll(new HashMap<String, Field>(){{
			put("fromEndpoint_companyName", Field.fromEndpoint_companyName);
			put("fromEndpoint_street", Field.fromEndpoint_street);
			put("fromEndpoint_postcode", Field.fromEndpoint_postcode);
			put("fromEndpoint_city", Field.fromEndpoint_city);
			put("fromEndpoint_province", Field.fromEndpoint_province);
			put("fromEndpoint_country", Field.fromEndpoint_country);
			put("toEndpoint_companyName", Field.toEndpoint_companyName);
			put("toEndpoint_street", Field.toEndpoint_street);
			put("toEndpoint_postcode", Field.toEndpoint_postcode);
			put("toEndpoint_city", Field.toEndpoint_city);
			put("toEndpoint_province", Field.toEndpoint_province);
			put("toEndpoint_country", Field.toEndpoint_country);
		}});
	}
	
	
	public static interface Comparator{
		public boolean equal(AccountingDocumentDTO doc1, AccountingDocumentDTO doc2);
	}
	
	private static boolean compareItems(AccountingDocumentDTO lhs, AccountingDocumentDTO rhs, boolean ignoreID){
		if(lhs.getItems().size() != rhs.getItems().size())
			return false;
		for(int i = 0; i < lhs.getItems().size(); ++i){
			if(!ignoreID && !EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), false))
				return false;
			if(ignoreID && !EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), "id"))
				return false;
		}
		return true;
	}
	 
	public static Comparator accountingDocumentComparator = new Comparator() {
		@Override
		public boolean equal(AccountingDocumentDTO lhs, AccountingDocumentDTO rhs) {
			boolean itemsEqual = compareItems(lhs, rhs, false);
			return EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business") && itemsEqual;
		}
	};
	
	public static Comparator accountingDocumentComparatorIgnoreID = new Comparator() {
		@Override
		public boolean equal(AccountingDocumentDTO lhs, AccountingDocumentDTO rhs) {
			boolean itemsEqual = compareItems(lhs, rhs, true);
			return EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business", "id") && itemsEqual;
		}
	};
	
	public static Comparator transportDocumentComparator = new Comparator() {
		@Override
		public boolean equal(AccountingDocumentDTO doc1, AccountingDocumentDTO doc2) {
			TransportDocumentDTO lhs = (TransportDocumentDTO)doc1, rhs = (TransportDocumentDTO)doc2;
			boolean itemsEqual = compareItems(lhs, rhs, false);
			return EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business", "fromEndpoint", "toEndpoint") &&
					EqualsBuilder.reflectionEquals(lhs.getFromEndpoint(), rhs.getFromEndpoint(), false) &&
					EqualsBuilder.reflectionEquals(lhs.getToEndpoint(), rhs.getToEndpoint(), false) && itemsEqual;
		}
	};
	
	public static Comparator transportDocumentComparatorIgnoreID = new Comparator() {
		@Override
		public boolean equal(AccountingDocumentDTO doc1, AccountingDocumentDTO doc2) {
			TransportDocumentDTO lhs = (TransportDocumentDTO)doc1, rhs = (TransportDocumentDTO)doc2;
			boolean itemsEqual = compareItems(lhs, rhs, true);
			return EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business", "fromEndpoint", "toEndpoint", "id") &&
					EqualsBuilder.reflectionEquals(lhs.getFromEndpoint(), rhs.getFromEndpoint(), false) &&
					EqualsBuilder.reflectionEquals(lhs.getToEndpoint(), rhs.getToEndpoint(), false) && itemsEqual;
		}
	};
	
	public static boolean equal(List<AccountingDocumentDTO> lhs, List<AccountingDocumentDTO> rhs, Comparator comparator){
		boolean contained = true;
		outer: for(AccountingDocumentDTO idto1: lhs){
			for(AccountingDocumentDTO idto2: rhs)
				if(comparator.equal(idto1, idto2))
					continue outer;
			contained = false;
			break outer;
		}
		return contained && lhs.size() == rhs.size();
	}
	
	
	public static <T extends AccountingDocument> T createDoc(Long documentID, Class<T> cls) throws InstantiationException, IllegalAccessException{
		T doc = cls.newInstance();
		doc.setAccountingDocumentDate(new Date());
		doc.setDocumentID(documentID);
		doc.setNote("");
		doc.setPaymentNote("");
		doc.setTotal(new BigDecimal("121.0"));
		doc.setTotalBeforeTax(new BigDecimal("100.0"));
		doc.setTotalTax(new BigDecimal("21.0"));
		AccountingDocumentItem item = new AccountingDocumentItem();
		item.setDescription("description");
		item.setPrice(new BigDecimal("100.0"));
		item.setQuantity(new BigDecimal("1.0"));
		item.setTax(new BigDecimal("21.0"));
		item.setTotal(new BigDecimal("121.0"));
		item.setTotalBeforeTax(new BigDecimal("100.0"));
		item.setTotalTax(new BigDecimal("21.0"));
		item.setUnitOfMeasure("piece");
		item.setAccountingDocument(doc);
		doc.getAccountingDocumentItems().add(item);
		return doc;
	}
	
	public static <T extends AbstractInvoice> T createInvOrCredNote(Long documentID, Class<T> cls) throws InstantiationException, IllegalAccessException{
		T doc = createDoc(documentID, cls);
		doc.setPayed(false);
		doc.setPaymentDueDate(new Date());
		doc.setPaymentType(PaymentType.CASH);
		return doc;
	}
	
	public static Estimation createEstimation(Long documentID) throws InstantiationException, IllegalAccessException{
		Estimation doc = createDoc(documentID, Estimation.class);
		doc.setLimitations("");
		doc.setValidTill(new Date());
		return doc;
	}
	
	public static TransportDocument createTransportDocument(Long documentID) throws InstantiationException, IllegalAccessException{
		TransportDocument doc = createDoc(documentID, TransportDocument.class);
		Endpoint toEndpoint = new Endpoint();
		toEndpoint.setCompanyName("The mighty company from this Young Entrepreneur");
		toEndpoint.setStreet("via Qualche Strada con Nome Lungo, 12");
		toEndpoint.setCity("Nervesa della Battaglia");
		toEndpoint.setPostcode("42837");
		toEndpoint.setProvince("PD");
		toEndpoint.setCountry("IT");
		doc.setToEndpoint(toEndpoint);
		Endpoint fromEndpoint = new Endpoint();
		fromEndpoint.setCompanyName("The mighty company from this Young Entrepreneur");
		fromEndpoint.setStreet("via Qualche Strada con Nome Lungo, 12");
		fromEndpoint.setCity("Nervesa della Battaglia");
		fromEndpoint.setPostcode("42837");
		fromEndpoint.setProvince("PD");
		fromEndpoint.setCountry("IT");
		doc.setFromEndpoint(fromEndpoint);
		return doc;
	}
	
	public static <T extends AccountingDocument> T createInvalidDoc(Long documentID, Class<T> cls) throws InstantiationException, IllegalAccessException{
		T doc = cls.newInstance();
		doc.setAccountingDocumentDate(new Date());
		doc.setDocumentID(documentID);
		doc.setNote(StringUtils.leftPad("1", 2000, '1'));
		doc.setPaymentNote(StringUtils.leftPad("1", 2000, '1'));
		doc.setTotal(null);
		doc.setTotalBeforeTax(null);
		doc.setTotalTax(null);
		AccountingDocumentItem item = new AccountingDocumentItem();
		item.setDescription(StringUtils.leftPad("1", 1000, '1'));
		item.setPrice(null);
		item.setQuantity(null);
		item.setTax(null);
		item.setTotal(null);
		item.setTotalBeforeTax(null);
		item.setTotalTax(null);
		item.setUnitOfMeasure(StringUtils.leftPad("1", 1000, '1'));
		item.setAccountingDocument(doc);
		doc.getAccountingDocumentItems().add(item);
		return doc;
	}
	
	public static <T extends AbstractInvoice> T createInvalidIngOrCredNote(Long documentID, Class<T> cls) throws InstantiationException, IllegalAccessException{
		T doc = createInvalidDoc(documentID, cls);
		doc.setPayed(false);
		doc.setPaymentDueDate(null);
		doc.setPaymentType(null);
		return doc;
	}
	
	public static Estimation createInvalidEstimation(Long documentID) throws InstantiationException, IllegalAccessException{
		Estimation doc = createInvalidDoc(documentID, Estimation.class);
		doc.setLimitations(StringUtils.leftPad("1", 2000, '1'));
		doc.setValidTill(new Date());
		return doc;
	}
	
	public static TransportDocument createInvalidTransportDocument(Long documentID) throws InstantiationException, IllegalAccessException{
		TransportDocument doc = createInvalidDoc(documentID, TransportDocument.class);
		Endpoint toEndpoint = new Endpoint();
		toEndpoint.setCompanyName(StringUtils.leftPad("1", 1000, '1'));
		toEndpoint.setStreet(StringUtils.leftPad("1", 1000, '1'));
		toEndpoint.setCity(StringUtils.leftPad("1", 1000, '1'));
		toEndpoint.setPostcode(StringUtils.leftPad("1", 1000, '1'));
		toEndpoint.setProvince(StringUtils.leftPad("1", 1000, '1'));
		toEndpoint.setCountry(StringUtils.leftPad("1", 1000, '1'));
		doc.setToEndpoint(toEndpoint);
		Endpoint fromEndpoint = new Endpoint();
		fromEndpoint.setCompanyName(StringUtils.leftPad("1", 1000, '1'));
		fromEndpoint.setStreet(StringUtils.leftPad("1", 1000, '1'));
		fromEndpoint.setCity(StringUtils.leftPad("1", 1000, '1'));
		fromEndpoint.setPostcode(StringUtils.leftPad("1", 1000, '1'));
		fromEndpoint.setProvince(StringUtils.leftPad("1", 1000, '1'));
		fromEndpoint.setCountry(StringUtils.leftPad("1", 1000, '1'));
		doc.setFromEndpoint(fromEndpoint);
		return doc;
	}
	
	public static Client createClient(){
		Client client = new Client();
		client.setName("The mighty company from this Young Entrepreneur");
		client.setAddress("via Qualche Strada con Nome Lungo, 12");
		client.setCity("Nervesa della Battaglia");
		client.setPostcode("42837");
		client.setProvince("PD");
		client.setCountry("IT");
		client.setVatID("IT04235756211");
		client.setEmail("");
		client.setFax("");
		client.setMobile("");
		client.setPhone("");
		client.setWeb("");
		client.setSsn("");
		return client;
	}
	
	public static <T1, T2 extends T1> void setPrivateField(Class<T1> cls, T2 target, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		java.lang.reflect.Field field = cls.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}
	
}
