package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@RemoteServiceRelativePath("client.rpc")
public interface ClientService extends RemoteService{

	public List<ClientDTO> getAll() throws NotAuthenticatedException, ConcurrentAccessException;
	
	public void remove(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, ConcurrentAccessException;
	
	public Long add(ClientDTO clientDTO) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public void update(ClientDTO clientDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
	public ClientDTO get(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
	public ClientDTO getFromInvoiceId(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
	public List<ClientDTO> searchClients(String query) throws InvalidArgumentException, NotAuthenticatedException, ConcurrentAccessException;

	public ClientDTO getFromEstimationId(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
}
