package com.novadart.novabill.frontend.client.demo.facade.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherServiceAsync;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class DemoBatchDataFetcherService implements
		BatchDataFetcherServiceAsync {

	@Override
	public void fetchNewInvoiceForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchNewInvoiceFromEstimationOpData(Long estimationID,
			AsyncCallback<Pair<Long, EstimationDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchNewInvoiceFromTransportDocumentOpData(
			Long transportDocumentID,
			AsyncCallback<Pair<Long, TransportDocumentDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchCloneInvoiceOpData(Long invoiceID, Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, InvoiceDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchNewEstimationForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchCloneEstimationOpData(Long invoiceID, Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, EstimationDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchNewTransportDocumentForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchCloneTransportDocumentOpData(
			Long transportDocID,
			Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, TransportDocumentDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchNewCreditNoteForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchNewCreditNoteFromInvoiceOpData(Long invoiceID,
			AsyncCallback<Pair<Long, InvoiceDTO>> callback) {
		// TODO Auto-generated method stub

	}

}
