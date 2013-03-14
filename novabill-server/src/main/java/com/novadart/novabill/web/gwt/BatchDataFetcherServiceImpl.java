package com.novadart.novabill.web.gwt;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.rpc.server.WebModeClientOracle.Triple;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherService;
import com.novadart.novabill.shared.client.facade.ClientService;
import com.novadart.novabill.shared.client.facade.EstimationService;
import com.novadart.novabill.shared.client.facade.InvoiceService;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;
import com.novadart.novabill.shared.client.tuple.Pair;

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

	@Override
	public Pair<Long, ClientDTO> fetchNewInvoiceForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, ClientDTO>(invoiceService.getNextInvoiceDocumentID(), clientService.get(clientID));
	}

	@Override
	public Pair<Long, EstimationDTO> fetchNewInvoiceFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, EstimationDTO>(invoiceService.getNextInvoiceDocumentID(), estimationService.get(estimationID));
	}

	@Override
	public Pair<Long, TransportDocumentDTO> fetchNewInvoiceFromTransportDocumentOpData(Long transportDocumentID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, TransportDocumentDTO>(invoiceService.getNextInvoiceDocumentID(), transportDocService.get(transportDocumentID));
	}

	@Override
	public Triple<Long, ClientDTO, InvoiceDTO> fetchCloneInvoiceOpData(Long invoiceID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Triple<Long, ClientDTO, InvoiceDTO>(invoiceService.getNextInvoiceDocumentID(), clientService.get(clientID), invoiceService.get(invoiceID));
	}
	
}
