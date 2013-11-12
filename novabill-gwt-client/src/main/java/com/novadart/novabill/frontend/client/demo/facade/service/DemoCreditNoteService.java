package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.CreditNoteGwtServiceAsync;

public class DemoCreditNoteService implements CreditNoteGwtServiceAsync {

	@Override
	public void add(CreditNoteDTO creditNoteDTO, AsyncCallback<Long> callback) {
		Data.save(creditNoteDTO, CreditNoteDTO.class);
		callback.onSuccess(creditNoteDTO.getId());
	}

	@Override
	public void get(Long id, AsyncCallback<CreditNoteDTO> callback) {
		try {
			callback.onSuccess(Data.getDoc(id, CreditNoteDTO.class));
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void getAllForClient(Long clientID,	AsyncCallback<List<CreditNoteDTO>> callback) {
		callback.onSuccess(Data.getDocsList(clientID, CreditNoteDTO.class));
	}

	@Override
	public void getAllForClientInRange(Long id, int start, int length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllInRange(Long businessID, Integer start, Integer length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback) {
		PageDTO<CreditNoteDTO> page = new PageDTO<CreditNoteDTO>();
		List<CreditNoteDTO> docs = Data.getDocsList(CreditNoteDTO.class);
		page.setItems(docs);
		page.setLength(docs.size());
		page.setOffset(0);
		page.setTotal(Long.valueOf(docs.size()));
		callback.onSuccess(page);
	}

	@Override
	public void remove(Long businessID, Long clientID, Long creditNoteID, AsyncCallback<Void> callback) {
		Data.remove(clientID, creditNoteID, CreditNoteDTO.class);
		callback.onSuccess(null);
	}

	@Override
	public void update(CreditNoteDTO creditNoteDTO, AsyncCallback<Void> callback) {
		Data.save(creditNoteDTO, CreditNoteDTO.class);
		callback.onSuccess(null);
	}

	@Override
	public void getNextCreditNoteDocumentID(AsyncCallback<Long> callback) {
		callback.onSuccess(Data.nextDocID(CreditNoteDTO.class));
	}

}
