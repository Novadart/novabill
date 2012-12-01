package com.novadart.novabill.shared.client.validation;

public enum Field {
	//Accounting document fields
	documentID, accountingDocumentDate, accountingDocumentYear, note, paymentNote, total, totalTax, totalBeforeTax,
	
	//Accounting document item fields
	accountingDocumentItems_description, accountingDocumentItems_unitOfMeasure, accountingDocumentItems_tax, accountingDocumentItems_quantity,
	accountingDocumentItems_totalBeforeTax, accountingDocumentItems_totalTax, accountingDocumentItems_total, accountingDocumentItems_price,
	
	//Invoice fields
	paymentType, paymentDueDate, payed,
	
	//Estimation
	limitations, validTill,
	
	//TransportDocument fromEndpoint fields
	fromEndpoint_companyName, fromEndpoint_street, fromEndpoint_postcode, fromEndpoint_city, fromEndpoint_province, fromEndpoint_country,
	
	//TransportDocument toEndpoint fields
	toEndpoint_companyName, toEndpoint_street, toEndpoint_postcode, toEndpoint_city, toEndpoint_province, toEndpoint_country,
	
	//Transport document fields
	transporter, transportationResponsibility, tradeZone, transportStartDate, numberOfPackages,
	
	//Business and Client fields
	name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatID, ssn, 
	
	//Client contact fields 
	contact_firstName, contact_lastName, contact_email, contact_phone, contact_fax, contact_mobile 
}
