package com.novadart.novabill.frontend.client.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class DocumentUtils {
	
	public static DateTimeFormat DOCUMENT_DATE_FORMAT = DateTimeFormat.getFormat("dd MMMM yyyy");
	
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
		return item.getPrice() == null 
				? BigDecimal.ZERO
				: item.getPrice()
				.multiply(item.getQuantity())
				.multiply(item.getTax())
				.divide(new BigDecimal(100));
	}
	
	
	public static BigDecimal calculateTotalBeforeTaxesForItem(AccountingDocumentItemDTO item){
		return item.getPrice() == null 
				? BigDecimal.ZERO
				: item.getPrice()
				.multiply(item.getQuantity());
	}
	
	
	public static BigDecimal calculateTotalAfterTaxesForItem(AccountingDocumentItemDTO item){
		return item.getPrice() == null 
				? BigDecimal.ZERO
				: item.getPrice()
				.multiply(item.getQuantity())
				.multiply(item.getTax().add(new BigDecimal(100)))
				.divide(new BigDecimal(100));
	}
	
	
	public static AccountingDocumentItemDTO createAccountingDocumentItem(String description, String price, 
			String quantity, String unitOfMeasure, BigDecimal tax){
		AccountingDocumentItemDTO ii = new AccountingDocumentItemDTO();

		try {
			ii.setDescription(description);
			ii.setPrice(parseCurrency(price));
			ii.setQuantity(parseValue(quantity));
			ii.setUnitOfMeasure(unitOfMeasure);
			ii.setTax(tax);
		} catch (NumberFormatException ex) {
			return null;
		}
		
		DocumentUtils.updateTotals(ii);
		return ii;
	}
	
	
	public static AccountingDocumentItemDTO createAccountingDocumentItem(String description) {
		AccountingDocumentItemDTO ii = new AccountingDocumentItemDTO();
		ii.setDescription(description);
		return ii;
	}
	
	
	public static String validateAccountingDocumentItem(String description, String price, 
			String quantity, String unitOfMeasure, BigDecimal tax){
		if(description.isEmpty()) {
			return I18NM.get.errorCheckField(I18N.INSTANCE.nameDescription());
		}
		
		if( price.isEmpty() ) {
			return I18NM.get.errorCheckField(I18N.INSTANCE.price());
		} else {
			
			try {
			
				parseCurrency(price);
			
			} catch (NumberFormatException e) {
				return I18NM.get.errorCheckField(I18N.INSTANCE.price());
			}
			
		}
		
		if( quantity.isEmpty() ) {
			return I18NM.get.errorCheckField(I18N.INSTANCE.quantity());
		} else {
			
			try {
				
				parseValue(quantity);
			
			} catch (NumberFormatException e) {
				return I18NM.get.errorCheckField(I18N.INSTANCE.quantity());
			}
		}
		
		if( tax == null ){
			return I18NM.get.errorCheckField(I18N.INSTANCE.vat());
		}
		
		return null;
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
		
		Date d;
		
		switch (payment.getPaymentDateGenerator()) {
		default:
		case CUSTOM:
			return null;
			
		case END_OF_MONTH:
			//add the delay	
			d = calculatePaymentDueDateAddingCommercialMonths(documentDate, payment.getPaymentDateDelta());
			
			// move to the end of month	
			CalendarUtil.setToFirstDayOfMonth(d);
			CalendarUtil.addMonthsToDate(d, 1);
			CalendarUtil.addDaysToDate(d, -1);
			return d;
			
		case IMMEDIATE:
			//add the delay	
			d = calculatePaymentDueDateAddingCommercialMonths(documentDate, payment.getPaymentDateDelta());
						
			return d;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private static Date calculatePaymentDueDateAddingCommercialMonths(Date date, int months){
		if(months == 0){
			return date;
		}
		
		// first of all add the months on a date set to the first day of month.
		// This way the different length of months won't get on the way
		Date resultDate = (Date)date.clone();
		CalendarUtil.setToFirstDayOfMonth(resultDate);
		CalendarUtil.addMonthsToDate(resultDate, months);
		
		//save the resulting month		
		int resultMonth = resultDate.getMonth();
		
		// set the day of the original date. Notice: if month is February and day is 31 this will cause a change of month.
		// Of course there are many other similar cases.
		resultDate.setDate(date.getDate());
		
		// NOTE: we can use > because December is 31 days long. However != is more general and can take into account cases that
		// do not come up into my mind now The months must be the same
		while(resultDate.getMonth() != resultMonth) {
			//remove a day till we get to the last day of the target month
			CalendarUtil.addDaysToDate(resultDate, -1);
		}
		
		return resultDate;
	}

}
