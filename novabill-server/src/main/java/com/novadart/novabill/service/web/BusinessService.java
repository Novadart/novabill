package com.novadart.novabill.service.web;

import java.math.BigDecimal;
import java.util.List;

import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

public interface BusinessService {

	public Long countClients(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public Long countInvoicesForYear(Long BusinessID, Integer year) throws NotAuthenticatedException, DataAccessException;
	
	public BigDecimal getTotalAfterTaxesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException;
	
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException;
	
	public List<InvoiceDTO> getInvoices(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException;
	
	public List<CreditNoteDTO> getCreditNotes(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException;
	
	public List<EstimationDTO> getEstimations(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException;
	
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException;
	
	public List<ClientDTO> getClients(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public List<CommodityDTO> getCommodities(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public List<PriceListDTO> getPriceLists(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public List<PaymentTypeDTO> getPaymentTypes(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public Long updateNotesBitMask(Long notesBitMask) throws NotAuthenticatedException, DataAccessException;
	
	public Long add(BusinessDTO businessDTO) throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, com.novadart.novabill.shared.client.exception.CloneNotSupportedException;
	
	public List<Integer> getInvoceYears(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public List<Integer> getCreditNoteYears(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public List<Integer> getEstimationYears(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public List<Integer> getTransportDocumentYears(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
}
