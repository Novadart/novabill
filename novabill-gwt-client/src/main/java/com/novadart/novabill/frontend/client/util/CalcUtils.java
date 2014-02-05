package com.novadart.novabill.frontend.client.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;

public class CalcUtils {

	public static final RegExp POSITIVE_TWO_DECIMALS_FLOAT = RegExp.compile("^\\d+((\\.|\\,)\\d{1,2})?$");
	public static final RegExp TWO_DECIMALS_FLOAT = RegExp.compile("^(\\+|\\-)?\\d+((\\.|\\,)\\d{1,2})?$");

	private static final BigDecimal BD_100 = BigDecimal.valueOf(100);

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

	public static BigDecimal round2Dec(BigDecimal value){
		return value.setScale(2, RoundingMode.HALF_UP);
	}

	/*
	 * Commodities methods
	 */
	
	public static BigDecimal calculatePriceForCommodity(CommodityDTO commodity, String priceListName){
		PriceDTO price = commodity.getPrices().get(priceListName);

		if(priceListName.equalsIgnoreCase("::default")){
			return price.getPriceValue();
		} else {

			if(price == null || price.getId() == null){
				//if no price for the given price list, return the default price
				price = commodity.getPrices().get("::default");
				return price.getPriceValue();
			}

			PriceDTO defaultPrice = commodity.getPrices().get("::default");
			BigDecimal percentage = null;

			switch (price.getPriceType()) {
			case DISCOUNT_PERCENT:
				percentage = price.getPriceValue().multiply(new BigDecimal("-1")).add(BD_100).divide(BD_100);
				return round2Dec(defaultPrice.getPriceValue().multiply(percentage));

			case DISCOUNT_FIXED:
				return round2Dec(defaultPrice.getPriceValue().subtract(price.getPriceValue()));

			case OVERCHARGE_FIXED:
				return round2Dec(defaultPrice.getPriceValue().add(price.getPriceValue()));

			case OVERCHARGE_PERCENT:
				percentage = price.getPriceValue().add(BD_100).divide(BD_100);
				return round2Dec(defaultPrice.getPriceValue().multiply(percentage));

			case FIXED:
				return price.getPriceValue();

			default:
				return null;
			}
		}
	}


