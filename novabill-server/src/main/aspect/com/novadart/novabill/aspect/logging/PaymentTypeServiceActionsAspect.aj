package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public aspect PaymentTypeServiceActionsAspect extends DBLoggerAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentTypeServiceActionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut add(PaymentTypeDTO paymentTypeDTO) :
		call(public Long com.novadart.novabill.service.web.PaymentTypeService.add(..)) && args(paymentTypeDTO);
	
	pointcut remove(Long businessID, Long id) :
		call(public void com.novadart.novabill.service.web.PaymentTypeService.remove(..)) && args(businessID, id);
	
	pointcut update(PaymentTypeDTO paymentTypeDTO) :
		call(public void com.novadart.novabill.service.web.PaymentTypeService.update(..)) && args(paymentTypeDTO);
	
	after(PaymentTypeDTO paymentTypeDTO) returning (Long id) : add(paymentTypeDTO){
		Long time = System.currentTimeMillis();
		Long businessID = paymentTypeDTO.getBusiness().getId();
		LOGGER.info("[{}, addPaymentType, {}, businessID: {}, id: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id,
							ReflectionToStringBuilder.toString(paymentTypeDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(PAYMENT_TYPE_NAME, paymentTypeDTO.getName());
		logActionInDB(businessID, EntityType.PAYMENT_TYPE, OperationType.CREATE, id, time, details);
	}
	
	void around(Long businessID, Long id) : remove(businessID, id){
		PaymentType paymentType = PaymentType.findPaymentType(id);
		proceed(businessID, id);
		Long time = System.currentTimeMillis();
		LOGGER.info("[{}, removePaymentType, {}, businessID: {}, id: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id});
		Map<String, String> details = ImmutableMap.of(PAYMENT_TYPE_NAME, paymentType.getName());
		logActionInDB(businessID, EntityType.PAYMENT_TYPE, OperationType.DELETE, id, time, details);
	}
	
	after(PaymentTypeDTO paymentTypeDTO) returning : update(paymentTypeDTO){
		Long time = System.currentTimeMillis();
		Long businessID = paymentTypeDTO.getBusiness().getId();
		LOGGER.info("[{}, updatePaymentType, {}, businessID: {}, dto: {}]",
				new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID,
							ReflectionToStringBuilder.toString(paymentTypeDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
		Map<String, String> details = ImmutableMap.of(PAYMENT_TYPE_NAME, paymentTypeDTO.getName());
		logActionInDB(businessID, EntityType.PAYMENT_TYPE, OperationType.UPDATE, paymentTypeDTO.getId(), time, details);
	}

}
