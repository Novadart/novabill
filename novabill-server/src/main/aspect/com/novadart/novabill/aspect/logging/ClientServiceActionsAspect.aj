package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public aspect ClientServiceActionsAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceActionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut add(Long businessID, ClientDTO clientDTO) :
		execution(public Long com.novadart.novabill.web.gwt.ClientServiceImpl.add(..)) && args(businessID, clientDTO);
	
	pointcut remove(Long businessID, Long id) :
		execution(public void com.novadart.novabill.web.gwt.ClientServiceImpl.remove(..)) && args(businessID, id);
	
	pointcut update(Long businessID, ClientDTO clientDTO) :
		execution(public void com.novadart.novabill.web.gwt.ClientServiceImpl.update(..)) && args(businessID, clientDTO);
	
	after(Long businessID, ClientDTO clientDTO) returning (Long id) : add(businessID, clientDTO){
		LOGGER.info("[{}, addClient, {}, businessID: {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), businessID, id,
							ReflectionToStringBuilder.toString(clientDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long businessID, Long id) returning : remove(businessID, id){
		LOGGER.info("[{}, removeClient, {}, businessID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), businessID, id});
	}
	
	after(Long businessID, ClientDTO clientDTO) : update(businessID, clientDTO){
		LOGGER.info("[{}, updateClient, {}, businessID: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), businessID,
							ReflectionToStringBuilder.toString(clientDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}

}
