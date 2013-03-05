package com.novadart.novabill.web.gwt;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.dto.factory.PaymentTypeDTOFactory;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.PaymentTypeService;

public class PaymentTypeServiceImpl implements PaymentTypeService {
	
	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private BusinessService businessService;

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<PaymentTypeDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getPaymentTypes(businessID);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#paymentTypeDTO?.business?.id == principal.business.id and " +
		  	  	  "#paymentTypeDTO != null and #paymentTypeDTO.id == null")
	public Long add(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		PaymentType paymentType = new PaymentType();
		PaymentTypeDTOFactory.copyFromDTO(paymentType, paymentTypeDTO);
		validator.validate(paymentType);
		Business business = Business.findBusiness(paymentTypeDTO.getBusiness().getId());
		business.getPaymentTypes().add(paymentType);
		paymentType.setBusiness(business);
		paymentType.flush();
		return paymentType.getId();
	}
	
	

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#paymentTypeDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#paymentTypeDTO?.id != null")
	public void update(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		PaymentType persistedPaymentType = PaymentType.findPaymentType(paymentTypeDTO.getId());
		if(persistedPaymentType == null)
			throw new NoSuchObjectException();
		PaymentTypeDTOFactory.copyFromDTO(persistedPaymentType, paymentTypeDTO);
		validator.validate(persistedPaymentType);
	}



	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.PaymentType).findPaymentType(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		PaymentType paymentType = PaymentType.findPaymentType(id);
		paymentType.remove(); //removing payment type
		if(Hibernate.isInitialized(paymentType.getBusiness().getPaymentTypes()))
			paymentType.getBusiness().getPaymentTypes().remove(paymentType);
	}

}
