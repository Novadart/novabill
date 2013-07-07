package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public interface BatchDataFetcherServiceAsync {

	void fetchNewInvoiceForClientOpData(Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, PaymentTypeDTO>> callback);

	void fetchNewInvoiceFromEstimationOpData(Long estimationID,
			AsyncCallback<Triple<Long, EstimationDTO, PaymentTypeDTO>> callback);

	void fetchNewInvoiceFromTransportDocumentOpData(
			Long transportDocumentID,
			AsyncCallback<Triple<Long, TransportDocumentDTO, PaymentTypeDTO>> callback);

	void fetchCloneInvoiceOpData(Long invoiceID, Long clientID, AsyncCallback<Triple<Long, ClientDTO, InvoiceDTO>> callback);

	void fetchNewEstimationForClientOpData(Long clientID, AsyncCallback<Pair<Long, ClientDTO>> callback);

	void fetchCloneEstimationOpData(Long invoiceID, Long clientID, AsyncCallback<Triple<Long, ClientDTO, EstimationDTO>> callback);

	void fetchNewTransportDocumentForClientOpData(Long clientID, AsyncCallback<Pair<Long, ClientDTO>> callback);

	void fetchCloneTransportDocumentOpData(Long transportDocID, Long clientID, AsyncCallback<Triple<Long, ClientDTO, TransportDocumentDTO>> callback);

	void fetchNewCreditNoteForClientOpData(Long clientID, AsyncCallback<Pair<Long, ClientDTO>> callback);

	void fetchNewCreditNoteFromInvoiceOpData(Long invoiceID, AsyncCallback<Pair<Long, InvoiceDTO>> callback);

}
