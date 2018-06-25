package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("transportdoc.rpc")
public interface TransportDocumentGwtService extends RemoteService {
	
	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public List<TransportDocumentDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException, ValidationException;
	
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException, DataIntegrityException, FreeUserAccessForbiddenException;
	
	public Long getNextTransportDocId() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<TransportDocumentDTO>  getAllWithIDs(List<Long> ids) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException;
	
	public void setInvoice(Long businessID, Long invoiceID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException;
	
	public void clearInvoice(Long businessID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException;
	
}
