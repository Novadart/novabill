package com.novadart.novabill.frontend.client.util;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.widget.dialog.client.ClientDialog;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class DocumentUtils {

	public static DateTimeFormat DOCUMENT_DATE_FORMAT = DateTimeFormat.getFormat("dd MMMM yyyy");

	public static AccountingDocumentItemDTO createAccountingDocumentItem(String sku, String description, String price, 
			String quantity, String weight, String unitOfMeasure, String tax, String discount){
		AccountingDocumentItemDTO ii = new AccountingDocumentItemDTO();

		try {
			ii.setSku(sku.isEmpty() ? null : sku);
			ii.setDescription(description);
			ii.setPrice(CalcUtils.parseCurrency(price));
			ii.setQuantity(CalcUtils.parseValue(quantity));
			ii.setWeight(weight != null && !weight.isEmpty() ? CalcUtils.parseValue(weight) : null);
			ii.setUnitOfMeasure(unitOfMeasure);
			ii.setTax(CalcUtils.parseValue(tax));
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
	
	@SuppressWarnings("deprecation")
	public static Date createNormalizedDate(Date date){
		Date normDate = (Date) date.clone();
		normDate.setHours(0);
		normDate.setMinutes(0);
		normDate.setSeconds(0);
		return normDate;
	}
	
	public static boolean isTextOnly(AccountingDocumentItemDTO item){
		return item.getPrice() == null;
	}

	
	private static boolean isEmpty(String str){
		return str == null || str.isEmpty();
	}

	public static void showClientDialogIfClientInformationNotComplete(ClientDTO client, final AsyncCallback<ClientDTO> callback){
		if( isEmpty(client.getAddress()) 
				|| isEmpty(client.getAddress())
				|| isEmpty(client.getCity())
				|| isEmpty(client.getCountry())
				|| isEmpty(client.getName())
				|| isEmpty(client.getPostcode())
				|| ("IT".equalsIgnoreCase(client.getCountry()) && isEmpty(client.getProvince()))
				|| (isEmpty(client.getSsn()) && isEmpty(client.getVatID()) ) ){
			
			ClientDialog clientDialog = new ClientDialog(Configuration.getBusiness().getId(), true, callback);
			clientDialog.setClient(client);
			clientDialog.center();

		} else {
			callback.onSuccess(client);
		}
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

		if( weight != null && !weight.isEmpty() ) {

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
