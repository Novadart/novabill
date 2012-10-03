package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.NumberOfEstimationsPerYearQuotaReachedChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.dto.factory.EstimationDTOFactory;
import com.novadart.novabill.domain.dto.factory.AccountingDocumentItemDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.AccountingDocumentValidator;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.EstimationService;

@SuppressWarnings("serial")
public class EstimationServiceImpl extends AbstractGwtController<EstimationService, EstimationServiceImpl> implements EstimationService {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private AccountingDocumentValidator validator;
	
	public EstimationServiceImpl() {
		super(EstimationService.class);
	}
	
	@Override
	public EstimationDTO get(long id) throws DataAccessException, NoSuchObjectException {
		Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(estimation.getBusiness().getId()))
			throw new DataAccessException();
		return EstimationDTOFactory.toDTO(estimation);
	}
	
	@Override
	public List<EstimationDTO> getAllForClient(long id) throws DataAccessException, NoSuchObjectException {
		Client client = Client.findClient(id);
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<Estimation> estimations = client.getSortedEstimations();
		List<EstimationDTO> estimationDTOs = new ArrayList<EstimationDTO>(estimations.size());
		for(Estimation estimation: estimations)
			estimationDTOs.add(EstimationDTOFactory.toDTO(estimation));
		return estimationDTOs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	//@Restrictions(checkers = {NumberOfEstimationsPerYearQuotaReachedChecker.class})
	public Long add(EstimationDTO estimationDTO) throws DataAccessException, AuthorizationException, ValidationException {
		Estimation estimation = new Estimation();
		Client client = Client.findClient(estimationDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		estimation.setClient(client);
		client.getEstimations().add(estimation);
		Business business = Business.findBusiness(estimationDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
		estimation.setBusiness(business);
		business.getEstimations().add(estimation);
		EstimationDTOFactory.copyFromDTO(estimation, estimationDTO, true);
		estimation.setDocumentID(business.getNextEstimationDocumentID());
		validator.validate(estimation);
		estimation.persist();
		estimation.flush();
		return estimation.getId();
	}

	@Override
	@Transactional(readOnly = false)
	public void remove(Long id) throws DataAccessException, NoSuchObjectException {
		Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(estimation.getBusiness().getId()))
			throw new DataAccessException();
		estimation.remove();
		if(Hibernate.isInitialized(estimation.getBusiness().getEstimations()))
			estimation.getBusiness().getEstimations().remove(estimation);
		if(Hibernate.isInitialized(estimation.getClient().getEstimations()))
			estimation.getClient().getEstimations().remove(estimation);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	public void update(EstimationDTO estimationDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		if(estimationDTO.getId() == null)
			throw new DataAccessException();
		Client client = Client.findClient(estimationDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(estimationDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
		Estimation persistedEstimation = Estimation.findEstimation(estimationDTO.getId());
		if(persistedEstimation == null)
			throw new NoSuchObjectException();
		EstimationDTOFactory.copyFromDTO(persistedEstimation, estimationDTO, false);
		persistedEstimation.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: estimationDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOFactory.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedEstimation);
			persistedEstimation.getAccountingDocumentItems().add(item);
		}
		validator.validate(persistedEstimation);
	}

	@Override
	public Long getNextEstimationId() throws NotAuthenticatedException, ConcurrentAccessException {
		return utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getNextEstimationDocumentID();
	}

	@Override
	public PageDTO<EstimationDTO> getAllForClientInRange(long id, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		Client client = Client.findClient(id);
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<Estimation> estimations = client.getAllEstimationsInRange(start, length);
		List<EstimationDTO> estimationDTOs = new ArrayList<EstimationDTO>(estimations.size());
		for(Estimation estimation: estimations)
			estimationDTOs.add(EstimationDTOFactory.toDTO(estimation));
		return new PageDTO<EstimationDTO>(estimationDTOs, start, length, Estimation.countEstimationsForClient(id));
	}

	@Override
	public PageDTO<EstimationDTO> getAllInRange(int start, int length) throws NotAuthenticatedException, ConcurrentAccessException {
		List<Estimation> estimations = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId()).getAllEstimationsInRange(start, length);
		List<EstimationDTO> estimationDTOs = new ArrayList<EstimationDTO>(estimations.size());
		for(Estimation estimation: estimations)
			estimationDTOs.add(EstimationDTOFactory.toDTO(estimation));
		return new PageDTO<EstimationDTO>(estimationDTOs, start, length, (int)Estimation.countEstimations());
	}

}
