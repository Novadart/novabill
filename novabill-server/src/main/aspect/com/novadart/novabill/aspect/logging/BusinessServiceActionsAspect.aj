package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

public aspect BusinessServiceActionsAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceActionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut update(BusinessDTO businessDTO) :
		execution(public void com.novadart.novabill.web.gwt.BusinessServiceImpl.update(..)) && args(businessDTO);
	
	after(BusinessDTO businessDTO) returning : update(businessDTO){
		LOGGER.info("[{}, updateBusiness, {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()),
				ReflectionToStringBuilder.toString(businessDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
	}
	
}
