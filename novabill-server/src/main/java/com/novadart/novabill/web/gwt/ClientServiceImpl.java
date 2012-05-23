package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.CheckQuotas;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.ClientDTOFactory;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.quota.NumberOfClientsQuotaReachedChecker;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.QuotaException;
import com.novadart.novabill.shared.client.facade.ClientService;

public class ClientServiceImpl extends AbstractGwtController<ClientService, ClientServiceImpl> implements ClientService {

	private static final long serialVersionUID = -5418569389456426364L;

	@Autowired
	private UtilsService utilsService;
	
	public ClientServiceImpl() {
		super(ClientService.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClientDTO> getAll() {
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId()); 
		Set<Client> clients = business.getClients();
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>(clients.size());
		for(Client client: clients)
			clientDTOs.add(ClientDTOFactory.toDTO(client));
		return clientDTOs;
	}

	@Override
	@Transactional(readOnly = false)
	public void remove(Long id) throws DataAccessException, NoSuchObjectException, DataIntegrityException {
		Client client = Client.findClient(id);
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		if(client.getInvoices().size() > 0)
			throw new DataIntegrityException();
		client.remove();
		if(Hibernate.isInitialized(client.getBusiness().getClients()))
			client.getBusiness().getClients().remove(client);
	}

	@Override
	@Transactional(readOnly = false)
	@CheckQuotas(checkers = {NumberOfClientsQuotaReachedChecker.class})
	public Long add(ClientDTO clientDTO) throws QuotaException {
		Client client = new Client(); 
		ClientDTOFactory.copyFromDTO(client, clientDTO);
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
		client.setBusiness(business);
		business.getClients().add(client);
		client.persist();
		client.flush();
		return client.getId();
	}

	@Override
	@Transactional(readOnly = false)
	public void update(ClientDTO clientDTO) throws DataAccessException, NoSuchObjectException {
		Client client = Client.findClient(clientDTO.getId());
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		ClientDTOFactory.copyFromDTO(client, clientDTO);
		client.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public ClientDTO get(Long id) throws DataAccessException, NoSuchObjectException {
		Client client = Client.findClient(id);
		if(client == null) 
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		return ClientDTOFactory.toDTO(client);
	}

	@Override
	@Transactional(readOnly = true)
	public ClientDTO getFromInvoiceId(Long id) throws DataAccessException, NoSuchObjectException {
		Client client = Invoice.findInvoice(id).getClient();
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		return ClientDTOFactory.toDTO(client);
	}

	@Override
	public List<ClientDTO> searchClients(String query) throws InvalidArgumentException {
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
		List<Client> clients = null;
		try{
			clients = business.searchClients(query);
		}catch (Exception e) {
			throw new InvalidArgumentException();
		}
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>();
		for(Client client: clients)
			clientDTOs.add(ClientDTOFactory.toDTO(client));
		return clientDTOs;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ClientDTO getFromEstimationId(Long id) throws DataAccessException, NotAuthenticatedException,NoSuchObjectException, ConcurrentAccessException {
		Client client = Estimation.findEstimation(id).getClient();
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		return ClientDTOFactory.toDTO(client);
	}

}