	/*
	 * PAYMENTS
	 */

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
			switch (payment.getPaymentDeltaType()) {
			case COMMERCIAL_MONTH:
				//add the delay	
				d = addCommercialMonthsToDate(documentDate, payment.getPaymentDateDelta());
				break;
				
			case DAYS:
				d = (Date)documentDate.clone();
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
				d = addCommercialMonthsToDate(documentDate, payment.getPaymentDateDelta());
				return d;
				
			case DAYS:
				d = (Date)documentDate.clone();
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

	public static BigDecimal calculateTotalBeforeTaxesForItem(AccountingDocumentItemDTO item){
		if(item.getPrice() == null){
			return BigDecimal.ZERO;
		}

		BigDecimal discount = item.getDiscount()==null ? 
				BigDecimal.ONE : 
					item.getDiscount().multiply(BigDecimal.valueOf(-1)).add(BD_100).divide(BD_100);
		BigDecimal total = item.getPrice()
				.multiply(item.getQuantity())
				.multiply(discount);

		// rounded because this way document total will be precise
		return round2Dec(total);
	}


	public static void updateTotals(AccountingDocumentItemDTO item){
		BigDecimal totBeforeTaxesForItem = CalcUtils.calculateTotalBeforeTaxesForItem(item);
		item.setTotalBeforeTax(totBeforeTaxesForItem);
	}


	private static Map<BigDecimal, List<AccountingDocumentItemDTO>> partitionItemsByTax(List<AccountingDocumentItemDTO> items){
		Map<BigDecimal, List<AccountingDocumentItemDTO>> partitions = new TreeMap<BigDecimal, List<AccountingDocumentItemDTO>>();

		BigDecimal tmpTax = null;
		List<AccountingDocumentItemDTO> tmpItems = null;

		for (AccountingDocumentItemDTO a : items) {
			if(a.getPrice() == null){
				continue;
			}

			tmpTax = a.getTax();
			tmpItems = partitions.get(tmpTax);

			if(tmpItems == null){
				tmpItems = new ArrayList<AccountingDocumentItemDTO>();
				partitions.put(tmpTax, tmpItems);
			}

			tmpItems.add(a);
		}

		return partitions;
	}
	
	
	public static BigDecimal calculateTotalWeight(AccountingDocumentItemDTO item){
		if(item.getWeight() == null){
			return BigDecimal.ZERO;
		}
		
		return item.getWeight().multiply(item.getQuantity());
	}
	
	
	public static BigDecimal calculateTotalWeight(List<AccountingDocumentItemDTO> items){
		BigDecimal total = BigDecimal.ZERO;
		for (AccountingDocumentItemDTO i : items) {
			total = total.add(calculateTotalWeight(i));
		}
		return total;
	}


	private static AccountingDocumentTotals calculatePartialTotals(BigDecimal tax, List<AccountingDocumentItemDTO> accountingDocumentItems){
		BigDecimal totalBeforeTaxes = BigDecimal.ZERO;

		for (AccountingDocumentItemDTO a : accountingDocumentItems) {
			totalBeforeTaxes = totalBeforeTaxes.add(a.getTotalBeforeTax());
		}

		//	calc total before taxes
		BigDecimal roundedTotalBeforeTaxes = round2Dec(totalBeforeTaxes);

		//	calc total taxes
		BigDecimal taxesPercent = tax.divide(BD_100); // for example 22 / 100 = 0.22
		BigDecimal totalTaxes = roundedTotalBeforeTaxes.multiply(taxesPercent);
		BigDecimal roundedTaxes = round2Dec(totalTaxes);

		// calc total after taxes
		BigDecimal roundedTotalAfterTaxes = roundedTotalBeforeTaxes.add(roundedTaxes); // already rounded

		AccountingDocumentTotals result = new AccountingDocumentTotals();
		result.setTotalBeforeTaxes(roundedTotalBeforeTaxes);
		result.setTotalTaxes(roundedTaxes);
		result.setTotalAfterTaxes(roundedTotalAfterTaxes);
		return result;
	}


	public static AccountingDocumentTotals calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems){

		Map<BigDecimal, List<AccountingDocumentItemDTO>> partitions = partitionItemsByTax(accountingDocumentItems);

		BigDecimal totalBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totalTaxes = BigDecimal.ZERO;

		AccountingDocumentTotals partialTotals = null;
		for (BigDecimal tax : partitions.keySet()) {
			partialTotals = calculatePartialTotals(tax, partitions.get(tax));
			totalBeforeTaxes = totalBeforeTaxes.add(partialTotals.getTotalBeforeTaxes());
			totalTaxes = totalTaxes.add(partialTotals.getTotalTaxes());
		}

		BigDecimal totalAfterTaxes = totalBeforeTaxes.add(totalTaxes);

		// all values are sum of rounded values, thus no rounding is required
		AccountingDocumentTotals result = new AccountingDocumentTotals();
		result.setTotalBeforeTaxes(totalBeforeTaxes);
		result.setTotalTaxes(totalTaxes);
		result.setTotalAfterTaxes(totalAfterTaxes);
		return result;
	}




	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, Label totalTax, 
			Label totalBeforeTaxes, Label totalAfterTaxes){

		AccountingDocumentTotals totals = calculateTotals(accountingDocumentItems);

		totalTax.setText(NumberFormat.getCurrencyFormat().format(totals.getTotalTaxes()));
		totalBeforeTaxes.setText(NumberFormat.getCurrencyFormat().format(totals.getTotalBeforeTaxes()));
		totalAfterTaxes.setText(NumberFormat.getCurrencyFormat().format(totals.getTotalAfterTaxes()));
	}


	public static void calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems, AccountingDocumentDTO doc) {
		AccountingDocumentTotals totals = calculateTotals(accountingDocumentItems);

		doc.setTotalBeforeTax(totals.getTotalBeforeTaxes());
		doc.setTotalTax(totals.getTotalTaxes());
		doc.setTotal(totals.getTotalAfterTaxes());
	}


	public static class AccountingDocumentTotals {
		private BigDecimal totalBeforeTaxes;
		private BigDecimal totalTaxes;
		private BigDecimal totalAfterTaxes;

		public BigDecimal getTotalBeforeTaxes() {
			return totalBeforeTaxes;
		}
		public void setTotalBeforeTaxes(BigDecimal totalBeforeTaxes) {
			this.totalBeforeTaxes = totalBeforeTaxes;
		}
		public BigDecimal getTotalTaxes() {
			return totalTaxes;
		}
		public void setTotalTaxes(BigDecimal totalTaxes) {
			this.totalTaxes = totalTaxes;
		}
		public BigDecimal getTotalAfterTaxes() {
			return totalAfterTaxes;
		}
		public void setTotalAfterTaxes(BigDecimal totalAfterTaxes) {
			this.totalAfterTaxes = totalAfterTaxes;
		}
	}

}
