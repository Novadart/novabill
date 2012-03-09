package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.InvalidInvoiceIDException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@RemoteServiceRelativePath("invoice.rpc")
public interface InvoiceService extends RemoteService {

	public InvoiceDTO get(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;

	public List<InvoiceDTO> getAllInRange(int start, int length) throws NotAuthenticatedException;

	public List<InvoiceDTO> getAllForClient(long clientId) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;

	public Long add(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, InvalidInvoiceIDException;

	public void update(InvoiceDTO invoiceDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, InvalidInvoiceIDException;

	public Long getNextInvoiceId() throws NotAuthenticatedException;

	public void remove(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;

	public List<InvoiceDTO> getAllForClientInRange(long clientId, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;

}
