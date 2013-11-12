package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface InvoiceGwtServiceAsync {

	void add(InvoiceDTO invoiceDTO, AsyncCallback<Long> callback);

	void get(Long id, AsyncCallback<InvoiceDTO> callback);

	void getAllForClient(Long clientID, AsyncCallback<List<InvoiceDTO>> callback);

	void getAllForClientInRange(Long clientID, Integer start, Integer length,
			AsyncCallback<PageDTO<InvoiceDTO>> callback);

	void getAllInRange(Long businessID, Integer start, Integer length,
			AsyncCallback<PageDTO<InvoiceDTO>> callback);

	void getNextInvoiceDocumentID(AsyncCallback<Long> callback);

	void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback);

	void setPayed(Long businessID, Long clientID, Long id, Boolean value,
			AsyncCallback<Void> callback);

	void update(InvoiceDTO invoiceDTO, AsyncCallback<Void> callback);

}
