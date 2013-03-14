package com.novadart.novabill.shared.client.facade;

import com.google.gwt.rpc.server.WebModeClientOracle.Triple;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.tuple.Pair;

public interface BatchDataFetcherServiceAsync {

	void fetchNewInvoiceForClientOpData(Long clientID, AsyncCallback<Pair<Long, ClientDTO>> callback);

	void fetchNewInvoiceFromEstimationOpData(Long estimationID, AsyncCallback<Pair<Long, EstimationDTO>> callback);

	void fetchNewInvoiceFromTransportDocumentOpData(Long transportDocumentID, AsyncCallback<Pair<Long, TransportDocumentDTO>> callback);

	void fetchCloneInvoiceOpData(Long invoiceID, Long clientID, AsyncCallback<Triple<Long, ClientDTO, InvoiceDTO>> callback);

	void fetchNewEstimationForClientOpData(Long clientID, AsyncCallback<Pair<Long, ClientDTO>> callback);

	void fetchCloneEstimationOpData(Long invoiceID, Long clientID, AsyncCallback<Triple<Long, ClientDTO, EstimationDTO>> callback);

	void fetchNewTransportDocumentForClientOpData(Long clientID, AsyncCallback<Pair<Long, ClientDTO>> callback);

	void fetchCloneTransportDocumentOpData(Long transportDocID, Long clientID, AsyncCallback<Triple<Long, ClientDTO, TransportDocumentDTO>> callback);

}
