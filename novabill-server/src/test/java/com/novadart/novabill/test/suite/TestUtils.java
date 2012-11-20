package com.novadart.novabill.test.suite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.novadart.novabill.domain.AbstractInvoice;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.shared.client.dto.AbstractInvoiceDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.validation.Field;

public class TestUtils {
	
	@SuppressWarnings("serial")
	public static Map<String, Field> abstractInvoiceValidationFieldsMap = new HashMap<String, Field>(){{
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
		
		//Invoice
		put("paymentType", Field.paymentType); put("paymentDueDate", Field.paymentDueDate); put("payed", Field.payed);
	}};
	
	public static interface Comparator{
		public boolean equal(AccountingDocumentDTO doc1, AccountingDocumentDTO doc2);
	}
	 
	public static Comparator abstractInvoicesComparator = new Comparator() {
		@Override
		public boolean equal(AccountingDocumentDTO doc1, AccountingDocumentDTO doc2) {
			AbstractInvoiceDTO lhs = (AbstractInvoiceDTO)doc1, rhs = (AbstractInvoiceDTO)doc2;
			if(lhs.getItems().size() != rhs.getItems().size())
				return false;
			boolean itemsEqual = true;
			for(int i = 0; i < lhs.getItems().size(); ++i){
				if(!EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), false)){
					itemsEqual = false;
					break;
				}
			}
			return EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business") && itemsEqual;
		}
	};
	
	public static Comparator abstractInvoicesComparatorIgnoreID = new Comparator() {
		@Override
		public boolean equal(AccountingDocumentDTO doc1, AccountingDocumentDTO doc2) {
			AbstractInvoiceDTO lhs = (AbstractInvoiceDTO)doc1, rhs = (AbstractInvoiceDTO)doc2;
			if(lhs.getItems().size() != rhs.getItems().size())
				return false;
			boolean itemsEqual = true;
			for(int i = 0; i < lhs.getItems().size(); ++i){
				if(!EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), "id")){
					itemsEqual = false;
					break;
				}
			}
			return EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business", "id") && itemsEqual;
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
	
	/**
	 *	Conversion 
	 */
	
	public static List<AccountingDocumentDTO> toDTOList(List<AccountingDocument> docs, ToDTOConverter converter){
		List<AccountingDocumentDTO> docDTOs = new ArrayList<AccountingDocumentDTO>();
		for(AccountingDocument doc: docs)
			docDTOs.add(converter.toDTO(doc));
		return docDTOs;
	}
	
	public interface ToDTOConverter{
		public AccountingDocumentDTO toDTO(AccountingDocument doc);
	}
	
	public static ToDTOConverter invoiceDTOConverter = new ToDTOConverter(){
		@Override
		public AccountingDocumentDTO toDTO(AccountingDocument doc){
			return InvoiceDTOFactory.toDTO((Invoice)doc);
		}
	};
	
	public static ToDTOConverter creditNoteDTOConverter = new ToDTOConverter(){
		@Override
		public AccountingDocumentDTO toDTO(AccountingDocument doc){
			return CreditNoteDTOFactory.toDTO((CreditNote)doc);
		}
	};
	
	public static <T extends AbstractInvoice> T createDoc(Long documentID, Class<T> cls) throws InstantiationException, IllegalAccessException{
		T doc = cls.newInstance();
		doc.setAccountingDocumentDate(new Date());
		doc.setDocumentID(documentID);
		doc.setNote("");
		doc.setPaymentNote("");
		doc.setTotal(new BigDecimal("121.0"));
		doc.setTotalBeforeTax(new BigDecimal("100.0"));
		doc.setTotalTax(new BigDecimal("21.0"));
		doc.setPayed(false);
		doc.setPaymentDueDate(new Date());
		doc.setPaymentType(PaymentType.CASH);
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
	
	public static <T extends AbstractInvoice> T createInvalidDoc(Long documentID, Class<T> cls) throws InstantiationException, IllegalAccessException{
		T doc = cls.newInstance();
		doc.setAccountingDocumentDate(new Date());
		doc.setDocumentID(documentID);
		doc.setNote(StringUtils.leftPad("1", 2000, '1'));
		doc.setPaymentNote(StringUtils.leftPad("1", 2000, '1'));
		doc.setTotal(null);
		doc.setTotalBeforeTax(null);
		doc.setTotalTax(null);
		doc.setPayed(false);
		doc.setPaymentDueDate(null);
		doc.setPaymentType(null);
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
	
}
