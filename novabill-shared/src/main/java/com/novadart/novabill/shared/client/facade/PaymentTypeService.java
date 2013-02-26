package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("paymenttype.rpc")
public interface PaymentTypeService extends RemoteService {
	
	public List<PaymentTypeDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException; 
	
	public Long add(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException;
	
	public void update(PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException;
	
	public void remove(Long businessID, Long id)  throws NotAuthenticatedException, DataAccessException;

}
