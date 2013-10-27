package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public aspect InvoiceServiceActionsAspect {
	
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
		LOGGER.info("[{}, addInvoice, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id,
							ReflectionToStringBuilder.toString(invoiceDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		LOGGER.info("[{}, removeInvoice, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), businessID, clientID, id});
	}
	
	after(InvoiceDTO invoiceDTO) returning : update(invoiceDTO){
		LOGGER.info("[{}, updateInvoice, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()),
				ReflectionToStringBuilder.toString(invoiceDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long businessID, Long clientID, Long id, Boolean value) returning : setPayed(businessID, clientID, id, value){
		LOGGER.info("[{}, setPayed, {}, businessID: {}, clientID: {}, id: {}, value: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), businessID, clientID, id, value});
	}

}
