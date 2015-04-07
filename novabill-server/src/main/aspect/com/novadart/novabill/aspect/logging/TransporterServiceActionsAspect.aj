package com.novadart.novabill.aspect.logging;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

public aspect TransporterServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransporterServiceActionsAspect.class);
	
	pointcut add(TransporterDTO transporterDTO) :
		execution(public Long com.novadart.novabill.service.web.TransporterService.add(..)) && args(transporterDTO);
	
	pointcut remove(Long businessID, Long id) : 
		execution(public void com.novadart.novabill.service.web.TransporterService.remove(..)) && args(businessID, id);
	
	pointcut update(TransporterDTO transporterDTO) : 
		execution(public void com.novadart.novabill.service.web.TransporterService.update(..)) && args(transporterDTO);
	
	
	after(TransporterDTO transporterDTO) returning (Long id) : add(transporterDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addTransporter, {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), id,
							ReflectionToStringBuilder.toString(transporterDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(TRANSPORTER_NAME, transporterDTO.getName());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.TRANSPORTER, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long id) : remove(businessID, id){
		Transporter transporter = Transporter.findTransporter(id);
		proceed(businessID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeTransporter, {}, businessID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id});
		Map<String, String> details = ImmutableMap.of(TRANSPORTER_NAME, transporter.getName());
		logActionInDB(businessID, EntityType.TRANSPORTER, OperationType.DELETE, id, time, details);
	}
	
	after(TransporterDTO transporterDTO) returning : update(transporterDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateTransporter, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time),
				ReflectionToStringBuilder.toString(transporterDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(TRANSPORTER_NAME, transporterDTO.getName());
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.TRANSPORTER, OperationType.UPDATE, transporterDTO.getId(), time, details);
	}

}
