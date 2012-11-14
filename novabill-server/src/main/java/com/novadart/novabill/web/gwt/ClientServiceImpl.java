package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.TaxableEntityValidator;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.ClientService;

public class ClientServiceImpl extends AbstractGwtController<ClientService, ClientServiceImpl> implements ClientService {

	private static final long serialVersionUID = -5418569389456426364L;

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private TaxableEntityValidator validator;
	
	public ClientServiceImpl() {
		super(ClientService.class);
	}

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public List<ClientDTO> getAll(Long businessID) {
		Set<Client> clients = Business.findBusiness(businessID).getClients();
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>(clients.size());
		for(Client client: clients)
			clientDTOs.add(ClientDTOFactory.toDTO(client));
		return clientDTOs;
	}

	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#id)?.business?.id == principal.business.id and principal.business.id == #businessID")
	public void remove(Long businessID, Long id) throws DataAccessException, NoSuchObjectException, DataIntegrityException {
		Client client = Client.findClient(id);
		if(client.hasAccountingDocs())
			throw new DataIntegrityException();
		client.remove();
		if(Hibernate.isInitialized(client.getBusiness().getClients()))
			client.getBusiness().getClients().remove(client);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	//@Restrictions(checkers = {NumberOfClientsQuotaReachedChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public Long add(Long businessID, ClientDTO clientDTO) throws AuthorizationException, ValidationException {
		Client client = new Client(); 
		ClientDTOFactory.copyFromDTO(client, clientDTO);
		validator.validate(client);
		Business business = Business.findBusiness(businessID);
		client.setBusiness(business);
		business.getClients().add(client);
		client.persist();
		client.flush();
		return client.getId();
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientDTO?.id)?.business?.id == principal.business.id and principal.business.id == #businessID")
	public void update(Long businessID, ClientDTO clientDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		Client client = Client.findClient(clientDTO.getId());
		ClientDTOFactory.copyFromDTO(client, clientDTO);
		validator.validate(client);
		client.flush();
	}

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#id)?.business?.id == principal.business.id and principal.business.id == #businessID")
	public ClientDTO get(Long businessID, Long id) throws DataAccessException, NoSuchObjectException {
		Client client = Client.findClient(id);
		if(client == null) 
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		return ClientDTOFactory.toDTO(client);
	}

	@Override
	@Transactional(readOnly = true)
	public ClientDTO getFromInvoiceId(Long id) throws DataAccessException, NoSuchObjectException {
		Client client = Invoice.findInvoice(id).getClient();
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		return ClientDTOFactory.toDTO(client);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<ClientDTO> searchClients(Long businessID, String query, int start, int length) throws InvalidArgumentException {
		Business business = Business.findBusiness(businessID);
		PageDTO<Client> clients = null;
		try{
			clients = business.prefixClientSearch(query, start, length);
		}catch (Exception e) {
			throw new InvalidArgumentException();
		}
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>();
		for(Client client: clients.getItems())
			clientDTOs.add(ClientDTOFactory.toDTO(client));
		return new PageDTO<ClientDTO>(clientDTOs, start, length, clients.getTotal());
	}
	
	@Override
	@Transactional(readOnly = true)
	public ClientDTO getFromEstimationId(Long id) throws DataAccessException, NotAuthenticatedException,NoSuchObjectException, ConcurrentAccessException {
		Client client = Estimation.findEstimation(id).getClient();
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		return ClientDTOFactory.toDTO(client);
	}

}
