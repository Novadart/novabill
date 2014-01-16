package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.EstimationService;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.EstimationGwtService;

@HandleGWTServiceAccessDenied
public class EstimationGwtController extends AbstractGwtController implements EstimationGwtService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private EstimationService estimationService;
	
	public EstimationDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return estimationService.get(id);
	}

	public List<EstimationDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return estimationService.getAllForClient(clientID, year);
	}

	public Long add(EstimationDTO estimationDTO) throws NotAuthenticatedException, DataAccessException, AuthorizationException, ValidationException {
		return estimationService.add(estimationDTO);
	}

	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException {
		estimationService.remove(businessID, clientID, id);
	}

	public void update(EstimationDTO estimationDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException {
		estimationService.update(estimationDTO);
	}

	public Long getNextEstimationId() throws NotAuthenticatedException, DataAccessException {
		return estimationService.getNextEstimationId();
	}

	public PageDTO<EstimationDTO> getAllForClientInRange(Long clientID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return estimationService.getAllForClientInRange(clientID, year, start, length);
	}

	public PageDTO<EstimationDTO> getAllInRange(Long businessID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException {
		return estimationService.getAllInRange(businessID, year, start, length);
	}

}
