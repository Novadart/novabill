package com.novadart.novabill.web.gwt;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.DocumentIDClassService;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.facade.DocumentIDClassGwtService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@HandleGWTServiceAccessDenied
public class DocumentIDClassGwtController extends AbstractGwtController implements DocumentIDClassGwtService {
	
	private static final long serialVersionUID = 2599518871485203967L;
	
	@Autowired
	private DocumentIDClassService documentIDClassService;


	@Override
	public List<DocumentIDClassDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return documentIDClassService.getAll(businessID);
	}
}
