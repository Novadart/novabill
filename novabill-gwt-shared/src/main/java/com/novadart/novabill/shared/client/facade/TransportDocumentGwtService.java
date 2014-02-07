package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("transportdoc.rpc")
public interface TransportDocumentGwtService extends RemoteService {
	
	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public List<TransportDocumentDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, AuthorizationException, ValidationException;
	
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException;
	
	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException;
	
	public Long getNextTransportDocId() throws NotAuthenticatedException, DataAccessException;
	
	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;
	
	public List<TransportDocumentDTO>  getAllWithIDs(List<Long> ids) throws DataAccessException, NoSuchObjectException;
	
	public void setInvoice(Long businessID, Long invoiceID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException;
	
	public void clearInvoice(Long businessID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException;
	
}
