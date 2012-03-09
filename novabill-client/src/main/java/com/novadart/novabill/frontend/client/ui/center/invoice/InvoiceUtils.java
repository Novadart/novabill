package com.novadart.novabill.frontend.client.ui.center.invoice;

import java.math.BigDecimal;
import java.util.Date;

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
