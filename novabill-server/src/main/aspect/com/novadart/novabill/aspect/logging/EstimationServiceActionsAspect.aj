package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public aspect EstimationServiceActionsAspect {
	
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
		LOGGER.info("[{}, addEstimation, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), id,
							ReflectionToStringBuilder.toString(estimationDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
	after(Long businessID, Long clientID, Long id) : remove(businessID, clientID, id){
		LOGGER.info("[{}, removeEstimation, {}, businessID: {}, clientID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), businessID, clientID, id});
	}
	
	after(EstimationDTO estimationDTO) returning : update(estimationDTO){
		LOGGER.info("[{}, updateEstimation, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()),
				ReflectionToStringBuilder.toString(estimationDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
}
