package com.novadart.novabill.frontend.client.view.center;

import com.google.gwt.resources.client.CssResource;

public interface AccountDocumentCss extends CssResource {

	String total();

	String loadDefaultsButton();

	String addressBox();

	String totalContainer();

	String itemTableContainer();

	String date();

	String modifyButton();

	String documentMainDetails();

	String createButton();
	
	String totalsContainer();

	String time();

	String invoiceNumberSuffix();

	String abortButton();

	String clientName();

	String docBody();

	String note();

	String newInvoice();

	String docScrollBody();

	String docControls();

	String convertToInvoiceLB();

	String label();

	String number();

	String newCreditNote();

	String loaderButton();

	String newTransportDocument();

	String totalBeforeTaxes();
	
	String totalAfterTaxes();
	
	String totalTax();

	@ClassName("AccountDocumentView")
	String accountDocumentView();

	String sublabel();

	String paymentNote();

	String newEstimation();

	String box();

	String docScroll();

}
