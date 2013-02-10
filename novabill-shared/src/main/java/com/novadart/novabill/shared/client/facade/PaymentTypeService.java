package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.RemoteService;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

public interface PaymentTypeService extends RemoteService {
	
	public Long add(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException;

}
