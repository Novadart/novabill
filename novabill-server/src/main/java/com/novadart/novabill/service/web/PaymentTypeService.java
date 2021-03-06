package com.novadart.novabill.service.web;

import java.util.List;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.TrialOrPremiumChecker;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.dto.transformer.PaymentTypeDTOTransformer;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class PaymentTypeService {
	
	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private BusinessService businessService;

	@Autowired
	private UtilsService utilsService;

	@Restrictions(checkers = TrialOrPremiumChecker.class)
	@PreAuthorize("#businessID == principal.business.id")
	public List<PaymentTypeDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getPaymentTypes(businessID);
	}

	@Restrictions(checkers = TrialOrPremiumChecker.class)
	@PreAuthorize("T(com.novadart.novabill.domain.PaymentType).findPaymentType(#id)?.business?.id == principal.business.id")
	public PaymentTypeDTO get(Long id) throws NotAuthenticatedException,NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		for(PaymentTypeDTO paymentTypeDTO: businessService.getPaymentTypes(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()))
			if(paymentTypeDTO.getId().equals(id))
				return paymentTypeDTO;
		throw new NoSuchObjectException();
	}

	@Restrictions(checkers = TrialOrPremiumChecker.class)
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#paymentTypeDTO?.business?.id == principal.business.id and " +
		  	  	  "#paymentTypeDTO != null and #paymentTypeDTO.id == null")
	public Long add(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException {
		PaymentType paymentType = new PaymentType();
		PaymentTypeDTOTransformer.copyFromDTO(paymentType, paymentTypeDTO);
		validator.validate(paymentType);
		Business business = Business.findBusiness(paymentTypeDTO.getBusiness().getId());
		business.getPaymentTypes().add(paymentType);
		paymentType.setBusiness(business);
		paymentType.flush();
		return paymentType.getId();
	}



	@Restrictions(checkers = TrialOrPremiumChecker.class)
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#paymentTypeDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#paymentTypeDTO?.id != null")
	public void update(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		PaymentType persistedPaymentType = PaymentType.findPaymentType(paymentTypeDTO.getId());
		if(persistedPaymentType == null)
			throw new NoSuchObjectException();
		PaymentTypeDTOTransformer.copyFromDTO(persistedPaymentType, paymentTypeDTO);
		validator.validate(persistedPaymentType);
	}



	@Restrictions(checkers = TrialOrPremiumChecker.class)
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.PaymentType).findPaymentType(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		PaymentType paymentType = PaymentType.findPaymentType(id);
		for(Client client: paymentType.getClients())
			client.setDefaultPaymentType(null);
		paymentType.remove(); //removing payment type
		if(Hibernate.isInitialized(paymentType.getBusiness().getPaymentTypes()))
			paymentType.getBusiness().getPaymentTypes().remove(paymentType);
	}

}
