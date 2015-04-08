package com.novadart.novabill.service.web;

import java.util.ArrayList;
import java.util.List;

import com.novadart.novabill.domain.*;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.dto.transformer.ClientAddressDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.Groups.HeavyClient;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class ClientService {

	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UtilsService utilsService;
	
	@Transactional(readOnly = false)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#id)?.business?.id == principal.business.id and " +
				  "principal.business.id == #businessID")
	public boolean remove(Long businessID, Long id) throws NoSuchObjectException {
		Client client = Client.findClient(id);
		if(client.hasAccountingDocs())
			return false;
		client.remove();
		if(Hibernate.isInitialized(client.getBusiness().getClients()))
			client.getBusiness().getClients().remove(client);
		return true;
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	//@Restrictions(checkers = {NumberOfClientsQuotaReachedChecker.class})
	@PreAuthorize("#businessID == principal.business.id and #clientDTO != null and #clientDTO.id == null")
	public Long add(Long businessID, ClientDTO clientDTO) throws FreeUserAccessForbiddenException, ValidationException {
		Client client = new Client(); 
		ClientDTOTransformer.copyFromDTO(client, clientDTO);
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


	private void updateDefaultDocumentIDClass(ClientDTO clientDTO, Client client){
		if(client.getDefaultDocumentIDClass() == null){
			if(clientDTO.getDefaultDocumentIDClassID() != null){
				DocumentIDClass documentIDClass = DocumentIDClass.findDocumentIDClass(clientDTO.getDefaultDocumentIDClassID());
				client.setDefaultDocumentIDClass(documentIDClass);
				documentIDClass.getClients().add(client);
			}
		} else {
			if(client.getDefaultDocumentIDClass().getId() != clientDTO.getDefaultDocumentIDClassID()){
				if(Hibernate.isInitialized(client.getDefaultDocumentIDClass().getClients()))
					client.getDefaultDocumentIDClass().getClients().remove(client);
				client.setDefaultDocumentIDClass(null);
				if(clientDTO.getDefaultDocumentIDClassID() != null){
					DocumentIDClass documentIDClass = DocumentIDClass.findDocumentIDClass(clientDTO.getDefaultDocumentIDClassID());
					client.setDefaultDocumentIDClass(documentIDClass);
					documentIDClass.getClients().add(client);
				}
			}
		}
	}


	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("principal.business.id == #businessID and #clientDTO?.id != null and " + 
				  "T(com.novadart.novabill.domain.Client).findClient(#clientDTO?.id)?.business?.id == principal.business.id")
	public void update(Long businessID, ClientDTO clientDTO) throws NoSuchObjectException, ValidationException {
		Client client = Client.findClient(clientDTO.getId());
		ClientDTOTransformer.copyFromDTO(client, clientDTO);
		updateDefaultPaymentType(clientDTO, client);
		updateDefaultPriceList(clientDTO, client);
		updateDefaultDocumentIDClass(clientDTO, client);
		validator.validate(client, HeavyClient.class);
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
			clientDTOs.add(ClientDTOTransformer.toDTO(client));
		return new PageDTO<ClientDTO>(clientDTOs, start, length, clients.getTotal());
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientAddressDTO?.client?.id)?.business?.id == principal.business.id and " +
				  "#clientAddressDTO != null and #clientAddressDTO.id == null")
	public Long addClientAddress(ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException {
		ClientAddress clientAddress = new ClientAddress();
		ClientAddressDTOTransformer.copyFromDTO(clientAddress, clientAddressDTO);
		validator.validate(clientAddress);
		Client client = Client.findClient(clientAddressDTO.getClient().getId());
		if(Hibernate.isInitialized(client.getAddresses()))
			client.getAddresses().add(clientAddress);
		clientAddress.setClient(client);
		clientAddress.persist();
		clientAddress.flush();
		return clientAddress.getId();
	}
	
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<ClientAddressDTO> getClientAddresses(Long clientID) throws NotAuthenticatedException, DataAccessException {
		Client client = Client.findClient(clientID);
		List<ClientAddressDTO> clientAddressDTOs = new ArrayList<>(client.getAddresses().size());
		for(ClientAddress clientAddress: client.getAddresses())
			clientAddressDTOs.add(ClientAddressDTOTransformer.toDTO(clientAddress));
		return clientAddressDTOs;
	}
	
	@Transactional(readOnly = false)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id and " +
				  "T(com.novadart.novabill.domain.ClientAddress).findClientAddress(#id)?.client?.id == #clientID")
	public void removeClientAddress(Long clientID, Long id) throws NotAuthenticatedException, DataAccessException {
		ClientAddress clientAddress = ClientAddress.findClientAddress(id);
		clientAddress.remove();
		Client client = Client.findClient(clientID);
		if(Hibernate.isInitialized(client.getAddresses()))
			client.getAddresses().remove(clientAddress);
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientAddressDTO?.client?.id)?.business?.id == principal.business.id and " +
			      "#clientAddressDTO != null and #clientAddressDTO.id != null")
	public void updateClientAddress(ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, NoSuchObjectException {
		ClientAddress clientAddress = ClientAddress.findClientAddress(clientAddressDTO.getId());
		if(clientAddress == null)
			throw new NoSuchObjectException();
		ClientAddressDTOTransformer.copyFromDTO(clientAddress, clientAddressDTO);
		validator.validate(clientAddress);
	}
	
}
