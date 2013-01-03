package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Endpoint;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.DTOUtils.Predicate;
import com.novadart.novabill.domain.dto.factory.AccountingDocumentItemDTOFactory;
import com.novadart.novabill.domain.dto.factory.TransportDocumentDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.AccountingDocumentValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;

public class TransportDocumentServiceImpl implements TransportDocumentService {


	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private AccountingDocumentValidator validator;
	
	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id)?.business?.id == principal.business.id")
	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return DTOUtils.findDocumentInCollection(businessService.getTransportDocuments(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()), id);
	}
	
	private static class EqualsClientIDPredicate implements Predicate<TransportDocumentDTO>{
		
		private Long id;
		
		public EqualsClientIDPredicate(Long id) {
			this.id = id;
		}
		
		@Override
		public boolean isTrue(TransportDocumentDTO doc) {
			return doc.getClient().getId().equals(id);
		}
		
	}

	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<TransportDocumentDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return new ArrayList<TransportDocumentDTO>(DTOUtils.filter(businessService.getTransportDocuments(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()), new EqualsClientIDPredicate(clientID)));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	//@Restrictions(checkers = {NumberOfTransportDocsPerYearQuotaReachedChecker.class})
	@PreAuthorize("#transportDocDTO?.business?.id == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Client).findClient(#transportDocDTO?.client?.id)?.business?.id == principal.business.id and " +
		  	  	  "#transportDocDTO != null and #transportDocDTO.id == null")
	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, AuthorizationException, ValidationException {
		TransportDocument transportDoc = new TransportDocument();
		transportDoc.setFromEndpoint(new Endpoint());
		transportDoc.setToEndpoint(new Endpoint());
		TransportDocumentDTOFactory.copyFromDTO(transportDoc, transportDocDTO, true);
		validator.validate(TransportDocument.class, transportDoc);
		Client client = Client.findClient(transportDocDTO.getClient().getId());
		transportDoc.setClient(client);
		client.getTransportDocuments().add(transportDoc);
		Business business = Business.findBusiness(transportDocDTO.getBusiness().getId());
		transportDoc.setBusiness(business);
		business.getTransportDocuments().add(transportDoc);
		transportDoc.persist();
		transportDoc.flush();
		return transportDoc.getId();
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	 "T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id)?.business?.id == #businessID and " +
		  	  	 "T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException {
		TransportDocument transportDoc = TransportDocument.findTransportDocument(id);
		transportDoc.remove();
		if(Hibernate.isInitialized(transportDoc.getBusiness().getTransportDocuments()))
			transportDoc.getBusiness().getTransportDocuments().remove(transportDoc);
		if(Hibernate.isInitialized(transportDoc.getClient().getTransportDocuments()))
			transportDoc.getClient().getTransportDocuments().remove(transportDoc);
		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#transportDocDTO?.business?.id == principal.business.id and " +
	  	  	  	  "T(com.novadart.novabill.domain.Client).findClient(#transportDocDTO?.client?.id)?.business?.id == principal.business.id and " +
	  	  	  	  "#transportDocDTO?.id != null")
	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException,
			ValidationException {
		TransportDocument persistedTransportDoc = TransportDocument.findTransportDocument(transportDocDTO.getId());
		if(persistedTransportDoc == null)
			throw new NoSuchObjectException();
		TransportDocumentDTOFactory.copyFromDTO(persistedTransportDoc, transportDocDTO, false);
		persistedTransportDoc.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: transportDocDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOFactory.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedTransportDoc);
			persistedTransportDoc.getAccountingDocumentItems().add(item);
		}
		validator.validate(TransportDocument.class, persistedTransportDoc);
		
	}

	@Override
	public Long getNextTransportDocId() throws NotAuthenticatedException {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextTransportDocDocumentID();
	}

	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		List<TransportDocumentDTO> allTransportDocs = getAllForClient(clientID);
		return new PageDTO<TransportDocumentDTO>(DTOUtils.range(allTransportDocs, start, length), start, length, new Long(allTransportDocs.size()));
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException {
		List<TransportDocumentDTO> allTransportDocs = businessService.getTransportDocuments(businessID);
		return new PageDTO<TransportDocumentDTO>(DTOUtils.range(allTransportDocs, start, length), start, length, new Long(allTransportDocs.size()));
	}

}
