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
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public aspect EstimationServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EstimationServiceActionsAspect.class);
	
	pointcut add(EstimationDTO estimationDTO) : 
		execution(public Long com.novadart.novabill.web.gwt.EstimationGwtController.add(..)) && args(estimationDTO);
	
	pointcut remove(Long businessID, Long clientID, Long id) : 
		execution(public void com.novadart.novabill.web.gwt.EstimationGwtController.remove(..)) && args(businessID, clientID, id);
	
	pointcut update(EstimationDTO estimationDTO) : 
		execution(public void com.novadart.novabill.web.gwt.EstimationGwtController.update(..)) && args(estimationDTO);
	
	
	after(EstimationDTO estimationDTO) returning (Long id) : add(estimationDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addEstimation, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(estimationDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.ESTIMATION, OperationType.CREATE, id, time, estimationDTO.getClient().getName());
	}
	
	void around(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		Client client = Client.findClient(clientID);
		proceed(businessID, clientID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeEstimation, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, clientID, id});
		logActionInDB(businessID, EntityType.ESTIMATION, OperationType.DELETE, id, time, client.getName());
	}
	
	after(EstimationDTO estimationDTO) returning : update(estimationDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateEstimation, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(estimationDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.ESTIMATION, OperationType.UPDATE, estimationDTO.getId(), time, estimationDTO.getClient().getName());
	}
	
}
