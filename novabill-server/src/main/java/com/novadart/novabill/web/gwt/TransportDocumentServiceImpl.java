package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.NumberOfTransportDocsPerYearQuotaReachedChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.factory.AccountingDocumentItemDTOFactory;
import com.novadart.novabill.domain.dto.factory.TransportDocumentDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.AccountingDocumentValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;

@SuppressWarnings("serial")
public class TransportDocumentServiceImpl extends AbstractGwtController<TransportDocumentService, TransportDocumentServiceImpl> implements TransportDocumentService {


	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private AccountingDocumentValidator validator;
	
	public TransportDocumentServiceImpl() {
		super(TransportDocumentService.class);
	}

	@Override
	public TransportDocumentDTO get(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		TransportDocument estimation = TransportDocument.findTransportDocument(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(estimation.getBusiness().getId()))
			throw new DataAccessException();
		return TransportDocumentDTOFactory.toDTO(estimation);
	}

	@Override
	public List<TransportDocumentDTO> getAllForClient(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		Client client = Client.findClient(id);
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<TransportDocument> transportDocs = client.getSortedTransportDocuments();
		List<TransportDocumentDTO> transportDocDTOs = new ArrayList<TransportDocumentDTO>(transportDocs.size());
		for(TransportDocument transportDoc: transportDocs)
			transportDocDTOs.add(TransportDocumentDTOFactory.toDTO(transportDoc));
		return transportDocDTOs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@Restrictions(checkers = {NumberOfTransportDocsPerYearQuotaReachedChecker.class})
	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, ConcurrentAccessException, AuthorizationException, ValidationException {
		TransportDocument transportDoc = new TransportDocument();
		Client client = Client.findClient(transportDocDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		transportDoc.setClient(client);
		client.getTransportDocuments().add(transportDoc);
		Business business = Business.findBusiness(transportDocDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
		transportDoc.setBusiness(business);
		business.getTransportDocuments().add(transportDoc);
		TransportDocumentDTOFactory.copyFromDTO(transportDoc, transportDocDTO, true);
		transportDoc.setDocumentID(business.getNextTransportDocDocumentID());
		validator.validate(transportDoc);
		transportDoc.persist();
		transportDoc.flush();
		return transportDoc.getId();
	}

	@Override
	public void remove(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException {
		TransportDocument transportDoc = TransportDocument.findTransportDocument(id);
		if(transportDoc == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(transportDoc.getBusiness().getId()))
			throw new DataAccessException();
		transportDoc.remove();
		if(Hibernate.isInitialized(transportDoc.getBusiness().getTransportDocuments()))
			transportDoc.getBusiness().getTransportDocuments().remove(transportDoc);
		if(Hibernate.isInitialized(transportDoc.getClient().getTransportDocuments()))
			transportDoc.getClient().getTransportDocuments().remove(transportDoc);
		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException,
			ValidationException {
		if(transportDocDTO.getId() == null)
			throw new DataAccessException();
		Client client = Client.findClient(transportDocDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(transportDocDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
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
		validator.validate(persistedTransportDoc);
		
	}

	@Override
	public Long getNextTransportDocId() throws NotAuthenticatedException, ConcurrentAccessException {
		return utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getNextTransportDocDocumentID();
	}

	@Override
	public PageDTO<TransportDocumentDTO> getAllForClientInRange(long id, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException,
			ConcurrentAccessException {
		Client client = Client.findClient(id);
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<TransportDocument> transportDocs = client.getAllTransportDocsInRange(start, length);
		List<TransportDocumentDTO> transportDocDTOs = new ArrayList<TransportDocumentDTO>(transportDocs.size());
		for(TransportDocument transportDoc: transportDocs)
			transportDocDTOs.add(TransportDocumentDTOFactory.toDTO(transportDoc));
		return new PageDTO<TransportDocumentDTO>(transportDocDTOs, start, length, TransportDocument.countTransportDocumentsForClient(id));
	}

	@Override
	public PageDTO<TransportDocumentDTO> getAllInRange(int start, int length) throws NotAuthenticatedException, ConcurrentAccessException {
		List<TransportDocument> transportDocs = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId()).getAllTransportDocsInRange(start, length);
		List<TransportDocumentDTO> transportDocDTOs = new ArrayList<TransportDocumentDTO>(transportDocs.size());
		for(TransportDocument transportDoc: transportDocs)
			transportDocDTOs.add(TransportDocumentDTOFactory.toDTO(transportDoc));
		return new PageDTO<TransportDocumentDTO>(transportDocDTOs, start, length, (int)TransportDocument.countTransportDocuments());
	}

}
