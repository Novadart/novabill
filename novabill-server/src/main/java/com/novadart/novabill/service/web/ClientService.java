package com.novadart.novabill.service.web;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.TaxableEntityValidator;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class ClientService {

	@Autowired
	private TaxableEntityValidator validator;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UtilsService utilsService;
	
	@Transactional(readOnly = false)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#id)?.business?.id == principal.business.id and " +
				  "principal.business.id == #businessID")
	public void remove(Long businessID, Long id) throws NoSuchObjectException, DataIntegrityException {
		Client client = Client.findClient(id);
		if(client.hasAccountingDocs())
			throw new DataIntegrityException();
		client.remove();
		if(Hibernate.isInitialized(client.getBusiness().getClients()))
			client.getBusiness().getClients().remove(client);
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	//@Restrictions(checkers = {NumberOfClientsQuotaReachedChecker.class})
	@PreAuthorize("#businessID == principal.business.id and #clientDTO != null and #clientDTO.id == null")
	public Long add(Long businessID, ClientDTO clientDTO) throws AuthorizationException, ValidationException {
		Client client = new Client(); 
		ClientDTOFactory.copyFromDTO(client, clientDTO);
		if(clientDTO.getDefaultPriceListID() == null)
			client.setDefaultPriceList(PriceList.getDefaultPriceList(businessID));
		else
			client.setDefaultPriceList(PriceList.findPriceList(clientDTO.getDefaultPriceListID()));
		validator.validate(client);
		Business business = Business.findBusiness(businessID);
		client.setBusiness(business);
		business.getClients().add(client);
		client.persist();
		client.flush();
		return client.getId();
	}

	
	private void updateDefaultPaymentType(ClientDTO clientDTO, Client client) {
		if(client.getDefaultPaymentType() == null){
			if(clientDTO.getDefaultPaymentTypeID() != null){
				PaymentType paymentType = PaymentType.findPaymentType(clientDTO.getDefaultPaymentTypeID());
				client.setDefaultPaymentType(paymentType);
				paymentType.getClients().add(client);
			}
		}else{
			if(client.getDefaultPaymentType().getId() != clientDTO.getDefaultPaymentTypeID()){
				if(Hibernate.isInitialized(client.getDefaultPaymentType().getClients()))
					client.getDefaultPaymentType().getClients().remove(client);
				client.setDefaultPaymentType(null);
				if(clientDTO.getDefaultPaymentTypeID() != null){
					PaymentType newDefaultPaymentType = PaymentType.findPaymentType(clientDTO.getDefaultPaymentTypeID());
					client.setDefaultPaymentType(newDefaultPaymentType);
					newDefaultPaymentType.getClients().add(client);
				}
			}
		}
	}
	
	private void updateDefaultPriceList(ClientDTO clientDTO, Client client){
		if(client.getDefaultPriceList() == null){
			if(clientDTO.getDefaultPriceListID() != null){
				PriceList priceList = PriceList.findPriceList(clientDTO.getDefaultPriceListID());
				client.setDefaultPriceList(priceList);
				priceList.getClients().add(client);
			}
		} else {
			if(client.getDefaultPriceList().getId() != clientDTO.getDefaultPriceListID()){
				if(Hibernate.isInitialized(client.getDefaultPriceList().getClients()))
					client.getDefaultPriceList().getClients().remove(client);
				client.setDefaultPriceList(null);
				if(clientDTO.getDefaultPriceListID() != null){
					PriceList newDefaultPriceList = PriceList.findPriceList(clientDTO.getDefaultPriceListID());
					client.setDefaultPriceList(newDefaultPriceList);
					newDefaultPriceList.getClients().add(client);
				}
			}
		}
	}
	
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("principal.business.id == #businessID and #clientDTO?.id != null and " + 
				  "T(com.novadart.novabill.domain.Client).findClient(#clientDTO?.id)?.business?.id == principal.business.id")
	public void update(Long businessID, ClientDTO clientDTO) throws NoSuchObjectException, ValidationException {
		Client client = Client.findClient(clientDTO.getId());
		ClientDTOFactory.copyFromDTO(client, clientDTO);
		updateDefaultPaymentType(clientDTO, client);
		updateDefaultPriceList(clientDTO, client);
		validator.validate(client);
		client.flush();
	}

	@Transactional(readOnly = true)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#id)?.business?.id == principal.business.id")
	public ClientDTO get(Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException {
		for(ClientDTO clientDTO: businessService.getClients(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()))
			if(clientDTO.getId().equals(id))
				return clientDTO;
		throw new NoSuchObjectException();
	}

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
	
}
