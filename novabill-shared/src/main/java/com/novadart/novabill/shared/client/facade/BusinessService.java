package com.novadart.novabill.shared.client.facade;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@RemoteServiceRelativePath("business.rpc")
public interface BusinessService extends RemoteService {
	
	public long countClients() throws NotAuthenticatedException, ConcurrentAccessException;
	
	public long countInvoices() throws NotAuthenticatedException, ConcurrentAccessException;
	
	public long countInvoicesForYear(int year) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public BigDecimal getTotalAfterTaxesForYear(int year) throws NotAuthenticatedException, ConcurrentAccessException;
	
	public BusinessStatsDTO getStats() throws NotAuthenticatedException, ConcurrentAccessException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException;

}
