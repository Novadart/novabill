package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.PaymentTypeService;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PaymentTypeGwtService;

@HandleGWTServiceAccessDenied
public class PaymentTypeGwtController extends AbstractGwtController implements PaymentTypeGwtService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PaymentTypeService paymentTypeService;
	
	@Override
	public List<PaymentTypeDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return paymentTypeService.getAll(businessID);
	}

	@Override
	public PaymentTypeDTO get(Long id) throws NotAuthenticatedException,
			NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return paymentTypeService.get(id);
	}

	@Override
	public Long add(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException {
		return paymentTypeService.add(paymentTypeDTO);
	}
	
	@Override
	public void update(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		paymentTypeService.update(paymentTypeDTO);
	}

	@Override
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		paymentTypeService.remove(businessID, id);
	}

}
