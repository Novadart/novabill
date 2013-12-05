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
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public privileged aspect TransportDocumentServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransportDocumentServiceActionsAspect.class);
	
	pointcut add(TransportDocumentDTO transportDocDTO) :
		execution(public Long com.novadart.novabill.web.gwt.TransportDocumentGwtController.add(..)) && args(transportDocDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		execution(public void com.novadart.novabill.web.gwt.TransportDocumentGwtController.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(TransportDocumentDTO transportDocDTO) : 
		execution(public void com.novadart.novabill.web.gwt.TransportDocumentGwtController.update(..)) && args(transportDocDTO);
	
	
	after(TransportDocumentDTO transportDocDTO) returning (Long id) : add(transportDocDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addTransportDoc, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(transportDocDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.TRANSPORT_DOCUMENT, OperationType.CREATE, id, time, transportDocDTO.getClient().getName());
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeTransportDoc, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		logActionInDB(businessID, EntityType.TRANSPORT_DOCUMENT, OperationType.DELETE, id, time, client.getName());
	}
	
	after(TransportDocumentDTO transportDocDTO) returning : update(transportDocDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateTransportDoc, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(transportDocDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.TRANSPORT_DOCUMENT, OperationType.UPDATE, transportDocDTO.getId(), time, transportDocDTO.getClient().getName());
	}

}
