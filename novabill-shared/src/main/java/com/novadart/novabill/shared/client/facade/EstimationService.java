package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("estimation.rpc")
public interface EstimationService extends RemoteService {
	
	public EstimationDTO get(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public List<EstimationDTO> getAllForClient(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public Long add(EstimationDTO estimationDTO) throws NotAuthenticatedException, DataAccessException, ConcurrentAccessException, AuthorizationException, ValidationException;
	
	public void remove(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
	public void update(EstimationDTO estimationDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException, ValidationException;
	
	public Long getNextEstimationId() throws NotAuthenticatedException, ConcurrentAccessException;
	
	public PageDTO<EstimationDTO> getAllForClientInRange(long id, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public PageDTO<EstimationDTO> getAllInRange(int start, int length) throws NotAuthenticatedException, ConcurrentAccessException;
	
}
