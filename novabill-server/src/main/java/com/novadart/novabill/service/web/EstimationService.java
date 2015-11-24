package com.novadart.novabill.service.web;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.TrialOrPremiumChecker;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.service.PDFStorageService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class EstimationService {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private AccountingDocumentValidator validator;

	@Autowired
	private PDFStorageService pdfStorageService;

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Estimation).findEstimation(#id)?.business?.id == principal.business.id")
	public EstimationDTO get(Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException {
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

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<EstimationDTO> getAllForClient(Long clientID, Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException {
		return new ArrayList<EstimationDTO>(DTOUtils.filter(businessService.getEstimations(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), year), new EqualsClientIDPredicate(clientID)));
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#estimationDTO?.business?.id == principal.business.id and " +
			  	  "T(com.novadart.novabill.domain.Client).findClient(#estimationDTO?.client?.id)?.business?.id == principal.business.id and " +
			  	  "#estimationDTO != null and #estimationDTO.id == null")
	public Long add(EstimationDTO estimationDTO) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException {
		Estimation estimation = new Estimation();
		EstimationDTOTransformer.copyFromDTO(estimation, estimationDTO, true);
		validator.validate(Estimation.class, estimation);
		Client client = Client.findClient(estimationDTO.getClient().getId());
		estimation.setClient(client);
		client.getEstimations().add(estimation);
		Business business = Business.findBusiness(estimationDTO.getBusiness().getId());
		estimation.setBusiness(business);
		business.getEstimations().add(estimation);
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(estimation, DocumentType.ESTIMATION);
		estimation.setDocumentPath(docPath);
		estimation.persist();
		estimation.flush();
		return estimation.getId();
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
			  	  "T(com.novadart.novabill.domain.Estimation).findEstimation(#id)?.business?.id == #businessID and " +
			  	  "T(com.novadart.novabill.domain.Estimation).findEstimation(#id)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException {
		Estimation estimation = Estimation.findEstimation(id);
		estimation.remove();
		if(Hibernate.isInitialized(estimation.getBusiness().getEstimations()))
			estimation.getBusiness().getEstimations().remove(estimation);
		if(Hibernate.isInitialized(estimation.getClient().getEstimations()))
			estimation.getClient().getEstimations().remove(estimation);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#estimationDTO?.business?.id == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Client).findClient(#estimationDTO?.client?.id)?.business?.id == principal.business.id and " +
		  	  	  "#estimationDTO?.id != null")
	public void update(EstimationDTO estimationDTO) throws DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException {
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
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(persistedEstimation, DocumentType.ESTIMATION);
		persistedEstimation.setDocumentPath(docPath);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public Long getNextEstimationId() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextEstimationDocumentID();
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<EstimationDTO> getAllForClientInRange(Long clientID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		List<EstimationDTO> allEstimations = getAllForClient(clientID, year);
		return new PageDTO<>(DTOUtils.range(allEstimations, start, length), start, length, new Long(allEstimations.size()));
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<EstimationDTO> getAllInRange(Long businessID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		List<EstimationDTO> allEstimations = businessService.getEstimations(businessID, year);
		return new PageDTO<>(DTOUtils.range(allEstimations, start, length), start, length, new Long(allEstimations.size()));
	}

}
