package com.novadart.novabill.shared.client.facade;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("business.rpc")
public interface BusinessService extends RemoteService {
	
	public long countClients(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public long countInvoices(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public long countInvoicesForYear(Long BusinessID, Integer year) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public BigDecimal getTotalAfterTaxesForYear(Long businessID, Integer year) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, ValidationException;
	
	public String generatePDFToken() throws NotAuthenticatedException, ConcurrentAccessException, NoSuchAlgorithmException;
	
	public String generateExportToken() throws NotAuthenticatedException, ConcurrentAccessException, NoSuchAlgorithmException;

}
