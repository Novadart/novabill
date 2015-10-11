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

	InvoiceDTO get(Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException;

	PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;

	PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, String docIDSuffix, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException;

	List<InvoiceDTO> getAllForClient(Long clientID, Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException;

	List<InvoiceDTO> getAllForClient(Long clientID, Integer year, String docIDSuffix) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException;

	void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, DataIntegrityException;

	Long add(InvoiceDTO invoiceDTO) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataIntegrityException;

	void update(InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException;

	Long getNextInvoiceDocumentID(String suffix);

	PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException;

	void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException;

	List<InvoiceDTO> getAllUnpaidInDateRange(FilteringDateType filteringDateType, Date startDate, Date endDate) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;

	boolean email(Long businessID, Long id, EmailDTO emailDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException;
	
	void markViewedByClient(Long businessID, Long id, Long viewingTime);

	void setEmailedToClientStatus(Long businessID, Long id, MailDeliveryStatus status);

}