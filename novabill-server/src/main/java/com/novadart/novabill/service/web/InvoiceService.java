package com.novadart.novabill.service.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.web.mvc.ajax.dto.EmailDTO;

public interface InvoiceService {

	public InvoiceDTO get(Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException;

	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;

	public List<InvoiceDTO> getAllForClient(Long clientID, Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException;

	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, DataIntegrityException;

	public Long add(InvoiceDTO invoiceDTO) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataIntegrityException;

	public void update(InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException;

	public Long getNextInvoiceDocumentID(String suffix);

	public PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException;

	public void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException;

	public List<InvoiceDTO> getAllUnpaidInDateRange(FilteringDateType filteringDateType, Date startDate, Date endDate) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;

	public boolean email(Long businessID, Long id, EmailDTO emailDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException;
	
	public void markViewedByClient(Long businessID, Long id, Long viewingTime);

}