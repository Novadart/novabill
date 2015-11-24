package com.novadart.novabill.service.web;

import java.math.BigDecimal;
import java.util.List;

import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.tuple.Pair;

public interface BusinessService {

	public Integer countClients(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Integer countInvoicesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Pair<BigDecimal, BigDecimal> getTotalsForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public void update(BusinessDTO businessDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<InvoiceDTO> getInvoices(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;

	public List<InvoiceDTO> getInvoices(Long businessID, Integer year, String docIDSuffix) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<CreditNoteDTO> getCreditNotes(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<EstimationDTO> getEstimations(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<ClientDTO> getClients(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<CommodityDTO> getCommodities(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<PriceListDTO> getPriceLists(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<PaymentTypeDTO> getPaymentTypes(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<TransporterDTO> getTransporters(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Long add(BusinessDTO businessDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, com.novadart.novabill.shared.client.exception.CloneNotSupportedException;
	
	public List<Integer> getInvoceYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<Integer> getCreditNoteYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<Integer> getEstimationYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<Integer> getTransportDocumentYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<LogRecordDTO> getLogRecords(Long businessID, Integer numberOfDays) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<BigDecimal> getInvoiceMonthTotals(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<SharingPermitDTO> getSharingPermits(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public void setDefaultLayout(Long businessID, LayoutType layoutType) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public List<NotificationDTO> getNotifications(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public void markNotificationAsSeen(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;

	public List<DocumentIDClassDTO> getDocumentIdClasses(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
}
