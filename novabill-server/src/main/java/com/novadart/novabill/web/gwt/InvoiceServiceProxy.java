package com.novadart.novabill.web.gwt;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.InvoiceService;

@HandleGWTServiceAccessDenied
public class InvoiceServiceProxy extends AbstractGwtController implements InvoiceService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("invoiceServiceImpl")
	private InvoiceService invoiceService;
	
	public InvoiceDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		return invoiceService.get(id);
	}

	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException {
		return invoiceService.getAllInRange(businessID, start, length);
	}

	public List<InvoiceDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		return invoiceService.getAllForClient(clientID);
	}

	public Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		return invoiceService.add(invoiceDTO);
	}

	public void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException {
		invoiceService.update(invoiceDTO);
	}

	public Long getNextInvoiceDocumentID() throws NotAuthenticatedException, DataAccessException {
		return invoiceService.getNextInvoiceDocumentID();
	}

	public void remove(Long businessID, Long clientID, Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		invoiceService.remove(businessID, clientID, id);
	}

	public PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer start, Integer length) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		return invoiceService.getAllForClientInRange(clientID, start, length);
	}

	public void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, NoSuchObjectException, AuthorizationException, DataAccessException {
		invoiceService.setPayed(businessID, clientID, id, value);
	}
	
}
