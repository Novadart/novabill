package com.novadart.novabill.web.gwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PaymentTypeService;

@HandleGWTServiceAccessDenied
public class PaymentTypeServiceProxy extends AbstractGwtController implements PaymentTypeService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("paymentTypeServiceImpl")
	private PaymentTypeService paymentTypeService;

	@Override
	public Long add(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		return paymentTypeService.add(paymentTypeDTO);
	}

}
