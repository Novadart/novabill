package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("invoice.rpc")
public interface InvoiceService extends RemoteService {

	public InvoiceDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, ConcurrentAccessException;

	public List<InvoiceDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException;

	public void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException;

	public Long getNextInvoiceDocumentID() throws NotAuthenticatedException, ConcurrentAccessException;

	public void remove(Long businessID, Long clientID, Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, AuthorizationException;
	
	public List<InvoiceDTO> getAll(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
}
