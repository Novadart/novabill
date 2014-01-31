package com.novadart.novabill.service.web;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.domain.dto.factory.TransporterDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
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
	public List<TransporterDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getTransporters(businessID);
	}
	
	@PreAuthorize("T(com.novadart.novabill.domain.Transporter).findTransporter(#id)?.business?.id == principal.business.id")
	public TransporterDTO get(Long id) throws NotAuthenticatedException,NoSuchObjectException, DataAccessException {
		for(TransporterDTO transporterDTO: businessService.getTransporters(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()))
			if(transporterDTO.getId().equals(id))
				return transporterDTO;
		throw new NoSuchObjectException();
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#transporterDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#transporterDTO != null and #transporterDTO.id == null")
	public Long add(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		Transporter transporter = new Transporter();
		TransporterDTOFactory.copyFromDTO(transporter, transporterDTO);
		Business business = Business.findBusiness(transporterDTO.getBusiness().getId());
		transporter.setBusiness(business);
		validator.validate(transporter);
		business.getTransporters().add(transporter);
		transporter.persist();
		transporter.flush();
		return transporter.getId();
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#transporterDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#transporterDTO?.id != null")
	public void update(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		Transporter persistedTransporter = Transporter.findTransporter(transporterDTO.getId());
		if(persistedTransporter == null)
			throw new NoSuchObjectException();
		TransporterDTOFactory.copyFromDTO(persistedTransporter, transporterDTO);
		validator.validate(persistedTransporter);
	}
	
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Transporter).findTransporter(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		Transporter transporter = Transporter.findTransporter(id);
		transporter.remove(); //removing payment type
		if(Hibernate.isInitialized(transporter.getBusiness().getTransporters()))
			transporter.getBusiness().getTransporters().remove(transporter);
	}

}
