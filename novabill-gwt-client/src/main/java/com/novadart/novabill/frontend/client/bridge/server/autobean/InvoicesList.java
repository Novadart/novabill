package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.util.List;

public interface InvoicesList {

	public List<Invoice> getInvoices();
	
	public void setInvoices(List<Invoice> invoices);
	
}
