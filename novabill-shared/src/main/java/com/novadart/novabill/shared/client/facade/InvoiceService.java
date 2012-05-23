package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.InvalidDocumentIDException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.QuotaException;

@RemoteServiceRelativePath("invoice.rpc")
public interface InvoiceService extends RemoteService {

	public InvoiceDTO get(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public List<InvoiceDTO> getAllInRange(int start, int length) throws NotAuthenticatedException, ConcurrentAccessException;

	public List<InvoiceDTO> getAllForClient(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, InvalidDocumentIDException, ConcurrentAccessException, QuotaException;

	public void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, InvalidDocumentIDException, ConcurrentAccessException;

	public Long getNextInvoiceDocumentID() throws NotAuthenticatedException, ConcurrentAccessException;

	public void remove(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public List<InvoiceDTO> getAllForClientInRange(long id, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public InvoiceDTO createFromEstimation(EstimationDTO estimationDTO) throws NotAuthenticatedException, DataAccessException, InvalidDocumentIDException, NoSuchObjectException, ConcurrentAccessException, QuotaException;
	
	public List<Long> validateInvoiceDocumentID(Long documentID) throws NotAuthenticatedException, ConcurrentAccessException;
}
