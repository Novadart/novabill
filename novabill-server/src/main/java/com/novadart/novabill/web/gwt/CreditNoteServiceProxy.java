package com.novadart.novabill.web.gwt;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.CreditNoteService;

public class CreditNoteServiceProxy extends AbstractGwtController<CreditNoteService, CreditNoteServiceProxy> implements CreditNoteService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("creditnoteServiceImpl")
	private CreditNoteService creditNoteService;

	public CreditNoteServiceProxy() {
		super(CreditNoteService.class);
	}

	public CreditNoteDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return creditNoteService.get(id);
	}

	public PageDTO<CreditNoteDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, ConcurrentAccessException {
		return creditNoteService.getAllInRange(businessID, start, length);
	}

	public List<CreditNoteDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return creditNoteService.getAllForClient(clientID);
	}

	public PageDTO<CreditNoteDTO> getAllForClientInRange(Long id, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return creditNoteService.getAllForClientInRange(id, start, length);
	}

	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException {
		return creditNoteService.add(creditNoteDTO);
	}

	public void remove(Long businessID, Long clientID, Long creditNoteID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		creditNoteService.remove(businessID, clientID, creditNoteID);
	}

	public void update(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException {
		creditNoteService.update(creditNoteDTO);
	}

	public Long getNextCreditNoteDocumentID() throws NotAuthenticatedException, ConcurrentAccessException {
		return creditNoteService.getNextCreditNoteDocumentID();
	}

}