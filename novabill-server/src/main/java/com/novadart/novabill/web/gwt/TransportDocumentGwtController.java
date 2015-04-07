package com.novadart.novabill.web.gwt;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.TransportDocumentService;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.TransportDocumentGwtService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException, ValidationException {
		return transportDocService.add(transportDocDTO);
	}

	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException {
		transportDocService.remove(businessID, clientID, id);
	}

	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException, DataIntegrityException {
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

	@Override
	public List<TransportDocumentDTO> getAllWithIDs(List<Long> ids) throws DataAccessException, NoSuchObjectException {
		return transportDocService.getAllWithIDs(ids);
	}

	@Override
	public void setInvoice(Long businessID, Long invoiceID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException {
		transportDocService.setInvoice(businessID, invoiceID, transportDocID);
	}

	@Override
	public void clearInvoice(Long businessID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException {
		transportDocService.clearInvoice(businessID, transportDocID);
	}

}
