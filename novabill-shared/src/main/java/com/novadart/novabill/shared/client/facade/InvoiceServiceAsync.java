package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public interface InvoiceServiceAsync {

	void get(long id, AsyncCallback<InvoiceDTO> callback);

	void getAllInRange(int start, int length, AsyncCallback<List<InvoiceDTO>> callback);

	void update(InvoiceDTO invoiceDTO, AsyncCallback<Void> callback);

	void getAllForClient(long id, AsyncCallback<List<InvoiceDTO>> callback);

	void add(InvoiceDTO invoiceDTO, AsyncCallback<Long> callback);

	void getNextInvoiceDocumentID(AsyncCallback<Long> callback);

	void remove(Long id, AsyncCallback<Void> callback);

	void getAllForClientInRange(long id, int start, int length,
			AsyncCallback<List<InvoiceDTO>> callback);

	void createFromEstimation(EstimationDTO estimationDTO,
			AsyncCallback<InvoiceDTO> callback);

	void validateInvoiceDocumentID(Long documentID,
			AsyncCallback<List<Long>> callback);

}
