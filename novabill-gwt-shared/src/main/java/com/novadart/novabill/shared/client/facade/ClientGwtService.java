package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("client.rpc")
public interface ClientGwtService extends RemoteService{

	public boolean remove(Long businessID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Long add(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException;
	
	public void update(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException;
	
	public ClientDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException;
	
	public PageDTO<ClientDTO> searchClients(Long businessID, String query, int start, int offset) throws InvalidArgumentException, NotAuthenticatedException, DataAccessException;
	
	public Long addClientAddress(ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException;
	
	public List<ClientAddressDTO> getClientAddresses(Long clientID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public void removeClientAddress(Long clientID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public void updateClientAddress(ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException, ValidationException, DataAccessException;

}
