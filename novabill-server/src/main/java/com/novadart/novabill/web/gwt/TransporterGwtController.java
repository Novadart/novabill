package com.novadart.novabill.web.gwt;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.TransporterService;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.TransporterGwtService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@HandleGWTServiceAccessDenied
public class TransporterGwtController extends AbstractGwtController implements TransporterGwtService {
	
	private static final long serialVersionUID = 2599518871485203967L;
	
	@Autowired
	private TransporterService transporterService;

	@Override
	public Long add(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException {
		return transporterService.add(transporterDTO);
	}

	@Override
	public TransporterDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		return transporterService.get(id);
	}
	
	@Override
	public void update(TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		transporterService.update(transporterDTO);
	}

	@Override
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		transporterService.remove(businessID, id);
	}

	@Override
	public List<TransporterDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return transporterService.getAll(businessID);
	}

}
