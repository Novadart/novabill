package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.CreditNoteServiceAsync;

public class DemoCreditNoteService implements CreditNoteServiceAsync {

	@Override
	public void add(CreditNoteDTO creditNoteDTO, AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(Long id, AsyncCallback<CreditNoteDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllForClient(Long clientID,
			AsyncCallback<List<CreditNoteDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllForClientInRange(Long id, int start, int length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllInRange(Long businessID, Integer start, Integer length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Long businessID, Long clientID, Long creditNoteID,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(CreditNoteDTO creditNoteDTO, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNextCreditNoteDocumentID(AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

}
