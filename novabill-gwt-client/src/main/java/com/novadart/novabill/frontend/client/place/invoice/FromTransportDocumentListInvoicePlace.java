package com.novadart.novabill.frontend.client.place.invoice;

import java.util.List;

public class FromTransportDocumentListInvoicePlace extends InvoicePlace {

	private List<Long> transportDocumentList;

	public List<Long> getTransportDocumentList() {
		return transportDocumentList;
	}

	public void setTransportDocumentList(List<Long> transportDocumentList) {
		this.transportDocumentList = transportDocumentList;
	}

}
