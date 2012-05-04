package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@RemoteServiceRelativePath("estimation.rpc")
public interface EstimationService extends RemoteService {
	
	public EstimationDTO get(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public List<EstimationDTO> getAllForClient(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public Long add(EstimationDTO estimationDTO) throws NotAuthenticatedException, DataAccessException, ConcurrentAccessException;
	
	public void remove(Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
	public void update(EstimationDTO estimationDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException;
	
	public Long getNextEstimationId() throws NotAuthenticatedException, ConcurrentAccessException;
	
}
