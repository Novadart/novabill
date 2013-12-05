package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.domain.Client;
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
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.INVOICE, OperationType.CREATE, id, time, invoiceDTO.getClient().getName());
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeInvoice, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		logActionInDB(businessID, EntityType.INVOICE, OperationType.DELETE, id, time, client.getName());
	}
	
	after(InvoiceDTO invoiceDTO) returning : update(invoiceDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateInvoice, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(invoiceDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.INVOICE, OperationType.UPDATE, invoiceDTO.getId(), time, invoiceDTO.getClient().getName());
	}
	
	after(Long businessID, Long clientID, Long id, Boolean value) returning : setPayed(businessID, clientID, id, value){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, setPayed, {}, businessID: {}, clientID: {}, id: {}, value: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id, value});
		logActionInDB(businessID, EntityType.INVOICE, OperationType.SET_PAYED, id, time);
	}

}
