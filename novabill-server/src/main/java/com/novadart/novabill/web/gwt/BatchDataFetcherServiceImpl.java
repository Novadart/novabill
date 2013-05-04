package com.novadart.novabill.web.gwt;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherService;
import com.novadart.novabill.shared.client.facade.ClientService;
import com.novadart.novabill.shared.client.facade.CreditNoteService;
import com.novadart.novabill.shared.client.facade.EstimationService;
import com.novadart.novabill.shared.client.facade.InvoiceService;
import com.novadart.novabill.shared.client.facade.PaymentTypeService;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class BatchDataFetcherServiceImpl extends AbstractGwtController implements BatchDataFetcherService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private EstimationService estimationService;
	
	@Autowired
	private TransportDocumentService transportDocService;
	
	@Autowired
	private CreditNoteService creditNoteService;
	
	@Autowired
	private PaymentTypeService paymentTypeService;

	private PaymentTypeDTO getDefaultPaymentTypeDTO(Long paymentTypeID) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		return paymentTypeID == null? null: paymentTypeService.get(paymentTypeID);
	}
	
	@Override
	public Triple<Long, ClientDTO, PaymentTypeDTO> fetchNewInvoiceForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		ClientDTO clientDTO = clientService.get(clientID);
		return new Triple<Long, ClientDTO, PaymentTypeDTO>(invoiceService.getNextInvoiceDocumentID(), clientDTO, getDefaultPaymentTypeDTO(clientDTO.getDefaultPaymentTypeID()));
	}

	@Override
	public Triple<Long, EstimationDTO, PaymentTypeDTO> fetchNewInvoiceFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		EstimationDTO estimationtDTO = estimationService.get(estimationID);
		return new Triple<Long, EstimationDTO, PaymentTypeDTO>(invoiceService.getNextInvoiceDocumentID(), estimationtDTO,
				getDefaultPaymentTypeDTO(estimationtDTO.getClient().getDefaultPaymentTypeID()));
	}

	@Override
	public Triple<Long, TransportDocumentDTO, PaymentTypeDTO> fetchNewInvoiceFromTransportDocumentOpData(Long transportDocumentID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		TransportDocumentDTO transDocDTO = transportDocService.get(transportDocumentID);
		return new Triple<Long, TransportDocumentDTO, PaymentTypeDTO>(invoiceService.getNextInvoiceDocumentID(), transDocDTO,
				getDefaultPaymentTypeDTO(transDocDTO.getClient().getDefaultPaymentTypeID()));
	}

	@Override
	public Triple<Long, ClientDTO, InvoiceDTO> fetchCloneInvoiceOpData(Long invoiceID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Triple<Long, ClientDTO, InvoiceDTO>(invoiceService.getNextInvoiceDocumentID(), clientService.get(clientID), invoiceService.get(invoiceID));
	}

	@Override
	public Pair<Long, ClientDTO> fetchNewEstimationForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, ClientDTO>(estimationService.getNextEstimationId(), clientService.get(clientID));
	}

	@Override
	public Triple<Long, ClientDTO, EstimationDTO> fetchCloneEstimationOpData(Long estimationID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Triple<Long, ClientDTO, EstimationDTO>(estimationService.getNextEstimationId(), clientService.get(clientID), estimationService.get(estimationID));
	}

	@Override
	public Pair<Long, ClientDTO> fetchNewTransportDocumentForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, ClientDTO>(transportDocService.getNextTransportDocId(), clientService.get(clientID));
	}

	@Override
	public Triple<Long, ClientDTO, TransportDocumentDTO> fetchCloneTransportDocumentOpData(Long transportDocID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Triple<Long, ClientDTO, TransportDocumentDTO>(transportDocService.getNextTransportDocId(), clientService.get(clientID), transportDocService.get(transportDocID));
	}

	@Override
	public Pair<Long, ClientDTO> fetchNewCreditNoteForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, ClientDTO>(creditNoteService.getNextCreditNoteDocumentID(), clientService.get(clientID));
	}

	@Override
	public Pair<Long, InvoiceDTO> fetchNewCreditNoteFromInvoiceOpData(Long invoiceID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, InvoiceDTO>(creditNoteService.getNextCreditNoteDocumentID(), invoiceService.get(invoiceID));
	}
	
}
