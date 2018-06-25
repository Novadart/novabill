package com.novadart.novabill.frontend.client.util;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.util.AccountingCalcUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CalcUtils {

//	public static final RegExp POSITIVE_TWO_DECIMALS_FLOAT = RegExp.compile("^\\d+((\\.|\\,)\\d{1,2})?$");
//	public static final RegExp TWO_DECIMALS_FLOAT = RegExp.compile("^(\\+|\\-)?\\d+((\\.|\\,)\\d{1,2})?$");

	public static BigDecimal parseValue(String value) throws NumberFormatException {
		double parsedValue = NumberFormat.getDecimalFormat().parse(value);
		// converting to string to increase precision
		return new BigDecimal( String.valueOf(parsedValue)  );
	}

	public static BigDecimal parseCurrency(String value) throws NumberFormatException {
		try{
			//GWT requires a non breakable space between currency symbol and value
			double parsedValue = NumberFormat.getCurrencyFormat().parse(value.replace(" ", "\u00A0"));
			// converting to string to increase precision
			return new BigDecimal( String.valueOf(parsedValue)  );

		} catch (NumberFormatException e) {
			return parseValue(value);
		}
	}

	/*
	 * Commodities methods
	 */
	
//	public static BigDecimal calculatePriceForCommodity(CommodityDTO commodity, String priceListName){
//		PriceDTO price = commodity.getPrices().get(priceListName);
//
//		if(priceListName.equalsIgnoreCase("::default")){
//			return price.getPriceValue();
//		} else {
//
//			if(price.getPriceValue() == null || price.getId() == null){
//				//if no price for the given price list, return the default price
//				price = commodity.getPrices().get("::default");
//				return price.getPriceValue();
//			}
//
//			PriceDTO defaultPrice = commodity.getPrices().get("::default");
//			BigDecimal percentage = null;
//
//			switch (price.getPriceType()) {
//			case DISCOUNT_PERCENT:
//				percentage = price.getPriceValue().multiply(new BigDecimal("-1")).add(BD_100).divide(BD_100);
//				return round2Dec(defaultPrice.getPriceValue().multiply(percentage));
//
//			case DISCOUNT_FIXED:
//				return round2Dec(defaultPrice.getPriceValue().subtract(price.getPriceValue()));
//
//			case OVERCHARGE_FIXED:
//				return round2Dec(defaultPrice.getPriceValue().add(price.getPriceValue()));
//
//			case OVERCHARGE_PERCENT:
//				percentage = price.getPriceValue().add(BD_100).divide(BD_100);
//				return round2Dec(defaultPrice.getPriceValue().multiply(percentage));
//
//			case FIXED:
//				return price.getPriceValue();
//
//			default:
//				return null;
//			}
//		}
//	}


	/*
	 * PAYMENTS
	 */

	public static Date calculatePaymentDueDate(Date documentDate, PaymentTypeDTO payment){
		if(PaymentDateType.CUSTOM.equals(payment.getPaymentDateGenerator())){
			return null;
		}

		Date d;
		Date normDate = DocumentUtils.createNormalizedDate(documentDate);

		switch (payment.getPaymentDateGenerator()) {
		default:
		case CUSTOM:
			return null;

		case END_OF_MONTH:
			switch (payment.getPaymentDeltaType()) {
			case COMMERCIAL_MONTH:
				//add the delay	
				d = addCommercialMonthsToDate(normDate, payment.getPaymentDateDelta());
				break;
				
			case DAYS:
				d = (Date)normDate.clone();
				CalendarUtil.addDaysToDate(d, payment.getPaymentDateDelta());
				break;
				
			default:
				return null;
			}
			
			// move to the end of month	
			CalendarUtil.setToFirstDayOfMonth(d);
			CalendarUtil.addMonthsToDate(d, 1);
			CalendarUtil.addDaysToDate(d, -1);
			
			CalendarUtil.addDaysToDate(d, payment.getSecondaryPaymentDateDelta()==null ? 0 : payment.getSecondaryPaymentDateDelta());
			return d;
			

		case IMMEDIATE:
			switch (payment.getPaymentDeltaType()) {
			case COMMERCIAL_MONTH:
				//add the delay	
				d = addCommercialMonthsToDate(normDate, payment.getPaymentDateDelta());
				return d;
				
			case DAYS:
				d = (Date)normDate.clone();
				CalendarUtil.addDaysToDate(d, payment.getPaymentDateDelta());
				return d;

			default:
				return null;
			}
			
		}
	}


	@SuppressWarnings("deprecation")
	private static Date addCommercialMonthsToDate(Date date, int months){
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


	/*
	 * TOTALS
	 */


	public static void updateTotals(AccountingDocumentItemDTO item){
		BigDecimal totBeforeTaxesForItem = AccountingCalcUtils.calculateTotalBeforeTaxesForItem(item);
		item.setTotalBeforeTax(totBeforeTaxesForItem);
	}


	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, Label totalTax, Label totalBeforeTaxes, Label totalAfterTaxes){

		AccountingCalcUtils.AccountingDocumentTotals totals = AccountingCalcUtils.calculateTotals(accountingDocumentItems);

		totalTax.setText(NumberFormat.getCurrencyFormat().format(totals.getTotalTaxes()));
		totalBeforeTaxes.setText(NumberFormat.getCurrencyFormat().format(totals.getTotalBeforeTaxes()));
		totalAfterTaxes.setText(NumberFormat.getCurrencyFormat().format(totals.getTotalAfterTaxes()));
	}


	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, AccountingDocumentDTO doc) {
		AccountingCalcUtils.AccountingDocumentTotals totals = AccountingCalcUtils.calculateTotals(accountingDocumentItems);

		doc.setTotalBeforeTax(totals.getTotalBeforeTaxes());
		doc.setTotalTax(totals.getTotalTaxes());
		doc.setTotal(totals.getTotalAfterTaxes());
	}

	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, InvoiceDTO invoice) {
		AccountingCalcUtils.AccountingDocumentTotals totals = AccountingCalcUtils.calculateTotals(accountingDocumentItems);

		invoice.setTotalBeforeTax(totals.getTotalBeforeTaxes());
		invoice.setTotalTax(totals.getTotalTaxes());
		invoice.setTotal(totals.getTotalAfterTaxes());
	}

}
