package com.novadart.novabill.frontend.client.place;

import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;

public class HistoryPrefix {
	
	public static final String INVOICE = "invoice";
	public static final String CREDIT_NOTE = "credit_note";
	public static final String TRANSPORT_DOCUMENT = "transport_document";
	public static final String CLIENT = "client";
	public static final String CLIENT_INVOICES  = "{clientId}/"+DOCUMENTS.invoices;
	public static final String CLIENT_ESTIMATIONS  = "{clientId}/"+DOCUMENTS.estimations;
	public static final String CLIENT_CREDIT_NOTES  = "{clientId}/"+DOCUMENTS.creditNotes;
	public static final String CLIENT_TRANSPORT_DOCUMENTS  = "{clientId}/"+DOCUMENTS.transportDocuments;
	public static final String BUSINESS = "business";
	public static final String PAYMENT = "payments";
	public static final String HOME = "home";
	public static final String ESTIMATION = "estimation";

}
