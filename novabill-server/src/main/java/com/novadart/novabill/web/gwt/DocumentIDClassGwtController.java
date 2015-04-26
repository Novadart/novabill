package com.novadart.novabill.web.gwt;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.DocumentIDClassService;
import com.novadart.novabill.service.web.TransporterService;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.DocumentIDClassGwtService;
import com.novadart.novabill.shared.client.facade.TransporterGwtService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@HandleGWTServiceAccessDenied
public class DocumentIDClassGwtController extends AbstractGwtController implements DocumentIDClassGwtService {
	
	private static final long serialVersionUID = 2599518871485203967L;
	
	@Autowired
	private DocumentIDClassService documentIDClassService;


	@Override
	public List<DocumentIDClassDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return documentIDClassService.getAll(businessID);
	}
}
