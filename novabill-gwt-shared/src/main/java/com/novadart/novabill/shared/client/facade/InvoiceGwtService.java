package com.novadart.novabill.shared.client.facade;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("invoice.rpc")
public interface InvoiceGwtService extends RemoteService {

	public InvoiceDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;

	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;

	public List<InvoiceDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;

	public Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, DataIntegrityException;

	public void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException;

	public Long getNextInvoiceDocumentID() throws NotAuthenticatedException, DataAccessException;

	public void remove(Long businessID, Long clientID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, DataIntegrityException;

	public PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;
	
	public void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, NoSuchObjectException, AuthorizationException, DataAccessException;
	
	public List<InvoiceDTO> getAllUnpaidInDateRange(Date startDate, Date endDate) throws NotAuthenticatedException, DataAccessException;
	
}
