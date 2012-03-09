package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@RemoteServiceRelativePath("client.rpc")
public interface ClientService extends RemoteService{

	public List<ClientDTO> getAll() throws NotAuthenticatedException;
	
	public void remove(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException;
	
	public Long add(ClientDTO clientDTO) throws NotAuthenticatedException;
	
	public void update(ClientDTO clientDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException;
	
	public ClientDTO get(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException;
	
	public ClientDTO getFromInvoiceId(Long invoiceId) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException;
	
	public List<ClientDTO> searchClients(String query) throws InvalidArgumentException;
	
}
