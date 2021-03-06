package com.novadart.novabill.web.gwt;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.InvoiceGwtService;

@HandleGWTServiceAccessDenied
public class InvoiceGwtController extends AbstractGwtController implements InvoiceGwtService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private InvoiceService invoiceService;
	
	public InvoiceDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.get(id);
	}

	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllInRange(businessID, year, start, length);
	}

	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, String suffix, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllInRange(businessID, year, suffix, start, length);
	}

	public List<InvoiceDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllForClient(clientID, year);
	}

	@Override
	public List<InvoiceDTO> getAllForClient(Long clientID, Integer year, String suffix) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllForClient(clientID, year, suffix);
	}

	public Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, DataIntegrityException {
		return invoiceService.add(invoiceDTO);
	}

	public void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, DataIntegrityException, FreeUserAccessForbiddenException {
		invoiceService.update(invoiceDTO);
	}

	public Long getNextInvoiceDocumentID(String suffix) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getNextInvoiceDocumentID(suffix);
	}

	public void remove(Long businessID, Long clientID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, DataIntegrityException, FreeUserAccessForbiddenException {
		invoiceService.remove(businessID, clientID, id);
	}

	public PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllForClientInRange(clientID, year, start, length);
	}

	public void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException, DataAccessException {
		invoiceService.setPayed(businessID, clientID, id, value);
	}

	@Override
	public List<InvoiceDTO> getAllUnpaidInDateRange(FilteringDateType filteringDateType, Date startDate, Date endDate) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllUnpaidInDateRange(filteringDateType, startDate, endDate);
	}
	
}
