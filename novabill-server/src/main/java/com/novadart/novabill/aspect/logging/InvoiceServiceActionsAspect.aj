package com.novadart.novabill.aspect.logging;

import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.novadart.novabill.service.UtilsService;
import java.util.Date;

public aspect InvoiceServiceActionsAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceActionsAspect.class);
	
	pointcut add(InvoiceDTO invoiceDTO) : 
		execution(public Long com.novadart.novabill.web.gwt.InvoiceServiceImpl.add(..)) && args(invoiceDTO);
	
	pointcut remove(Long id) : 
		execution(public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.remove(..)) && args(id);
	
	pointcut update(InvoiceDTO invoiceDTO) : 
		execution(public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.update(..)) && args(invoiceDTO);
	
	pointcut setPayed(Long id, Boolean value) : 
		execution(public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.setPayed(..)) && args(id, value);
	
	after(InvoiceDTO invoiceDTO) returning (Long id) : add(invoiceDTO){
		LOGGER.info("[{}, addInvoice, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id,
							ReflectionToStringBuilder.toString(invoiceDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long id) : remove(id){
		LOGGER.info("[{}, removeInvoice, {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id});
	}
	
	after(InvoiceDTO invoiceDTO) returning : update(invoiceDTO){
		LOGGER.info("[{}, updateInvoice, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()),
				ReflectionToStringBuilder.toString(invoiceDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long id, Boolean value) returning : setPayed(id, value){
		LOGGER.info("[{}, setPayed, {}, id: {}, value: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id, value});
	}

}
