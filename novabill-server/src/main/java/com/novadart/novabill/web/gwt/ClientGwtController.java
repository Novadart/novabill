package com.novadart.novabill.web.gwt;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.ClientService;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.ClientGwtService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@HandleGWTServiceAccessDenied
public class ClientGwtController extends AbstractGwtController implements ClientGwtService {
	
	private static final long serialVersionUID = -5418569389456426364L;
	
	@Autowired
	private ClientService clientService;
	
	public boolean remove(Long businessID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return clientService.remove(businessID, id);
	}

	public Long add(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException {
		return clientService.add(businessID, clientDTO);
	}

	public void update(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		clientService.update(businessID, clientDTO);
	}

	public ClientDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return clientService.get(id);
	}

	public PageDTO<ClientDTO> searchClients(Long businessID, String query, int start, int offset) throws InvalidArgumentException, NotAuthenticatedException, DataAccessException {
		return clientService.searchClients(businessID, query, start, offset);
	}

	@Override
	public Long addClientAddress(ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException {
		return clientService.addClientAddress(clientAddressDTO);
	}

	@Override
	public List<ClientAddressDTO> getClientAddresses(Long clientID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return clientService.getClientAddresses(clientID);
	}

	@Override
	public void removeClientAddress(Long clientID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		clientService.removeClientAddress(clientID, id);
	}

	@Override
	public void updateClientAddress(ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException, ValidationException, DataAccessException {
		clientService.updateClientAddress(clientAddressDTO);
	}

}
