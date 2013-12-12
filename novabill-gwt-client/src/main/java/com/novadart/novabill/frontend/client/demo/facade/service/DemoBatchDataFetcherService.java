package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtServiceAsync;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class DemoBatchDataFetcherService implements
		BatchDataFetcherGwtServiceAsync {

	@Override
	public void fetchNewInvoiceForClientOpData(Long clientID,
			AsyncCallback<Triple<Long, ClientDTO, PaymentTypeDTO>> callback) {
		try {
			Triple<Long, ClientDTO, PaymentTypeDTO> p = new Triple<Long, ClientDTO, PaymentTypeDTO>();
			ClientDTO client = Data.getClient(clientID);
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(client);
			p.setThird(client.getDefaultPaymentTypeID() == null 
					? null 
					: Data.getPayment(client.getDefaultPaymentTypeID()));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewInvoiceFromEstimationOpData(Long estimationID,
			AsyncCallback<Triple<Long, EstimationDTO, PaymentTypeDTO>> callback) {
		try {
			Triple<Long, EstimationDTO, PaymentTypeDTO> p = new Triple<Long, EstimationDTO, PaymentTypeDTO>();
			EstimationDTO estimation = Data.getDoc(estimationID, EstimationDTO.class);
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(estimation);
			ClientDTO client = estimation.getClient();
			p.setThird(client.getDefaultPaymentTypeID() == null 
					? null 
					: Data.getPayment(client.getDefaultPaymentTypeID()));
			callback.onSuccess(p);
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void fetchNewInvoiceFromTransportDocumentOpData(
			Long transportDocumentID,
			AsyncCallback<Triple<Long, TransportDocumentDTO, PaymentTypeDTO>> callback) {
		try {
			Triple<Long, TransportDocumentDTO, PaymentTypeDTO> p = new Triple<Long, TransportDocumentDTO, PaymentTypeDTO>();
			TransportDocumentDTO td = Data.getDoc(transportDocumentID, TransportDocumentDTO.class);
			p.setFirst(Data.nextDocID(InvoiceDTO.class));
			p.setSecond(td);
			ClientDTO client = td.getClient();
			p.setThird(client.getDefaultPaymentTypeID() == null 
					? null 
					: Data.getPayment(client.getDefaultPaymentTypeID()));
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

	
	@Override
	public void fetchNewInvoiceFromTransportDocumentsOpData(
			List<Long> transportDocumentIDs,
			AsyncCallback<Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchModifyPriceList(
			Long businessID,
			Long priceListID,
			AsyncCallback<Pair<PriceListDTO, Map<String, Pair<String, PriceDTO>>>> callback) {
		// TODO Auto-generated method stub
		
	}

}
