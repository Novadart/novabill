package com.novadart.novabill.web.gwt;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;

@HandleGWTServiceAccessDenied
public class TransportDocumentServiceProxy extends AbstractGwtController implements TransportDocumentService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("transportdocImpl")
	private TransportDocumentService transportDocService;
	
	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return transportDocService.get(id);
	}

	public List<TransportDocumentDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return transportDocService.getAllForClient(clientID);
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

	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException {
		return transportDocService.getAllForClientInRange(clientID, start,
				length);
	}

	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException {
		return transportDocService.getAllInRange(businessID, start, length);
	}

}
