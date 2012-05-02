package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.InvalidInvoiceIDException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@RemoteServiceRelativePath("invoice.rpc")
public interface InvoiceService extends RemoteService {

	public InvoiceDTO get(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public List<InvoiceDTO> getAllInRange(int start, int length) throws NotAuthenticatedException, ConcurrentAccessException;

	public List<InvoiceDTO> getAllForClient(long clientId) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, InvalidInvoiceIDException, ConcurrentAccessException;

	public void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, InvalidInvoiceIDException, ConcurrentAccessException;

	public Long getNextInvoiceId() throws NotAuthenticatedException, ConcurrentAccessException;

	public void remove(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

	public List<InvoiceDTO> getAllForClientInRange(long clientId, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;
	
	public InvoiceDTO createFromEstimation(EstimationDTO estimationDTO) throws NotAuthenticatedException, DataAccessException, InvalidInvoiceIDException, NoSuchObjectException;
}
