package com.novadart.novabill.frontend.client.util;

import java.math.BigDecimal;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class DocumentUtils {
	
	public static DateTimeFormat DOCUMENT_DATE_FORMAT = DateTimeFormat.getFormat("dd MMMM yyyy");
	
	public static AccountingDocumentItemDTO createAccountingDocumentItem(String sku, String description, String price, 
			String quantity, String weight, String unitOfMeasure, BigDecimal tax, String discount){
		AccountingDocumentItemDTO ii = new AccountingDocumentItemDTO();

		try {
			ii.setSku(sku.isEmpty() ? null : sku);
			ii.setDescription(description);
			ii.setPrice(CalcUtils.parseCurrency(price));
			ii.setQuantity(CalcUtils.parseValue(quantity));
			ii.setWeight(weight != null ? CalcUtils.parseValue(weight) : null);
			ii.setUnitOfMeasure(unitOfMeasure);
			ii.setTax(tax);
			ii.setDiscount(discount.isEmpty() ? BigDecimal.ZERO : CalcUtils.parseValue(discount));
		} catch (NumberFormatException ex) {
			return null;
		}
		
		CalcUtils.updateTotals(ii);
		return ii;
	}
	
	
	public static AccountingDocumentItemDTO createAccountingDocumentItem(String description) {
		AccountingDocumentItemDTO ii = new AccountingDocumentItemDTO();
		ii.setDescription(description);
		return ii;
	}
	
	
	public static String validateAccountingDocumentItem(String description, String price, 
			String quantity, String weight, String unitOfMeasure, BigDecimal tax, String discount){
		if(description.isEmpty()) {
			return I18NM.get.errorCheckField(I18N.INSTANCE.nameDescription());
		}
		
		if( price.isEmpty() ) {
			return I18NM.get.errorCheckField(I18N.INSTANCE.price());
		} else {
			
			try {
			
				CalcUtils.parseCurrency(price);
			
			} catch (NumberFormatException e) {
				return I18NM.get.errorCheckField(I18N.INSTANCE.price());
			}
			
		}
		
		if( quantity.isEmpty() ) {
			return I18NM.get.errorCheckField(I18N.INSTANCE.quantity());
		} else {
			
			try {
				
				CalcUtils.parseValue(quantity);
			
			} catch (NumberFormatException e) {
				return I18NM.get.errorCheckField(I18N.INSTANCE.quantity());
			}
		}
		
		if( !weight.isEmpty() ) {
			
			try {
				
				CalcUtils.parseValue(weight);
			
			} catch (NumberFormatException e) {
				return I18NM.get.errorCheckField(I18N.INSTANCE.weight());
			}
		}
		
		if( !discount.isEmpty() ) {
			try {
				
				CalcUtils.parseValue(discount);
			
			} catch (NumberFormatException e) {
				return I18NM.get.errorCheckField(I18N.INSTANCE.discount());
			}
		}
		
		if( tax == null ){
			return I18NM.get.errorCheckField(I18N.INSTANCE.vat());
		}
		
		return null;
	}
	
}
