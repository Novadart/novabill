package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.PriceListDTO;

public aspect PriceListServiceActionsAspect extends DBLoggerAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceListServiceActionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut add(PriceListDTO priceListDTO) :
		execution(public Long com.novadart.novabill.service.web.PriceListService.add(..)) && args(priceListDTO);
	
	pointcut remove(Long businessID, Long id) :
		execution(public void com.novadart.novabill.service.web.PriceListService.remove(..)) && args(businessID, id);
	
	pointcut update(PriceListDTO priceListDTO) :
		execution(public void com.novadart.novabill.service.web.PriceListService.update(..)) && args(priceListDTO);
	
	after(PriceListDTO priceListDTO) returning (Long id) : add(priceListDTO){
		Long time = System.currentTimeMillis();
		Long businessID = priceListDTO.getBusiness().getId();
		LOGGER.info("[{}, addPriceList, {}, businessID: {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id,
							ReflectionToStringBuilder.toString(priceListDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(PRICE_LIST_NAME, priceListDTO.getName());
		logActionInDB(businessID, EntityType.PRICE_LIST, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long id) : remove(businessID, id){
		PriceList priceList = PriceList.findPriceList(id);
		proceed(businessID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removePriceList, {}, businessID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id});
		Map<String, String> details = ImmutableMap.of(PRICE_LIST_NAME, priceList.getName());
		logActionInDB(businessID, EntityType.PRICE_LIST, OperationType.DELETE, id, time, details);
	}
	
	after(PriceListDTO priceListDTO) returning : update(priceListDTO){
		Long time = System.currentTimeMillis();
		Long businessID = priceListDTO.getBusiness().getId();
		LOGGER.info("[{}, updatePriceList, {}, businessID: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID,
							ReflectionToStringBuilder.toString(priceListDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(PRICE_LIST_NAME, priceListDTO.getName());
		logActionInDB(businessID, EntityType.PRICE_LIST, OperationType.UPDATE, priceListDTO.getId(), time, details);
	}

}
