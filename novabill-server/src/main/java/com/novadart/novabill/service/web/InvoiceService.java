package com.novadart.novabill.service.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.MailDeliveryStatus;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.web.mvc.ajax.dto.EmailDTO;

public interface InvoiceService {

	InvoiceDTO get(Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException;

	PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;

	PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, String docIDSuffix, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;

	List<InvoiceDTO> getAllForClient(Long clientID, Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException;

	List<InvoiceDTO> getAllForClient(Long clientID, Integer year, String docIDSuffix) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException;

	void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException;

	Long add(InvoiceDTO invoiceDTO) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataIntegrityException;

	void update(InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException, FreeUserAccessForbiddenException, NotAuthenticatedException;

	Long getNextInvoiceDocumentID(String suffix) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException;

	PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException;

	void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException;

	List<InvoiceDTO> getAllUnpaidInDateRange(FilteringDateType filteringDateType, Date startDate, Date endDate) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;

	boolean email(Long businessID, Long id, EmailDTO emailDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException;
	
	void markViewedByClient(Long businessID, Long id, Long viewingTime);

	void setEmailedToClientStatus(Long businessID, Long id, MailDeliveryStatus status);

}