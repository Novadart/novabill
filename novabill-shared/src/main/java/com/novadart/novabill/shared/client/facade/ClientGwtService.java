package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("client.rpc")
public interface ClientGwtService extends RemoteService{

	public void remove(Long businessID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, DataAccessException;
	
	public Long add(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException;
	
	public void update(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException;
	
	public ClientDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;
	
	public PageDTO<ClientDTO> searchClients(Long businessID, String query, int start, int offset) throws InvalidArgumentException, NotAuthenticatedException, DataAccessException;

}
