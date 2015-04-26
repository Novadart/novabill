package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.*;

import java.util.List;

@XsrfProtect
@RemoteServiceRelativePath("documentidclass.rpc")
public interface DocumentIDClassGwtService extends RemoteService {

	List<DocumentIDClassDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
}
