package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;

public class DemoInvoiceService implements InvoiceServiceAsync {

	@Override
	public void add(InvoiceDTO invoiceDTO, AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(Long id, AsyncCallback<InvoiceDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllForClient(Long clientID,
			AsyncCallback<List<InvoiceDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllForClientInRange(Long clientID, Integer start,
			Integer length, AsyncCallback<PageDTO<InvoiceDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllInRange(Long businessID, Integer start, Integer length,
			AsyncCallback<PageDTO<InvoiceDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNextInvoiceDocumentID(AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPayed(Long businessID, Long clientID, Long id,
			Boolean value, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(InvoiceDTO invoiceDTO, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

}
