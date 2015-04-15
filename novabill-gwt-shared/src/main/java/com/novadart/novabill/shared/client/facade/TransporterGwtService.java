package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("transporter.rpc")
public interface TransporterGwtService extends RemoteService {

	List<TransporterDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	TransporterDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;
	
	Long add(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException;
	
	void update(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException;
	
	void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException;
	
}
