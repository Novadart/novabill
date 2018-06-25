package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

@XsrfProtect
@RemoteServiceRelativePath("batchdatafetcher.rpc")
public interface BatchDataFetcherGwtService extends RemoteService {
	
	public Triple<Long, ClientDTO, PaymentTypeDTO> fetchNewInvoiceForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Triple<Long, EstimationDTO, PaymentTypeDTO> fetchNewInvoiceFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Triple<Long, TransportDocumentDTO, PaymentTypeDTO> fetchNewInvoiceFromTransportDocumentOpData(Long transportDocumentID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Triple<Long, ClientDTO, InvoiceDTO> fetchCloneInvoiceOpData(Long invoiceID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Pair<Long, ClientDTO> fetchNewEstimationForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Triple<Long, ClientDTO, EstimationDTO> fetchCloneEstimationOpData(Long estimationID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Pair<Long, ClientDTO> fetchNewTransportDocumentForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Triple<Long, ClientDTO, TransportDocumentDTO> fetchCloneTransportDocumentOpData(Long transportDocID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Pair<Long, ClientDTO> fetchNewCreditNoteForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Pair<Long, InvoiceDTO> fetchNewCreditNoteFromInvoiceOpData(Long invoiceID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO> fetchNewInvoiceFromTransportDocumentsOpData(List<Long> transportDocumentIDs) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Pair<PriceListDTO, List<PriceListDTO>> fetchSelectCommodityForDocItemOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Pair<Long, EstimationDTO> fetchNewTransportDocumentFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;

}
