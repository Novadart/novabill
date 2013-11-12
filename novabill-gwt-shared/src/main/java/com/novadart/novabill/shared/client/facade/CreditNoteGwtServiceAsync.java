package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface CreditNoteGwtServiceAsync {

	void add(CreditNoteDTO creditNoteDTO, AsyncCallback<Long> callback);

	void get(Long id, AsyncCallback<CreditNoteDTO> callback);

	void getAllForClient(Long clientID,
			AsyncCallback<List<CreditNoteDTO>> callback);

	void getAllForClientInRange(Long id, int start, int length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback);

	void getAllInRange(Long businessID, Integer start, Integer length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback);

	void remove(Long businessID, Long clientID, Long creditNoteID,
			AsyncCallback<Void> callback);

	void update(CreditNoteDTO creditNoteDTO, AsyncCallback<Void> callback);

	void getNextCreditNoteDocumentID(AsyncCallback<Long> callback);
	
}
