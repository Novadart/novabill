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
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public privileged aspect TransportDocumentServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransportDocumentServiceActionsAspect.class);
	
	pointcut add(TransportDocumentDTO transportDocDTO) :
		execution(public Long com.novadart.novabill.service.web.TransportDocumentService.add(..)) && args(transportDocDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		execution(public void com.novadart.novabill.service.web.TransportDocumentService.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(TransportDocumentDTO transportDocDTO) : 
		execution(public void com.novadart.novabill.service.web.TransportDocumentService.update(..)) && args(transportDocDTO);
	
	
	after(TransportDocumentDTO transportDocDTO) returning (Long id) : add(transportDocDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addTransportDoc, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(transportDocDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, transportDocDTO.getClient().getName(), DOCUMENT_ID, transportDocDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.TRANSPORT_DOCUMENT, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		TransportDocument transDoc = TransportDocument.findTransportDocument(id);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeTransportDoc, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, client.getName(), DOCUMENT_ID, transDoc.getDocumentID().toString());
		logActionInDB(businessID, EntityType.TRANSPORT_DOCUMENT, OperationType.DELETE, id, time, details);
	}
	
	after(TransportDocumentDTO transportDocDTO) returning : update(transportDocDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateTransportDoc, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(transportDocDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, transportDocDTO.getClient().getName(), DOCUMENT_ID, transportDocDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.TRANSPORT_DOCUMENT, OperationType.UPDATE, transportDocDTO.getId(), time, details);
	}

}
