package com.novadart.novabill.web.gwt;

import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.DocumentIDClass;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.DocumentIDClassService;
import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
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
	private DocumentIDClassService docIDClassService;

	@Autowired
	private UtilsService utilsService;
	
	private PaymentTypeDTO getDefaultPaymentTypeDTO(Long paymentTypeID) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return paymentTypeID == null? null: paymentTypeService.get(paymentTypeID);
	}

	private String getInvoiceDocumentIDSuffixForClient(Long clientID) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		Long documentIDClassID = clientService.get(clientID).getDefaultDocumentIDClassID();
		return documentIDClassID == null? null: DocumentIDClass.findDocumentIDClass(documentIDClassID).getSuffix();
	}

	@Override
	public Triple<Long, ClientDTO, PaymentTypeDTO> fetchNewInvoiceForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		ClientDTO clientDTO = clientService.get(clientID);
		String suffix = getInvoiceDocumentIDSuffixForClient(clientID);
		return new Triple<>(invoiceService.getNextInvoiceDocumentID(suffix), clientDTO, getDefaultPaymentTypeDTO(clientDTO.getDefaultPaymentTypeID()));
	}

	@Override
	public Triple<Long, EstimationDTO, PaymentTypeDTO> fetchNewInvoiceFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		EstimationDTO estimationDTO = estimationService.get(estimationID);
		String suffix = getInvoiceDocumentIDSuffixForClient(estimationDTO.getClient().getId());
		return new Triple<>(invoiceService.getNextInvoiceDocumentID(suffix), estimationDTO,
				getDefaultPaymentTypeDTO(estimationDTO.getClient().getDefaultPaymentTypeID()));
	}

	@Override
	public Triple<Long, TransportDocumentDTO, PaymentTypeDTO> fetchNewInvoiceFromTransportDocumentOpData(Long transportDocumentID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		TransportDocumentDTO transDocDTO = transportDocService.get(transportDocumentID);
		String suffix = getInvoiceDocumentIDSuffixForClient(transDocDTO.getClient().getId());
		return new Triple<>(invoiceService.getNextInvoiceDocumentID(suffix), transDocDTO,
				getDefaultPaymentTypeDTO(transDocDTO.getClient().getDefaultPaymentTypeID()));
	}

	@Override
	public Triple<Long, ClientDTO, InvoiceDTO> fetchCloneInvoiceOpData(Long invoiceID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		String suffix = getInvoiceDocumentIDSuffixForClient(clientID);
		return new Triple<>(invoiceService.getNextInvoiceDocumentID(suffix), clientService.get(clientID), invoiceService.get(invoiceID));
	}

	@Override
	public Pair<Long, ClientDTO> fetchNewEstimationForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Pair<>(estimationService.getNextEstimationId(), clientService.get(clientID));
	}

	@Override
	public Triple<Long, ClientDTO, EstimationDTO> fetchCloneEstimationOpData(Long estimationID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Triple<>(estimationService.getNextEstimationId(), clientService.get(clientID), estimationService.get(estimationID));
	}

	@Override
	public Pair<Long, ClientDTO> fetchNewTransportDocumentForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Pair<>(transportDocService.getNextTransportDocId(), clientService.get(clientID));
	}

	@Override
	public Triple<Long, ClientDTO, TransportDocumentDTO> fetchCloneTransportDocumentOpData(Long transportDocID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Triple<>(transportDocService.getNextTransportDocId(), clientService.get(clientID), transportDocService.get(transportDocID));
	}

	@Override
	public Pair<Long, ClientDTO> fetchNewCreditNoteForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Pair<>(creditNoteService.getNextCreditNoteDocumentID(), clientService.get(clientID));
	}

	@Override
	public Pair<Long, InvoiceDTO> fetchNewCreditNoteFromInvoiceOpData(Long invoiceID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Pair<>(creditNoteService.getNextCreditNoteDocumentID(), invoiceService.get(invoiceID));
	}

	@Override
	public Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO> fetchNewInvoiceFromTransportDocumentsOpData(List<Long> transportDocumentIDs) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		if(transportDocumentIDs.size() == 0)
			throw new IllegalArgumentException();
		List<TransportDocumentDTO> transportDocDTOs = transportDocService.getAllWithIDs(transportDocumentIDs);
		Long defaultPaymentTypeID = transportDocDTOs.get(0).getClient().getDefaultPaymentTypeID();
		Long defaultDocumentIDClassID = transportDocDTOs.get(0).getClient().getDefaultDocumentIDClassID();
		String suffix = defaultDocumentIDClassID == null? null:
				docIDClassService.get(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), defaultDocumentIDClassID).getSuffix();
		return new Triple<>(invoiceService.getNextInvoiceDocumentID(suffix),
				transportDocDTOs, defaultPaymentTypeID == null? null: paymentTypeService.get(defaultPaymentTypeID));
	}

	@Override
	public Pair<PriceListDTO, List<PriceListDTO>> fetchSelectCommodityForDocItemOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Pair<>(priceListService.get(Client.findClient(clientID).getDefaultPriceList().getId()),
				priceListService.getAll(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()));
	}

	@Override
	public Pair<Long, EstimationDTO> fetchNewTransportDocumentFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new Pair<>(transportDocService.getNextTransportDocId(), estimationService.get(estimationID));
	}
	
}
