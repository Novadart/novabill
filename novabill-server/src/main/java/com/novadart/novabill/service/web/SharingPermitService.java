package com.novadart.novabill.service.web;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.domain.dto.transformer.SharingPermitDTOTransformer;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;


@Service
public class SharingPermitService {

	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private BusinessService businessService;
	
	@PreAuthorize("#businessID == principal.business.id")
	public List<SharingPermitDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException{
		return businessService.getSharingPermits(businessID);
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#sharingPermitDTO?.business?.id == principal.business.id and " +
	  	  	      "#sharingPermitDTO != null and #sharingPermitDTO.id == null")
	public Long add(Long businessID, SharingPermitDTO sharingPermitDTO) throws ValidationException {
		SharingPermit sharingPermit = new SharingPermit();
		SharingPermitDTOTransformer.copyFromDTO(sharingPermit, sharingPermitDTO);
		Business business = Business.findBusiness(sharingPermitDTO.getBusiness().getId());
		sharingPermit.setBusiness(business);
		validator.validate(sharingPermit);
		business.getSharingPermits().add(sharingPermit);
		sharingPermit.setBusiness(business);
		sharingPermit.persist();
		sharingPermit.flush();
		return sharingPermit.getId();
	}
	
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.SharingPermit).findSharingPermit(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) {
		SharingPermit sharingPermit = SharingPermit.findSharingPermit(id);
		sharingPermit.remove();
		if(Hibernate.isInitialized(sharingPermit.getBusiness().getSharingPermits()))
			sharingPermit.getBusiness().getSharingPermits().remove(sharingPermit);
	}
	
}
