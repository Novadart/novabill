package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.TransportDocumentService;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransportDocumentGwtService;

@HandleGWTServiceAccessDenied
public class TransportDocumentGwtController extends AbstractGwtController implements TransportDocumentGwtService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private TransportDocumentService transportDocService;
	
	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return transportDocService.get(id);
	}

	public List<TransportDocumentDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return transportDocService.getAllForClient(clientID, year);
	}

	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, AuthorizationException, ValidationException {
		return transportDocService.add(transportDocDTO);
	}

	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException {
		transportDocService.remove(businessID, clientID, id);
	}

	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException {
		transportDocService.update(transportDocDTO);
	}

	public Long getNextTransportDocId() throws NotAuthenticatedException, DataAccessException {
		return transportDocService.getNextTransportDocId();
	}

	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return transportDocService.getAllForClientInRange(clientID, year, start, length);
	}

	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException {
		return transportDocService.getAllInRange(businessID, year, start, length);
	}

}
