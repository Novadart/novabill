package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public aspect InvoiceServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceActionsAspect.class);
	
	pointcut add(InvoiceDTO invoiceDTO) : 
		execution(public Long com.novadart.novabill.web.gwt.InvoiceGwtController.add(..)) && args(invoiceDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		execution(public void com.novadart.novabill.web.gwt.InvoiceGwtController.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(InvoiceDTO invoiceDTO) : 
		execution(public void com.novadart.novabill.web.gwt.InvoiceGwtController.update(..)) && args(invoiceDTO);
	
	pointcut setPayed(Long businessID, Long clientID, Long id, Boolean value) : 
		execution(public void com.novadart.novabill.web.gwt.InvoiceGwtController.setPayed(..)) && args(businessID, clientID, id, value);
	
	after(InvoiceDTO invoiceDTO) returning (Long id) : add(invoiceDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addInvoice, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(invoiceDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, invoiceDTO.getClient().getName(), DOCUMENT_ID, invoiceDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.INVOICE, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		Invoice invoice = Invoice.findInvoice(id);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeInvoice, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, client.getName(), DOCUMENT_ID, invoice.getDocumentID().toString());
		logActionInDB(businessID, EntityType.INVOICE, OperationType.DELETE, id, time, details);
	}
	
	after(InvoiceDTO invoiceDTO) returning : update(invoiceDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateInvoice, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(invoiceDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, invoiceDTO.getClient().getName(), DOCUMENT_ID, invoiceDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.INVOICE, OperationType.UPDATE, invoiceDTO.getId(), time, details);
	}
	
	after(Long businessID, Long clientID, Long id, Boolean value) returning : setPayed(businessID, clientID, id, value){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, setPayed, {}, businessID: {}, clientID: {}, id: {}, value: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id, value});
		Invoice invoice = Invoice.findInvoice(id);
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, invoice.getClient().getName(), DOCUMENT_ID, invoice.getDocumentID().toString());
		logActionInDB(businessID, EntityType.INVOICE, OperationType.SET_PAYED, id, time, details);
	}

}
