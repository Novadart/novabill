package com.novadart.novabill.shared.client.validation;

public enum Field {
	documentID, accountingDocumentDate, accountingDocumentYear, note, total, totalTax, totalBeforeTax, //Accounting document fields
	paymentNote, paymentType, paymentDueDate, payed, //Invoice fields
	name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatID, ssn, //Business and Client fields
	contact_firstName, contact_lastName, contact_email, contact_phone, contact_fax, contact_mobile //Client contact fields 
}
