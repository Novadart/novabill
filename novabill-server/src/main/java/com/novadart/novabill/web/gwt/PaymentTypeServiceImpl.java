package com.novadart.novabill.web.gwt;

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
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PaymentTypeService;

public class PaymentTypeServiceImpl implements PaymentTypeService {
	
	@Autowired
	private SimpleValidator validator;

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

}
