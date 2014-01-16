package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.InvoiceGwtServiceAsync;

public class DemoInvoiceService implements InvoiceGwtServiceAsync {

	@Override
	public void add(InvoiceDTO invoiceDTO, AsyncCallback<Long> callback) {
		Data.save(invoiceDTO, InvoiceDTO.class);
		callback.onSuccess(invoiceDTO.getId());
	}

	@Override
	public void get(Long id, AsyncCallback<InvoiceDTO> callback) {
		try {
			callback.onSuccess(Data.getDoc(id, InvoiceDTO.class));
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void getAllForClient(Long clientID, Integer year,
			AsyncCallback<List<InvoiceDTO>> callback) {
		callback.onSuccess(Data.getDocsList(clientID, InvoiceDTO.class));
	}

	@Override
	public void getAllForClientInRange(Long clientID, Integer year, Integer start,
			Integer length, AsyncCallback<PageDTO<InvoiceDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllInRange(Long businessID, Integer year, Integer start, Integer length,
			AsyncCallback<PageDTO<InvoiceDTO>> callback) {
		PageDTO<InvoiceDTO> page = new PageDTO<InvoiceDTO>();
		List<InvoiceDTO> docs = Data.getDocsList(InvoiceDTO.class);
		page.setItems(docs);
		page.setLength(docs.size());
		page.setOffset(0);
		page.setTotal(Long.valueOf(docs.size()));
		callback.onSuccess(page);
	}

	@Override
	public void getNextInvoiceDocumentID(AsyncCallback<Long> callback) {
		callback.onSuccess(Data.nextDocID(InvoiceDTO.class));
	}

	@Override
	public void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback) {
		Data.remove(clientID, id, InvoiceDTO.class);
		callback.onSuccess(null);
	}

	@Override
	public void setPayed(Long businessID, Long clientID, Long id,
			Boolean value, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(InvoiceDTO invoiceDTO, AsyncCallback<Void> callback) {
		Data.save(invoiceDTO, InvoiceDTO.class);
		callback.onSuccess(null);
	}

}
