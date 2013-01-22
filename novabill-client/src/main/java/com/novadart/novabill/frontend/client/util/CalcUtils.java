package com.novadart.novabill.frontend.client.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;

public class CalcUtils {
	
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
		
		CalcUtils.updateTotals(ii);
		return ii;
	}
	
	
	public static void updateTotals(AccountingDocumentItemDTO item){
		BigDecimal totBeforeTaxesForItem = CalcUtils.calculateTotalBeforeTaxesForItem(item);
		BigDecimal totTaxesForItem = CalcUtils.calculateTaxesForItem(item);
		item.setTotal(totBeforeTaxesForItem.add(totTaxesForItem));
		item.setTotalTax(totTaxesForItem);
		item.setTotalBeforeTax(totBeforeTaxesForItem);
	}
	
	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, Label totalTax, 
			Label totalBeforeTaxes, Label totalAfterTaxes){
		
		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (AccountingDocumentItemDTO item : accountingDocumentItems) {
			totBeforeTaxes = totBeforeTaxes.add(CalcUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(CalcUtils.calculateTaxesForItem(item));
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
			totBeforeTaxes = totBeforeTaxes.add(CalcUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(CalcUtils.calculateTaxesForItem(item));
		}
		BigDecimal totAfterTaxes = totBeforeTaxes.add(totTaxes);

		doc.setTotalBeforeTax(totBeforeTaxes);
		doc.setTotalTax(totTaxes);
		doc.setTotal(totAfterTaxes);
	}
	
	
	public static Date calculatePaymentDueDate(Date creation, PaymentType paymentType){
		
		switch(paymentType){
		case BANK_TRANSFER:
		case CASH:
		default:
			return creation;
			
		case BANK_TRANSFER_30:
		case RIBA_30:
		case RIBA_30_FM:
			return new Date(creation.getTime()+ONE_DAY_MS*30);
			
		case BANK_TRANSFER_60:
		case RIBA_60:
		case RIBA_60_FM:
			return new Date(creation.getTime()+ONE_DAY_MS*60);
		
		case RIBA_90:
		case RIBA_90_FM:
			return new Date(creation.getTime()+ONE_DAY_MS*90);
			
		case RIBA_120:
		case RIBA_120_FM:
			return new Date(creation.getTime()+ONE_DAY_MS*120);
		
		case RIBA_150:
		case RIBA_150_FM:
			return new Date(creation.getTime()+ONE_DAY_MS*150);
			
		case RIBA_180:
		case RIBA_180_FM:
			return new Date(creation.getTime()+ONE_DAY_MS*180);
		}
	}
	
}
