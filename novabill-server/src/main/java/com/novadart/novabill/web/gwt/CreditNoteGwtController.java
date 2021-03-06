package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.CreditNoteService;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.CreditNoteGwtService;

@HandleGWTServiceAccessDenied
public class CreditNoteGwtController extends AbstractGwtController implements CreditNoteGwtService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CreditNoteService creditNoteService;

	public CreditNoteDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return creditNoteService.get(id);
	}

	public PageDTO<CreditNoteDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return creditNoteService.getAllInRange(businessID, year, start, length);
	}

	public List<CreditNoteDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return creditNoteService.getAllForClient(clientID, year);
	}

	public PageDTO<CreditNoteDTO> getAllForClientInRange(Long id, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return creditNoteService.getAllForClientInRange(id, year, start, length);
	}

	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException {
		return creditNoteService.add(creditNoteDTO);
	}

	public void remove(Long businessID, Long clientID, Long creditNoteID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		creditNoteService.remove(businessID, clientID, creditNoteID);
	}

	public void update(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		creditNoteService.update(creditNoteDTO);
	}

	public Long getNextCreditNoteDocumentID() throws NotAuthenticatedException, DataAccessException {
		return creditNoteService.getNextCreditNoteDocumentID();
	}

}
