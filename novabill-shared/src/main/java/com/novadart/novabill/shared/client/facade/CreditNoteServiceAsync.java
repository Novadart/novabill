package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface CreditNoteServiceAsync {

	void getNextInvoiceDocumentID(AsyncCallback<Long> callback);

	void get(long id, AsyncCallback<CreditNoteDTO> callback);

	void getAllInRange(int start, int length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback);

	void getAllForClient(long id, AsyncCallback<List<CreditNoteDTO>> callback);

	void add(CreditNoteDTO creditNoteDTO, AsyncCallback<Long> callback);

	void remove(Long id, AsyncCallback<Void> callback);

	void update(CreditNoteDTO creditNoteDTO, AsyncCallback<Void> callback);

	void getAllForClientInRange(long id, int start, int length,
			AsyncCallback<PageDTO<CreditNoteDTO>> callback);

}
