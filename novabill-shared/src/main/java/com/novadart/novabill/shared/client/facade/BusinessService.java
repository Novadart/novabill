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
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("business.rpc")
public interface BusinessService extends RemoteService {
	
	public Long countClients(Long businessID) throws NotAuthenticatedException;
	
	public Long countInvoices(Long businessID) throws NotAuthenticatedException;
	
	public Long countInvoicesForYear(Long BusinessID, Integer year) throws NotAuthenticatedException;
	
	public BigDecimal getTotalAfterTaxesForYear(Long businessID, Integer year) throws NotAuthenticatedException;
	
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException;
	
	public String generatePDFToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException;
	
	public String generateExportToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException;
	
	public List<InvoiceDTO> getInvoices(Long businessID) throws NotAuthenticatedException;
	
	public List<CreditNoteDTO> getCreditNotes(Long businessID) throws NotAuthenticatedException;
	
	public List<EstimationDTO> getEstimations(Long businessID) throws NotAuthenticatedException;
	
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID) throws NotAuthenticatedException;
	
	public List<ClientDTO> getClients(Long businessID) throws NotAuthenticatedException;

}
