package com.novadart.novabill.aspect.logging;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

public aspect EstimationServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EstimationServiceActionsAspect.class);
	
	pointcut add(EstimationDTO estimationDTO) : 
		execution(public Long com.novadart.novabill.service.web.EstimationService.add(..)) && args(estimationDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		execution(public void com.novadart.novabill.service.web.EstimationService.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(EstimationDTO estimationDTO) : 
		execution(public void com.novadart.novabill.service.web.EstimationService.update(..)) && args(estimationDTO);
	
	
	after(EstimationDTO estimationDTO) returning (Long id) : add(estimationDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addEstimation, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(estimationDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, estimationDTO.getClient().getName(), DOCUMENT_ID, estimationDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.ESTIMATION, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		Estimation estimation = Estimation.findEstimation(id);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeEstimation, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, client.getName(), DOCUMENT_ID, estimation.getDocumentID().toString());
		logActionInDB(businessID, EntityType.ESTIMATION, OperationType.DELETE, id, time, details);
	}
	
	after(EstimationDTO estimationDTO) returning : update(estimationDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateEstimation, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(estimationDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(CLIENT_NAME, estimationDTO.getClient().getName(), DOCUMENT_ID, estimationDTO.getDocumentID().toString());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.ESTIMATION, OperationType.UPDATE, estimationDTO.getId(), time, details);
	}
	
}
