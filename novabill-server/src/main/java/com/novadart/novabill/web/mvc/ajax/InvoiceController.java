package com.novadart.novabill.web.mvc.ajax;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.web.mvc.ajax.dto.EmailInvoiceDTO;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/invoices")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private BusinessService businessService;
	
	@RequestMapping(value = "/year/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<InvoiceDTO> getInvoices(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.getInvoices(businessID, year);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public InvoiceDTO get(@PathVariable Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException{
		return invoiceService.get(id);
	}
	
	@RequestMapping(value = "/year/{year}/start/{start}/offset/{length}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PageDTO<InvoiceDTO> getAllInRange(@PathVariable Long businessID, @PathVariable Integer year,
			@PathVariable Integer start, @PathVariable Integer length) throws NotAuthenticatedException, DataAccessException{
		return invoiceService.getAllInRange(businessID, year, start, length);
	}
	
	@RequestMapping(value = "/year/{year}/clients/{clientID}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<InvoiceDTO> getAllForClient(@PathVariable Long clientID, @PathVariable Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
		return invoiceService.getAllForClient(clientID, year);
	}
	
	@RequestMapping(value = "/{id}/clients/{clientID}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void remove(@PathVariable Long businessID, @PathVariable Long clientID, @PathVariable Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, DataIntegrityException {
		invoiceService.remove(businessID, clientID, id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String add(@RequestBody InvoiceDTO invoiceDTO) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataIntegrityException {
		return invoiceService.add(invoiceDTO).toString();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException {
		invoiceService.update(invoiceDTO);
	}

	@RequestMapping(value = "/nextid", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getNextInvoiceDocumentID() {
		return invoiceService.getNextInvoiceDocumentID().toString();
	}

	@RequestMapping(value = "/year/{year}/clients/{clientID}/start/{start}/offset/{length}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PageDTO<InvoiceDTO> getAllForClientInRange(@PathVariable Long clientID, @PathVariable Integer year, 
			@PathVariable Integer start, @PathVariable Integer length) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
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
	public void email(@PathVariable Long id, @RequestBody EmailInvoiceDTO emailInvoiceDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		invoiceService.email(id, emailInvoiceDTO.getTo(), emailInvoiceDTO.getReplyTo(), emailInvoiceDTO.getSubject(), emailInvoiceDTO.getMessage());
	}

}
