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
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public privileged aspect CreditNoteServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditNoteServiceActionsAspect.class);
	
	pointcut add(CreditNoteDTO creditNoteDTO) :
		call(public Long com.novadart.novabill.service.web.CreditNoteService.add(..)) && args(creditNoteDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		call(public void com.novadart.novabill.service.web.CreditNoteService.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(CreditNoteDTO creditNoteDTO) : 
		call(public void com.novadart.novabill.service.web.CreditNoteService.update(..)) && args(creditNoteDTO);
	
	after(CreditNoteDTO creditNoteDTO) returning (Long id) : add(creditNoteDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addCreditNote, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(creditNoteDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, creditNoteDTO.getClient().getName(), DOCUMENT_ID, creditNoteDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.CREDIT_NOTE, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		CreditNote creditNote = CreditNote.findCreditNote(id);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeCreditNote, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, client.getName(), DOCUMENT_ID, creditNote.getDocumentID().toString());
		logActionInDB(businessID, EntityType.CREDIT_NOTE, OperationType.DELETE, id, time, details);
	}
	
	after(CreditNoteDTO creditNoteDTO) returning : update(creditNoteDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateCreditNote, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(creditNoteDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, creditNoteDTO.getClient().getName(), DOCUMENT_ID, creditNoteDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.CREDIT_NOTE, OperationType.UPDATE, creditNoteDTO.getId(), time, details);
	}

}
