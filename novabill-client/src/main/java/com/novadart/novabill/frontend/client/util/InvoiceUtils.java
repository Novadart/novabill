package com.novadart.novabill.frontend.client.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;

public class InvoiceUtils {
	
	private static final long ONE_DAY_MS = 1000 * 60 * 60 * 24;
	
	public static BigDecimal calculateTaxesForItem(InvoiceItemDTO item){
		return item.getPrice()
				.multiply(item.getQuantity())
				.multiply(item.getTax())
				.divide(new BigDecimal(100));
	}
	
	
	public static BigDecimal calculateTotalBeforeTaxesForItem(InvoiceItemDTO item){
		return item.getPrice()
				.multiply(item.getQuantity());
	}
	
	
	public static BigDecimal calculateTotalAfterTaxesForItem(InvoiceItemDTO item){
		return item.getPrice()
				.multiply(item.getQuantity())
				.multiply(item.getTax().add(new BigDecimal(100)))
				.divide(new BigDecimal(100));
	}
	
	
	public static boolean isNumber(String value){
		try {
			Double.parseDouble(value);
			return true;
		} catch(NumberFormatException e){
			return false;
		}
	}
	
	public static InvoiceItemDTO createInvoiceItem(String description, String price, 
			String quantity, String unitOfMeasure, String tax){
		for (String txt : new String[]{description,quantity,price}) {
			if(txt.isEmpty()){
				return null;
			}
		}
		
		InvoiceItemDTO ii = new InvoiceItemDTO();
		double tmpVal;

		try {
			ii.setDescription(description);
			tmpVal = NumberFormat.getDecimalFormat().parse(price);
			ii.setPrice(new BigDecimal( tmpVal ) );
			tmpVal = NumberFormat.getDecimalFormat().parse(quantity);
			ii.setQuantity(new BigDecimal(tmpVal));
			ii.setUnitOfMeasure(unitOfMeasure);
			ii.setTax(new BigDecimal(tax));
		} catch (NumberFormatException ex) {
			return null;
		}
		BigDecimal totBeforeTaxesForItem = InvoiceUtils.calculateTotalBeforeTaxesForItem(ii);
		BigDecimal totTaxesForItem = InvoiceUtils.calculateTaxesForItem(ii);
		ii.setTotal(totBeforeTaxesForItem.add(totTaxesForItem));
		ii.setTotalTax(totTaxesForItem);
		ii.setTotalBeforeTax(totBeforeTaxesForItem);
		
		return ii;
	}
	
	public static void calculateTotals(List<InvoiceItemDTO> invoiceItems, Label totalTax, 
			Label totalBeforeTaxes, Label totalAfterTaxes){
		
		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (InvoiceItemDTO item : invoiceItems) {
			totBeforeTaxes = totBeforeTaxes.add(InvoiceUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(InvoiceUtils.calculateTaxesForItem(item));
		}
		BigDecimal totAfterTaxes = totBeforeTaxes.add(totTaxes);

		totalTax.setText(NumberFormat.getCurrencyFormat().format(totTaxes.doubleValue()));
		totalBeforeTaxes.setText(NumberFormat.getCurrencyFormat().format(totBeforeTaxes.doubleValue()));
		totalAfterTaxes.setText(NumberFormat.getCurrencyFormat().format(totAfterTaxes.doubleValue()));
	}
	
	public static void calculateTotals(List<InvoiceItemDTO> invoiceItems, AccountingDocumentDTO doc) {
		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (InvoiceItemDTO item : invoiceItems) {
			totBeforeTaxes = totBeforeTaxes.add(InvoiceUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(InvoiceUtils.calculateTaxesForItem(item));
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