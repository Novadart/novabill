package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("estimation.rpc")
public interface EstimationGwtService extends RemoteService {
	
	public EstimationDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public List<EstimationDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public Long add(EstimationDTO estimationDTO) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException, ValidationException;
	
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public void update(EstimationDTO estimationDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException;
	
	public Long getNextEstimationId() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public PageDTO<EstimationDTO> getAllForClientInRange(Long clientID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException;
	
	public PageDTO<EstimationDTO> getAllInRange(Long businessID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
}
