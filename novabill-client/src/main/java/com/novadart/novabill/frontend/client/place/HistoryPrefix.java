package com.novadart.novabill.frontend.client.place;

import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;

public class HistoryPrefix {
	
	public static final String INVOICE = "invoice";
	public static final String CLIENT = "client";
	public static final String CLIENT_INVOICES  = "{clientId}/"+DOCUMENTS.invoices;
	public static final String CLIENT_ESTIMATIONS  = "{clientId}/"+DOCUMENTS.estimations;
	public static final String BUSINESS = "business";
	public static final String HOME = "home";
	public static final String ESTIMATION = "estimation";

}
