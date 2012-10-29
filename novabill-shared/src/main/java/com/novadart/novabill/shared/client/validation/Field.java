package com.novadart.novabill.shared.client.validation;

public enum Field {
	documentID, accountingDocumentDate, accountingDocumentYear, note, paymentNote, total, totalTax, totalBeforeTax, //Accounting document fields
	accountingDocumentItems_description, accountingDocumentItems_unitOfMeasure, accountingDocumentItems_tax, accountingDocumentItems_quantity,
	accountingDocumentItems_totalBeforeTax, accountingDocumentItems_totalTax, accountingDocumentItems_total, accountingDocumentItems_price, //Accounting document item fields  
	paymentType, paymentDueDate, payed, //Invoice fields
	fromEndpoint_companyName, fromEndpoint_street, fromEndpoint_postcode, fromEndpoint_city, fromEndpoint_province, fromEndpoint_country, //TransportDocument fromEndpoint fields
	toEndpoint_companyName, toEndpoint_street, toEndpoint_postcode, toEndpoint_city, toEndpoint_province, toEndpoint_country, //TransportDocument toEndpoint fields
	name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatID, ssn, //Business and Client fields
	contact_firstName, contact_lastName, contact_email, contact_phone, contact_fax, contact_mobile //Client contact fields 
}
