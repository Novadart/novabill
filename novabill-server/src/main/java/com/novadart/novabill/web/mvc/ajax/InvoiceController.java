package com.novadart.novabill.web.mvc.ajax;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.web.mvc.ajax.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/invoices")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UtilsService utilsService;
	
	@RequestMapping(value = "/year/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<InvoiceDTO> getInvoices(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getInvoices(businessID, year);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public InvoiceDTO get(@PathVariable Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException {
		return invoiceService.get(id);
	}
	
	@RequestMapping(value = "/year/{year}/start/{start}/offset/{length}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PageDTO<InvoiceDTO> getAllInRange(@PathVariable Long businessID, @PathVariable Integer year,
			@PathVariable Integer start, @PathVariable Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllInRange(businessID, year, start, length);
	}

	@RequestMapping(value = "/year/{year}/suffix/{suffix}/start/{start}/offset/{length}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PageDTO<InvoiceDTO> getAllInRange(@PathVariable Long businessID, @PathVariable Integer year, @PathVariable String suffix,
											 @PathVariable Integer start, @PathVariable Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllInRange(businessID, year, suffix, start, length);
	}
	
	@RequestMapping(value = "/year/{year}/clients/{clientID}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<InvoiceDTO> getAllForClient(@PathVariable Long clientID, @PathVariable Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException {
		return invoiceService.getAllForClient(clientID, year);
	}
	
	@RequestMapping(value = "/{id}/clients/{clientID}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void remove(@PathVariable Long businessID, @PathVariable Long clientID, @PathVariable Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		invoiceService.remove(businessID, clientID, id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> add(@RequestBody InvoiceDTO invoiceDTO) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataIntegrityException {
		return ImmutableMap.of(JsonConst.VALUE, invoiceService.add(invoiceDTO));
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException, FreeUserAccessForbiddenException, NotAuthenticatedException {
		invoiceService.update(invoiceDTO);
	}

	@RequestMapping(value = "/nextid", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> getNextInvoiceDocumentID(@RequestParam(value = "suffix", required = false) String suffix) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		return ImmutableMap.of(JsonConst.VALUE, invoiceService.getNextInvoiceDocumentID(suffix) + suffix);
	}

	@RequestMapping(value = "/year/{year}/clients/{clientID}/start/{start}/offset/{length}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PageDTO<InvoiceDTO> getAllForClientInRange(@PathVariable Long clientID, @PathVariable Integer year, 
			@PathVariable Integer start, @PathVariable Integer length) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException {
		return invoiceService.getAllForClientInRange(clientID, year, start, length);
	}
	
	@RequestMapping(value = "/{id}/clients/{clientID}/payed/{value}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void setPayed(@PathVariable Long businessID, @PathVariable Long clientID, @PathVariable Long id,
			@PathVariable Boolean value) throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException {
		invoiceService.setPayed(businessID, clientID, id, value);
	}
	
	@RequestMapping(value = "/filtertype/{filteringDateType}/start/{startDate}/end/{endDate}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<InvoiceDTO> getAllUnpaidInDateRange(@PathVariable FilteringDateType filteringDateType,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) Date endDate) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return invoiceService.getAllUnpaidInDateRange(filteringDateType, startDate, endDate);
	}
	
	@RequestMapping(value = "/{id}/email", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> email(@PathVariable Long id, @RequestBody EmailDTO emailDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, DataAccessException, NotAuthenticatedException, FreeUserAccessForbiddenException {
		return ImmutableMap.of(JsonConst.VALUE, invoiceService.email(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), id, emailDTO));
	}

}
