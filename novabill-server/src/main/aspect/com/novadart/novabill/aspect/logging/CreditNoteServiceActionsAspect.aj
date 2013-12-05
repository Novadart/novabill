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
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public privileged aspect CreditNoteServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditNoteServiceActionsAspect.class);
	
	pointcut add(CreditNoteDTO creditNoteDTO) :
		execution(public Long com.novadart.novabill.web.gwt.CreditNoteGwtController.add(..)) && args(creditNoteDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		execution(public void com.novadart.novabill.web.gwt.CreditNoteGwtController.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(CreditNoteDTO creditNoteDTO) : 
		execution(public void com.novadart.novabill.web.gwt.CreditNoteGwtController.update(..)) && args(creditNoteDTO);
	
	after(CreditNoteDTO creditNoteDTO) returning (Long id) : add(creditNoteDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addCreditNote, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(creditNoteDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.CREDIT_NOTE, OperationType.CREATE, id, time, creditNoteDTO.getClient().getName());
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeCreditNote, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		logActionInDB(businessID, EntityType.CREDIT_NOTE, OperationType.DELETE, id, time, client.getName());
	}
	
	after(CreditNoteDTO creditNoteDTO) returning : update(creditNoteDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateCreditNote, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(creditNoteDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.CREDIT_NOTE, OperationType.UPDATE, creditNoteDTO.getId(), time, creditNoteDTO.getClient().getName());
	}

}
