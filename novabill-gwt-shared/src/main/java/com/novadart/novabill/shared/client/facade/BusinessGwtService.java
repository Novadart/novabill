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
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.tuple.Pair;

@XsrfProtect
@RemoteServiceRelativePath("business.rpc")
public interface BusinessGwtService extends RemoteService {
	
	public Integer countClients(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Integer countInvoicesForYear(Long BusinessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Pair<BigDecimal, BigDecimal> getTotalsForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException;
	
	public String generatePDFToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException;
	
	public String generateExportToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException;
	
	public List<InvoiceDTO> getInvoices(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<CreditNoteDTO> getCreditNotes(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<EstimationDTO> getEstimations(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<ClientDTO> getClients(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<CommodityDTO> getCommodities(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<PaymentTypeDTO> getPaymentTypes(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<TransporterDTO> getTransporters(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public String generateLogoOpToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException;
	
	public Long add(BusinessDTO businessDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, com.novadart.novabill.shared.client.exception.CloneNotSupportedException;
	
}
