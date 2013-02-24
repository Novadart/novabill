package com.novadart.novabill.frontend.client.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class DocumentUtils {
	
	private static final long ONE_DAY_MS = 1000 * 60 * 60 * 24;
	
	public static BigDecimal parseValue(String value) throws NumberFormatException {
		return new BigDecimal( NumberFormat.getDecimalFormat().parse(value) );
	}
	
	public static BigDecimal parseCurrency(String value) throws NumberFormatException {
		try{
			//GWT requires a non breakable space between currency symbol and value
			return new BigDecimal( NumberFormat.getCurrencyFormat().parse(value.replace(" ", "\u00A0")) );
		} catch (NumberFormatException e) {
			return parseValue(value);
		}
	}
	
	
	public static BigDecimal calculateTaxesForItem(AccountingDocumentItemDTO item){
		return item.getPrice()
				.multiply(item.getQuantity())
				.multiply(item.getTax())
				.divide(new BigDecimal(100));
	}
	
	
	public static BigDecimal calculateTotalBeforeTaxesForItem(AccountingDocumentItemDTO item){
		return item.getPrice()
				.multiply(item.getQuantity());
	}
	
	
	public static BigDecimal calculateTotalAfterTaxesForItem(AccountingDocumentItemDTO item){
		return item.getPrice()
				.multiply(item.getQuantity())
				.multiply(item.getTax().add(new BigDecimal(100)))
				.divide(new BigDecimal(100));
	}
	
	
	public static AccountingDocumentItemDTO createAccountingDocumentItem(String description, String price, 
			String quantity, String unitOfMeasure, String tax){
		for (String txt : new String[]{description,quantity,price}) {
			if(txt.isEmpty()){
				return null;
			}
		}
		
		AccountingDocumentItemDTO ii = new AccountingDocumentItemDTO();

		try {
			ii.setDescription(description);
			ii.setPrice(parseCurrency(price));
			ii.setQuantity(parseValue(quantity));
			ii.setUnitOfMeasure(unitOfMeasure);
			ii.setTax(new BigDecimal(tax));
		} catch (NumberFormatException ex) {
			return null;
		}
		
		DocumentUtils.updateTotals(ii);
		return ii;
	}
	
	
	public static void updateTotals(AccountingDocumentItemDTO item){
		BigDecimal totBeforeTaxesForItem = DocumentUtils.calculateTotalBeforeTaxesForItem(item);
		BigDecimal totTaxesForItem = DocumentUtils.calculateTaxesForItem(item);
		item.setTotal(totBeforeTaxesForItem.add(totTaxesForItem));
		item.setTotalTax(totTaxesForItem);
		item.setTotalBeforeTax(totBeforeTaxesForItem);
	}
	
	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, Label totalTax, 
			Label totalBeforeTaxes, Label totalAfterTaxes){
		
		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (AccountingDocumentItemDTO item : accountingDocumentItems) {
			totBeforeTaxes = totBeforeTaxes.add(DocumentUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(DocumentUtils.calculateTaxesForItem(item));
		}
		BigDecimal totAfterTaxes = totBeforeTaxes.add(totTaxes);

		totalTax.setText(NumberFormat.getCurrencyFormat().format(totTaxes.doubleValue()));
		totalBeforeTaxes.setText(NumberFormat.getCurrencyFormat().format(totBeforeTaxes.doubleValue()));
		totalAfterTaxes.setText(NumberFormat.getCurrencyFormat().format(totAfterTaxes.doubleValue()));
	}
	
	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, AccountingDocumentDTO doc) {
		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (AccountingDocumentItemDTO item : accountingDocumentItems) {
			totBeforeTaxes = totBeforeTaxes.add(DocumentUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(DocumentUtils.calculateTaxesForItem(item));
		}
		BigDecimal totAfterTaxes = totBeforeTaxes.add(totTaxes);

		doc.setTotalBeforeTax(totBeforeTaxes);
		doc.setTotalTax(totTaxes);
		doc.setTotal(totAfterTaxes);
	}
	
	public static Date calculatePaymentDueDate(Date documentDate, PaymentTypeDTO payment){
		if(PaymentDateType.CUSTOM.equals(payment.getPaymentDateGenerator())){
			return null;
		}
		
		long delta = payment.getPaymentDateDelta() * ONE_DAY_MS;
		
		switch (payment.getPaymentDateGenerator()) {
		default:
		case CUSTOM:
			return null;
			
		case END_OF_MONTH:
			Date d = new Date(documentDate.getTime()+delta);
			CalendarUtil.setToFirstDayOfMonth(d);
			CalendarUtil.addMonthsToDate(d, 1);
			CalendarUtil.addDaysToDate(d, -1);
			return d;
			
		case IMMEDIATE:
			return new Date(documentDate.getTime()+delta);
		}
	}
}
