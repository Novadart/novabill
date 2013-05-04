package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

@XsrfProtect
@RemoteServiceRelativePath("batchdatafetcher.rpc")
public interface BatchDataFetcherService extends RemoteService {
	
	public Triple<Long, ClientDTO, PaymentTypeDTO> fetchNewInvoiceForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Triple<Long, EstimationDTO, PaymentTypeDTO> fetchNewInvoiceFromEstimationOpData(Long estimationID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Triple<Long, TransportDocumentDTO, PaymentTypeDTO> fetchNewInvoiceFromTransportDocumentOpData(Long transportDocumentID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Triple<Long, ClientDTO, InvoiceDTO> fetchCloneInvoiceOpData(Long invoiceID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Pair<Long, ClientDTO> fetchNewEstimationForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Triple<Long, ClientDTO, EstimationDTO> fetchCloneEstimationOpData(Long estimationID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Pair<Long, ClientDTO> fetchNewTransportDocumentForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Triple<Long, ClientDTO, TransportDocumentDTO> fetchCloneTransportDocumentOpData(Long transportDocID, Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Pair<Long, ClientDTO> fetchNewCreditNoteForClientOpData(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Pair<Long, InvoiceDTO> fetchNewCreditNoteFromInvoiceOpData(Long invoiceID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;

}
