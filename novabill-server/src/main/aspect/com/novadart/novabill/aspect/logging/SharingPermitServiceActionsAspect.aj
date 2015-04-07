package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;

public aspect SharingPermitServiceActionsAspect extends DBLoggerAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SharingPermitServiceActionsAspect.class);

	@Autowired
	private UtilsService utilsService;
	
	pointcut add(Long businessID, SharingPermitDTO sharingPermitDTO):
		execution(public Long com.novadart.novabill.service.web.SharingPermitService.add(..)) && args(businessID, sharingPermitDTO);
	
	pointcut remove(Long businessID, Long id):
		execution(public void com.novadart.novabill.service.web.SharingPermitService.remove(..)) && args(businessID, id);
	
	after(Long businessID, SharingPermitDTO sharingPermitDTO) returning (Long id): add(businessID, sharingPermitDTO) {
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, grantSharingPermit, {}, businessID: {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id,
							ReflectionToStringBuilder.toString(sharingPermitDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(SHARING_PERMIT_DESC, sharingPermitDTO.getDescription());
		logActionInDB(businessID, EntityType.SHARING_PERMIT, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long id): remove(businessID, id) {
		SharingPermit sharingPermit = SharingPermit.findSharingPermit(id);
		proceed(businessID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, revokeSharingPermit, {}, businessID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id});
		Map<String, String> details = ImmutableMap.of(SHARING_PERMIT_DESC, sharingPermit.getDescription());
		logActionInDB(businessID, EntityType.SHARING_PERMIT, OperationType.DELETE, id, time, details);
	}

}
