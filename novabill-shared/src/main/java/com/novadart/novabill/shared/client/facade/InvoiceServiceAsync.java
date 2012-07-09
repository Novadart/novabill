package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface InvoiceServiceAsync {

	void get(long id, AsyncCallback<InvoiceDTO> callback);

	void getAllInRange(int start, int length,
			AsyncCallback<PageDTO<InvoiceDTO>> callback);

	void update(InvoiceDTO invoiceDTO, AsyncCallback<Void> callback);

	void getAllForClient(long id, AsyncCallback<List<InvoiceDTO>> callback);

	void add(InvoiceDTO invoiceDTO, AsyncCallback<Long> callback);

	void getNextInvoiceDocumentID(AsyncCallback<Long> callback);

	void remove(Long id, AsyncCallback<Void> callback);

	void getAllForClientInRange(long id, int start, int length,
			AsyncCallback<PageDTO<InvoiceDTO>> callback);

	void createFromEstimation(InvoiceDTO invoiceDTO, Long estimationID,
			AsyncCallback<Long> callback);

	void setPayed(Long id, Boolean value, AsyncCallback<Void> callback);

}
