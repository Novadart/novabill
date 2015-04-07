package com.novadart.novabill.service.web;

import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.DTOUtils.Predicate;
import com.novadart.novabill.domain.dto.transformer.AccountingDocumentItemDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.EstimationDTOTransformer;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.AccountingDocumentValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EstimationService {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private AccountingDocumentValidator validator;
	
	@PreAuthorize("T(com.novadart.novabill.domain.Estimation).findEstimation(#id)?.business?.id == principal.business.id")
	public EstimationDTO get(Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
		Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchElementException();
		return EstimationDTOTransformer.toDTO(estimation, true);
	}
	
	private static class EqualsClientIDPredicate implements Predicate<EstimationDTO>{
		
		private Long id;
		
		public EqualsClientIDPredicate(Long id) {
			this.id = id;
		}
		
		@Override
		public boolean isTrue(EstimationDTO doc) {
			return doc.getClient().getId().equals(id);
		}
		
	}
	
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<EstimationDTO> getAllForClient(Long clientID, Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
		return new ArrayList<EstimationDTO>(DTOUtils.filter(businessService.getEstimations(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), year), new EqualsClientIDPredicate(clientID)));
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	//@Restrictions(checkers = {NumberOfEstimationsPerYearQuotaReachedChecker.class})
	@PreAuthorize("#estimationDTO?.business?.id == principal.business.id and " +
			  	  "T(com.novadart.novabill.domain.Client).findClient(#estimationDTO?.client?.id)?.business?.id == principal.business.id and " +
			  	  "#estimationDTO != null and #estimationDTO.id == null")
	public Long add(EstimationDTO estimationDTO) throws DataAccessException, FreeUserAccessForbiddenException, ValidationException {
		Estimation estimation = new Estimation();
		EstimationDTOTransformer.copyFromDTO(estimation, estimationDTO, true);
		validator.validate(Estimation.class, estimation);
		Client client = Client.findClient(estimationDTO.getClient().getId());
		estimation.setClient(client);
		client.getEstimations().add(estimation);
		Business business = Business.findBusiness(estimationDTO.getBusiness().getId());
		estimation.setBusiness(business);
		business.getEstimations().add(estimation);
		estimation.persist();
		estimation.flush();
		return estimation.getId();
	}

	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
			  	  "T(com.novadart.novabill.domain.Estimation).findEstimation(#id)?.business?.id == #businessID and " +
			  	  "T(com.novadart.novabill.domain.Estimation).findEstimation(#id)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NoSuchObjectException {
		Estimation estimation = Estimation.findEstimation(id);
		estimation.remove();
		if(Hibernate.isInitialized(estimation.getBusiness().getEstimations()))
			estimation.getBusiness().getEstimations().remove(estimation);
		if(Hibernate.isInitialized(estimation.getClient().getEstimations()))
			estimation.getClient().getEstimations().remove(estimation);
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#estimationDTO?.business?.id == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Client).findClient(#estimationDTO?.client?.id)?.business?.id == principal.business.id and " +
		  	  	  "#estimationDTO?.id != null")
	public void update(EstimationDTO estimationDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		Estimation persistedEstimation = Estimation.findEstimation(estimationDTO.getId());
		if(persistedEstimation == null)
			throw new NoSuchObjectException();
		EstimationDTOTransformer.copyFromDTO(persistedEstimation, estimationDTO, false);
		persistedEstimation.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: estimationDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOTransformer.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedEstimation);
			persistedEstimation.getAccountingDocumentItems().add(item);
		}
		validator.validate(Estimation.class, persistedEstimation);
	}

	public Long getNextEstimationId() throws NotAuthenticatedException {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextEstimationDocumentID();
	}

	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<EstimationDTO> getAllForClientInRange(Long clientID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		List<EstimationDTO> allEstimations = getAllForClient(clientID, year);
		return new PageDTO<EstimationDTO>(DTOUtils.range(allEstimations, start, length), start, length, new Long(allEstimations.size()));
	}

	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<EstimationDTO> getAllInRange(Long businessID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException {
		List<EstimationDTO> allEstimations = businessService.getEstimations(businessID, year);
		return new PageDTO<EstimationDTO>(DTOUtils.range(allEstimations, start, length), start, length, new Long(allEstimations.size()));
	}

}
