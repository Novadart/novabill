package com.novadart.novabill.aspect.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;

import java.util.Arrays;
import java.util.Map;

public abstract aspect DBLoggerAspect {
	
	public static final String CLIENT_NAME = "clientName";
	
	public static final String COMMODITY_NAME = "commodityName";
	
	public static final String DOCUMENT_ID = "documentID";
	
	public static final String PAYMENT_TYPE_NAME = "paymentTypeName";
	
	public static final String PRICE_LIST_NAME = "priceListName";
	
	public static final String PAYED_STATUS = "payedStatus";
	
	public static final String SHARING_PERMIT_DESC = "sharingPermitDesc";
	
	public static final String REPLY_TO = "replyTo";
	
	public static final String TRANSPORTER_NAME = "transporterName";

	public static final String DOCUMENT_ID_CLASS_NAME = "documentIdClassName";
	
	private String toJsonString(Map<String, String> jsonObject) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(jsonObject);
		} catch (Exception e) {
			throw new RuntimeException("Json conversion problem");
		}
	}
	
	protected void logActionInDB(Long businessID, EntityType entityType, OperationType opType, Long entityID, Long time, Map<String, String> details){
		boolean isDeleteOp = OperationType.DELETE.equals(opType);
		LogRecord record = new LogRecord(entityType, opType, entityID, time);
		record.setReferringToDeletedEntity(isDeleteOp);
		if(details != null)
			record.setDetails(toJsonString(details));
		record.setBusiness(Business.findBusiness(businessID));
		Long logRecID = record.merge().getId();
		if(isDeleteOp)
			LogRecord.markAsReferringToDeletedEntity(businessID, entityID, entityType, Arrays.asList(logRecID));
	}
	
	protected void logActionInDB(Long businessID, EntityType entityType, OperationType opType, Long entityID, Long time){
		logActionInDB(businessID, entityType, opType, entityID, time, null);
	}

}
