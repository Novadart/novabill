package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("transportdoc.rpc")
public interface TransportDocumentService extends RemoteService {
	
	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public List<TransportDocumentDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, ConcurrentAccessException, AuthorizationException, ValidationException;
	
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException, ValidationException;
	
	public Long getNextTransportDocId() throws NotAuthenticatedException, ConcurrentAccessException;
	
	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, ConcurrentAccessException;
	
}
