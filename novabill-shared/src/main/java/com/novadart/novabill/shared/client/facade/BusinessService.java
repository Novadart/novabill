package com.novadart.novabill.shared.client.facade;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("business.rpc")
public interface BusinessService extends RemoteService {
	
	public Long countClients(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public Long countInvoices(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public Long countInvoicesForYear(Long BusinessID, Integer year) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public BigDecimal getTotalAfterTaxesForYear(Long businessID, Integer year) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, ValidationException;
	
	public String generatePDFToken() throws NotAuthenticatedException, ConcurrentAccessException, NoSuchAlgorithmException, UnsupportedEncodingException;
	
	public String generateExportToken() throws NotAuthenticatedException, ConcurrentAccessException, NoSuchAlgorithmException, UnsupportedEncodingException;
	
	public List<InvoiceDTO> getInvoices(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public List<CreditNoteDTO> getCreditNotes(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public List<EstimationDTO> getEstimations(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;

}
