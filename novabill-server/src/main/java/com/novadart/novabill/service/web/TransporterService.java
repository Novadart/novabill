package com.novadart.novabill.service.web;

import java.util.List;

import com.novadart.novabill.authorization.TrialOrPremiumChecker;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.domain.dto.transformer.TransporterDTOTransformer;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;


@Service
public class TransporterService {
	
	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UtilsService utilsService;
	
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public List<TransporterDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getTransporters(businessID);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Transporter).findTransporter(#id)?.business?.id == principal.business.id")
	public TransporterDTO get(Long id) throws NotAuthenticatedException,NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		for(TransporterDTO transporterDTO: businessService.getTransporters(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()))
			if(transporterDTO.getId().equals(id))
				return transporterDTO;
		throw new NoSuchObjectException();
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#transporterDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#transporterDTO != null and #transporterDTO.id == null")
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public Long add(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException {
		Transporter transporter = new Transporter();
		TransporterDTOTransformer.copyFromDTO(transporter, transporterDTO);
		Business business = Business.findBusiness(transporterDTO.getBusiness().getId());
		transporter.setBusiness(business);
		validator.validate(transporter);
		business.getTransporters().add(transporter);
		transporter.persist();
		transporter.flush();
		return transporter.getId();
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#transporterDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#transporterDTO?.id != null")
	public void update(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		Transporter persistedTransporter = Transporter.findTransporter(transporterDTO.getId());
		if(persistedTransporter == null)
			throw new NoSuchObjectException();
		TransporterDTOTransformer.copyFromDTO(persistedTransporter, transporterDTO);
		validator.validate(persistedTransporter);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Transporter).findTransporter(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Transporter transporter = Transporter.findTransporter(id);
		transporter.remove(); //removing payment type
		if(Hibernate.isInitialized(transporter.getBusiness().getTransporters()))
			transporter.getBusiness().getTransporters().remove(transporter);
	}

}
