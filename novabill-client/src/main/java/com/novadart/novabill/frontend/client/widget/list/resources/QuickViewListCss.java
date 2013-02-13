package com.novadart.novabill.frontend.client.widget.list.resources;

import com.google.gwt.resources.client.CssResource;

public interface QuickViewListCss extends CssResource {

	String downloadAsPDF();
	
	String delete();
	
	String clone();
	
	String creditNote();
	
	String total();

	String openInvoice();

	String openTransportDocument();

	@ClassName("InvoiceList")
	String invoiceList();

	@ClassName("payed-true")
	String payedTrue();

	String pdf();

	String payed();

	String quickViewCell();

	String createInvoice();

	String date();

	String openCreditNote();

	String main();

	String id();

	String upper();

	String tools();

	@ClassName("TransportDocumentList")
	String transportDocumentList();

	String details();

	String convertToInvoice();

	@ClassName("EstimationList")
	String estimationList();

	String name();

	String openEstimation();

	@ClassName("payed-false")
	String payedFalse();

}
