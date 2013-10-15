package com.novadart.novabill.frontend.client.bridge.server.data;

import java.util.List;

public interface IInvoicesData {

	public List<IAutoBeanInvoiceDTO> getInvoices();

	public void setInvoices(List<IAutoBeanInvoiceDTO> invoices);

}