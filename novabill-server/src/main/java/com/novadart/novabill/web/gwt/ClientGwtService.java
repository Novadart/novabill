package com.novadart.novabill.web.gwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.ClientService;

@HandleGWTServiceAccessDenied
public class ClientGwtService extends AbstractGwtController implements ClientService {
	
	private static final long serialVersionUID = -5418569389456426364L;
	
	@Autowired
	@Qualifier("clientServiceImpl")
	private ClientService clientService;
	
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, DataAccessException {
		clientService.remove(businessID, id);
	}

	public Long add(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException {
		return clientService.add(businessID, clientDTO);
	}

	public void update(Long businessID, ClientDTO clientDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException {
		clientService.update(businessID, clientDTO);
	}

	public ClientDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		return clientService.get(id);
	}

	public PageDTO<ClientDTO> searchClients(Long businessID, String query, int start, int offset) throws InvalidArgumentException, NotAuthenticatedException, DataAccessException {
		return clientService.searchClients(businessID, query, start, offset);
	}

}
