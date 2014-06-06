package com.novadart.novabill.shared.client.validation;

/*
 * Field enumeration is the shared vocabulary of field identifiers between the server and the GWT client.
 * Field identifiers denote the fields whose values violate the validation constraints.
 */
public enum Field {
	//Accounting document fields
	documentID, accountingDocumentDate, accountingDocumentYear, note, paymentNote, layoutType, total, totalTax, totalBeforeTax,
	
	//Accounting document item fields
	accountingDocumentItems_description, accountingDocumentItems_unitOfMeasure, accountingDocumentItems_tax, accountingDocumentItems_quantity,
	accountingDocumentItems_totalBeforeTax, accountingDocumentItems_totalTax, accountingDocumentItems_total, accountingDocumentItems_price,
	
	//Invoice fields
	paymentDueDate, payed, paymentTypeName,
	
	//Estimation
	limitations, validTill,
	
	//TransportDocument fromEndpoint fields
	fromEndpoint_companyName, fromEndpoint_street, fromEndpoint_postcode, fromEndpoint_city, fromEndpoint_province, fromEndpoint_country,
	
	//TransportDocument toEndpoint fields
	toEndpoint_companyName, toEndpoint_street, toEndpoint_postcode, toEndpoint_city, toEndpoint_province, toEndpoint_country,
	
	//Transport document fields
	transporter, transportationResponsibility, tradeZone, transportStartDate, numberOfPackages, cause, appearanceOfTheGoods,
	
	//Business and Client fields
	name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatID, ssn, defaultPriceList,
	
	//Client contact fields 
	contact_firstName, contact_lastName, contact_email, contact_phone, contact_fax, contact_mobile, contact_note,
	
	//PaymentType fields
	defaultPaymentNote, paymentDateGenerator, paymentDateDelta, secondaryPaymentDateDelta, paymentTypeCls,
	
	//Commodity
	sku, description, unitOfMeasure, tax,
	
	//Price
	priceValue, priceType,
	
	//Client address
	companyName,
	
	//Business settings
	settings_defaultLayoutType, settings_priceDisplayInDocsMonolithic, settings_incognitoEnabled, settings_invoiceFooterNote,
	settings_creditNoteFooterNote, settings_estimationFooterNote, settings_transportDocumentFooterNote,
	emailSubject, emailText, emailReplyTo
}
