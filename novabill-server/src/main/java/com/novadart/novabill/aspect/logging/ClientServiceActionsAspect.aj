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
	
	pointcut add(ClientDTO clientDTO) :
		execution(public Long com.novadart.novabill.web.gwt.ClientServiceImpl.add(..)) && args(clientDTO);
	
	pointcut remove(Long id) :
		execution(public void com.novadart.novabill.web.gwt.ClientServiceImpl.remove(..)) && args(id);
	
	pointcut update(ClientDTO clientDTO) :
		execution(public void com.novadart.novabill.web.gwt.ClientServiceImpl.update(..)) && args(clientDTO);
	
	after(ClientDTO clientDTO) returning (Long id) : add(clientDTO){
		LOGGER.info("[{}, addClient, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id,
							ReflectionToStringBuilder.toString(clientDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long id) returning : remove(id){
		LOGGER.info("[{}, removeClient, {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id});
	}
	
	after(ClientDTO clientDTO) : update(clientDTO){
		LOGGER.info("[{}, updateClient, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()),
							ReflectionToStringBuilder.toString(clientDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}

}
