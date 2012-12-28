package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;

public class TransportDocumentServiceProxy extends AbstractGwtController<TransportDocumentService, TransportDocumentServiceProxy> implements TransportDocumentService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("transportdocImpl")
	private TransportDocumentService transportDocService;
	
	public TransportDocumentServiceProxy() {
		super(TransportDocumentService.class);
	}

	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return transportDocService.get(id);
	}

	public List<TransportDocumentDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return transportDocService.getAllForClient(clientID);
	}

	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, ConcurrentAccessException, AuthorizationException, ValidationException {
		return transportDocService.add(transportDocDTO);
	}

	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException {
		transportDocService.remove(businessID, clientID, id);
	}

	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException, ValidationException {
		transportDocService.update(transportDocDTO);
	}

	public Long getNextTransportDocId() throws NotAuthenticatedException, ConcurrentAccessException {
		return transportDocService.getNextTransportDocId();
	}

	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return transportDocService.getAllForClientInRange(clientID, start,
				length);
	}

	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, ConcurrentAccessException {
		return transportDocService.getAllInRange(businessID, start, length);
	}

}