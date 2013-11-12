package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;


@XsrfProtect
@RemoteServiceRelativePath("creditnote.rpc")
public interface CreditNoteGwtService extends RemoteService {
	
	public CreditNoteDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public PageDTO<CreditNoteDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;
	
	public List<CreditNoteDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public PageDTO<CreditNoteDTO> getAllForClientInRange(Long id, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException;
	
	public void remove(Long businessID, Long clientID, Long creditNoteID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public void update(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException;

	public Long getNextCreditNoteDocumentID() throws NotAuthenticatedException, DataAccessException;
	
}
