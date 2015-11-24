package com.novadart.novabill.web.mvc.ajax;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.CloneNotSupportedException;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses")
public class BusinessController {
	
	@Autowired
	private BusinessService businessService;
	
	@RequestMapping(value = "/{businessID}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BusinessDTO get(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.get(businessID);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> add(@RequestBody BusinessDTO businessDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, CloneNotSupportedException {
		return ImmutableMap.of(JsonConst.VALUE, businessService.add(businessDTO).toString());
	}
	
	@RequestMapping(value = "/{businessID}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody BusinessDTO businessDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		businessService.update(businessDTO);
	}

	@RequestMapping(value = "/{businessID}/defaulttemplate/{layoutType}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void setDefaultLayout(@PathVariable Long businessID, @PathVariable LayoutType layoutType) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		businessService.setDefaultLayout(businessID, layoutType);
	}
	
	@RequestMapping(value = "/{businessID}/creditnotes/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<CreditNoteDTO> getCreditNotes(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getCreditNotes(businessID, year);
	}
	
	@RequestMapping(value = "/{businessID}/estimations/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<EstimationDTO> getEstimations(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getEstimations(businessID, year);
	}
	
	@RequestMapping(value = "/{businessID}/transdocs/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<TransportDocumentDTO> getTransportDocuments(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getTransportDocuments(businessID, year);
	}
	
	@RequestMapping(value = "/{businessID}/clients", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<ClientDTO> getClients(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getClients(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/clients/count", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> countClients(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return ImmutableMap.of(JsonConst.VALUE, businessService.countClients(businessID));
	}
	
	@RequestMapping(value = "/{businessID}/invoices/{year}/count", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> countInvoicesForYear(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return ImmutableMap.of(JsonConst.VALUE, businessService.countInvoicesForYear(businessID, year));
	}
	
	@RequestMapping(value = "/{businessID}/invoices/{year}/totals", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Pair<BigDecimal, BigDecimal> getTotalsForYear(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getTotalsForYear(businessID, year);
	}
	
	@RequestMapping(value = "/{businessID}/stats", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BusinessStatsDTO getStats(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getStats(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/invoices/years", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Integer> getInvoceYears(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getInvoceYears(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/creditnotes/years", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Integer> getCreditNoteYears(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getCreditNoteYears(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/estimations/years", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Integer> getEstimationYears(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getEstimationYears(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/transdocs/years", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Integer> getTransportDocumentYears(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getTransportDocumentYears(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/logrecords", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<LogRecordDTO> getLogRecords(@PathVariable Long businessID, @PathVariable Integer numberOfDays) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getLogRecords(businessID, numberOfDays);
	}
	
	@RequestMapping(value = "/{businessID}/invoices/monthtotals", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<BigDecimal> getInvoiceMonthTotals(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getInvoiceMonthTotals(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/notifications", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<NotificationDTO> getNotifications(@PathVariable Long businessID) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		return businessService.getNotifications(businessID);
	}
	
	@RequestMapping(value = "/{businessID}/notifications/{id}/markseen", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void markNotificationAsSeen(@PathVariable Long businessID, @PathVariable Long id) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		businessService.markNotificationAsSeen(businessID, id);
	}
	
}
