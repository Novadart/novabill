package com.novadart.novabill.shared.client.facade;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("invoice.rpc")
public interface InvoiceGwtService extends RemoteService {

	InvoiceDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;

	PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;

	PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, String suffix, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;

	List<InvoiceDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;

	List<InvoiceDTO> getAllForClient(Long clientID, Integer year, String suffix) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;

	Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, DataIntegrityException;

	void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, DataIntegrityException;

	Long getNextInvoiceDocumentID(String suffix) throws NotAuthenticatedException, DataAccessException;

	void remove(Long businessID, Long clientID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, DataIntegrityException;

	PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException;
	
	void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException, DataAccessException;
	
	List<InvoiceDTO> getAllUnpaidInDateRange(FilteringDateType filteringDateType, Date startDate, Date endDate) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
}
