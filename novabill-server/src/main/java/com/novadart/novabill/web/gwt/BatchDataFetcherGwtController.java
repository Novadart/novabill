package com.novadart.novabill.web.gwt;

import com.novadart.novabill.domain.Client;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.facade.*;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BatchDataFetcherGwtController extends AbstractGwtController implements BatchDataFetcherGwtService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ClientGwtService clientService;
	
	@Autowired
	private InvoiceGwtService invoiceService;
	
	@Autowired
	private EstimationGwtService estimationService;
	
	@Autowired
	private TransportDocumentGwtService transportDocService;
	
	@Autowired
	private CreditNoteGwtService creditNoteService;
	
	@Autowired
	private PaymentTypeGwtService paymentTypeService;

	@Autowired
	private PriceListService priceListService;
	
	@Autowired
	private UtilsService utilsService;
	
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

	@Override
	public Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO> fetchNewInvoiceFromTransportDocumentsOpData(List<Long> transportDocumentIDs) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		if(transportDocumentIDs.size() == 0)
			throw new IllegalArgumentException();
		List<TransportDocumentDTO> transportDocDTOs = transportDocService.getAllWithIDs(transportDocumentIDs);
		Long defaultPaymentTypeID = transportDocDTOs.get(0).getClient().getDefaultPaymentTypeID(); 
		return new Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO>(invoiceService.getNextInvoiceDocumentID(),
				transportDocDTOs, defaultPaymentTypeID == null? null: paymentTypeService.get(defaultPaymentTypeID));
	}

	@Override
	public Pair<PriceListDTO, List<PriceListDTO>> fetchSelectCommodityForDocItemOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<PriceListDTO, List<PriceListDTO>>(priceListService.get(Client.findClient(clientID).getDefaultPriceList().getId()),
				priceListService.getAll(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()));
	}

	@Override
	public Pair<Long, EstimationDTO> fetchNewTransportDocumentFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new Pair<Long, EstimationDTO>(transportDocService.getNextTransportDocId(), estimationService.get(estimationID));
	}
	
}
