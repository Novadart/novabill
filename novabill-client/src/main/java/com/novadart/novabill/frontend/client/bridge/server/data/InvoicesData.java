package com.novadart.novabill.frontend.client.bridge.server.data;

import java.util.List;

public class InvoicesData implements IInvoicesData {

	private List<IAutoBeanInvoiceDTO> invoices;

	@Override
	public List<IAutoBeanInvoiceDTO> getInvoices() {
		return invoices;
	}

	@Override
	public void setInvoices(List<IAutoBeanInvoiceDTO> invoices) {
		this.invoices = invoices;
	}
}
