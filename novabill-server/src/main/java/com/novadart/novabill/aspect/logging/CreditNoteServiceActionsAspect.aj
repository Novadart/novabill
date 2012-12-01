package com.novadart.novabill.aspect.logging;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public privileged aspect CreditNoteServiceActionsAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditNoteServiceActionsAspect.class);
	
	pointcut add(CreditNoteDTO creditNoteDTO) :
		execution(public Long com.novadart.novabill.web.gwt.CreditNoteServiceImpl.add(..)) && args(creditNoteDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		execution(public void com.novadart.novabill.web.gwt.CreditNoteServiceImpl.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(CreditNoteDTO creditNoteDTO) : 
		execution(public void com.novadart.novabill.web.gwt.CreditNoteServiceImpl.update(..)) && args(creditNoteDTO);
	
	after(CreditNoteDTO creditNoteDTO) returning (Long id) : add(creditNoteDTO){
		LOGGER.info("[{}, addCreditNote, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id,
							ReflectionToStringBuilder.toString(creditNoteDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		LOGGER.info("[{}, removeCreditNote, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), businessID, clientID, id});
	}
	
	after(CreditNoteDTO creditNoteDTO) returning : update(creditNoteDTO){
		LOGGER.info("[{}, updateCreditNote, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()),
				ReflectionToStringBuilder.toString(creditNoteDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}

}
