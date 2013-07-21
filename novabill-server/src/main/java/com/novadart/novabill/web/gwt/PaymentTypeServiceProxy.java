package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
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
	public List<PaymentTypeDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return paymentTypeService.getAll(businessID);
	}

	@Override
	public PaymentTypeDTO get(Long id) throws NotAuthenticatedException,
			NoSuchObjectException, DataAccessException {
		return paymentTypeService.get(id);
	}

	@Override
	public Long add(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		return paymentTypeService.add(paymentTypeDTO);
	}
	
	@Override
	public void update(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		paymentTypeService.update(paymentTypeDTO);
	}

	@Override
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		paymentTypeService.remove(businessID, id);
	}

}
