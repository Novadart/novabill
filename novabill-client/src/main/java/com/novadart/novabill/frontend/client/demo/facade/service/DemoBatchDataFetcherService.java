package com.novadart.novabill.frontend.client.demo.facade.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherServiceAsync;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class DemoBatchDataFetcherService implements
		BatchDataFetcherServiceAsync {

	@Override
	public void fetchNewInvoiceForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		try {
			Pair<Long, ClientDTO> p = new Pair<Long, ClientDTO>();
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(Data.getClient(clientID));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewInvoiceFromEstimationOpData(Long estimationID,
			AsyncCallback<Pair<Long, EstimationDTO>> callback) {
		try {
			Pair<Long, EstimationDTO> p = new Pair<Long, EstimationDTO>();
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(Data.getDoc(estimationID, EstimationDTO.class));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewInvoiceFromTransportDocumentOpData(
			Long transportDocumentID,
			AsyncCallback<Pair<Long, TransportDocumentDTO>> callback) {
		try {
			Pair<Long, TransportDocumentDTO> p = new Pair<Long, TransportDocumentDTO>();
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(Data.getDoc(transportDocumentID, TransportDocumentDTO.class));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchCloneInvoiceOpData(Long invoiceID, Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, InvoiceDTO>> callback) {
		try {
			Triple<Long, ClientDTO, InvoiceDTO> p = new Triple<Long, ClientDTO, InvoiceDTO>();
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(Data.getClient(clientID));
			p.setThird(Data.getDoc(invoiceID, InvoiceDTO.class));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewEstimationForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		try {
			Pair<Long, ClientDTO> p = new Pair<Long, ClientDTO>();
			p.setFirst(Data.nextDocID(EstimationDTO.class));
			p.setSecond(Data.getClient(clientID));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchCloneEstimationOpData(Long invoiceID, Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, EstimationDTO>> callback) {
		try {
			Triple<Long, ClientDTO, EstimationDTO> p = new Triple<Long, ClientDTO, EstimationDTO>();
			p.setFirst(Data.nextDocID(EstimationDTO.class));
			p.setSecond(Data.getClient(clientID));
			p.setThird(Data.getDoc(invoiceID, EstimationDTO.class));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewTransportDocumentForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		try {
			Pair<Long, ClientDTO> p = new Pair<Long, ClientDTO>();
			p.setFirst(Data.nextDocID(TransportDocumentDTO.class));
			p.setSecond(Data.getClient(clientID));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchCloneTransportDocumentOpData(
			Long transportDocID,
			Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, TransportDocumentDTO>> callback) {
		try {
			Triple<Long, ClientDTO, TransportDocumentDTO> p = new Triple<Long, ClientDTO, TransportDocumentDTO>();
			p.setFirst(Data.nextDocID(TransportDocumentDTO.class));
			p.setSecond(Data.getClient(clientID));
			p.setThird(Data.getDoc(transportDocID, TransportDocumentDTO.class));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewCreditNoteForClientOpData(Long clientID,
			AsyncCallback<Pair<Long, ClientDTO>> callback) {
		try {
			Pair<Long, ClientDTO> p = new Pair<Long, ClientDTO>();
			p.setFirst(Data.nextDocID(CreditNoteDTO.class));
			p.setSecond(Data.getClient(clientID));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewCreditNoteFromInvoiceOpData(Long invoiceID,
			AsyncCallback<Pair<Long, InvoiceDTO>> callback) {
		try {
			Pair<Long, InvoiceDTO> p = new Pair<Long, InvoiceDTO>();
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(Data.getDoc(invoiceID, InvoiceDTO.class));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

}
