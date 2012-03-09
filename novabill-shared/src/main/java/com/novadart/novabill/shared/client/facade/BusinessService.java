package com.novadart.novabill.shared.client.facade;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@RemoteServiceRelativePath("business.rpc")
public interface BusinessService extends RemoteService {
	
	public long countClients() throws NotAuthenticatedException;
	
	public long countInvoices() throws NotAuthenticatedException;
	
	public long countInvoicesForYear(int year) throws NotAuthenticatedException;
	
	public BigDecimal getTotalAfterTaxesForYear(int year) throws NotAuthenticatedException;
	
	public BusinessStatsDTO getStats() throws NotAuthenticatedException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;

}
