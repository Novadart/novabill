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
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public aspect ClientServiceActionsAspect extends DBLoggerAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceActionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut add(Long businessID, ClientDTO clientDTO) :
		call(public Long com.novadart.novabill.service.web.ClientService.add(..)) && args(businessID, clientDTO);
	
	pointcut remove(Long businessID, Long id) :
		call(public boolean com.novadart.novabill.service.web.ClientService.remove(..)) && args(businessID, id);
	
	pointcut update(Long businessID, ClientDTO clientDTO) :
		call(public void com.novadart.novabill.service.web.ClientService.update(..)) && args(businessID, clientDTO);
	
	after(Long businessID, ClientDTO clientDTO) returning (Long id) : add(businessID, clientDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addClient, {}, businessID: {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id,
							ReflectionToStringBuilder.toString(clientDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, clientDTO.getName());
		logActionInDB(businessID, EntityType.CLIENT, OperationType.CREATE, id, time, details);
	}
	
	boolean around(Long businessID, Long id) : remove(businessID, id){
		Client client = Client.findClient(id);
		boolean result = proceed(businessID, id);
		if(result){
			Long time = System.currentTimeMillis();
			LOGGER.info("[{}, removeClient, {}, businessID: {}, id: {}]", new Object[] {utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id});
			Map<String, String> details = ImmutableMap.of(CLIENT_NAME, client.getName());
			logActionInDB(businessID, EntityType.CLIENT, OperationType.DELETE, id, time, details);
		}
		return result;
	}
	
	after(Long businessID, ClientDTO clientDTO) returning : update(businessID, clientDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateClient, {}, businessID: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID,
							ReflectionToStringBuilder.toString(clientDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, clientDTO.getName());
		logActionInDB(businessID, EntityType.CLIENT, OperationType.UPDATE, clientDTO.getId(), time, details);
	}

}
