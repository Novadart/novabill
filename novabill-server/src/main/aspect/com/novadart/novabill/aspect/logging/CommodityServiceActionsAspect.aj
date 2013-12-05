package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.CommodityDTO;

public aspect CommodityServiceActionsAspect extends DBLoggerAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommodityServiceActionsAspect.class);
	
	pointcut add(CommodityDTO commodityDTO) : 
		execution(public Long com.novadart.novabill.web.gwt.CommodityGwtController.add(..)) && args(commodityDTO);
	
	pointcut remove(Long businessID, Long id) : 
		execution(public void com.novadart.novabill.web.gwt.CommodityGwtController.remove(..)) && args(businessID, id);
	
	pointcut update(CommodityDTO commodityDTO) : 
		execution(public void com.novadart.novabill.web.gwt.CommodityGwtController.update(..)) && args(commodityDTO);
	
	after(CommodityDTO commodityDTO) returning (Long id) : add(commodityDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, addCommodity, {}, businessID: {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), commodityDTO.getBusiness().getId(), id,
							ReflectionToStringBuilder.toString(commodityDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.COMMODITY, OperationType.CREATE, id, time, commodityDTO.getDescription());
	}
	
	void around(Long businessID, Long id) : remove(businessID, id){
		Commodity commodity = Commodity.findCommodity(id);
		proceed(businessID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removeCommodity, {}, businessID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id});
		logActionInDB(businessID, EntityType.COMMODITY, OperationType.DELETE, id, time, commodity.getDescription());
	}
	
	after(CommodityDTO commodityDTO) returning : update(commodityDTO){
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, updateCommodity, {}, businessID: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), commodityDTO.getBusiness().getId(),
							ReflectionToStringBuilder.toString(commodityDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		logActionInDB(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(),
				EntityType.COMMODITY, OperationType.UPDATE, commodityDTO.getId(), time, commodityDTO.getDescription());
	}


}
