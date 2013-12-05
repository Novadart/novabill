package com.novadart.novabill.aspect.logging;

import org.apache.commons.lang3.StringUtils;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;

public abstract aspect DBLoggerAspect {
	
	protected void logActionInDB(Long businessID, EntityType entityType, OperationType opType, Long entityID, Long time, String details){
		LogRecord record = new LogRecord(entityType, opType, entityID, time);
		if(StringUtils.isNotBlank(details))
			record.setDetails(details);
		record.setBusiness(Business.findBusiness(businessID));
		record.persist();
	}
	
	protected void logActionInDB(Long businessID, EntityType entityType, OperationType opType, Long entityID, Long time){
		logActionInDB(businessID, entityType, opType, entityID, time, null);
	}

}
