package com.novadart.novabill.frontend.client.place.invoice;

public class CloneInvoicePlace extends InvoicePlace {

	private Long invoiceId;
	private Long clientId;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
}
