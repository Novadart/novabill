package com.novadart.novabill.frontend.client.bridge.server.autobean;


public interface LogRecord {

	String getEntityType();

	void setEntityType(String entityType);

	String getOperationType();

	void setOperationType(String operationType);

	Long getEntityID();

	void setEntityID(Long entityID);

	Long getTime();

	void setTime(Long time);

	String getDetails();

	void setDetails(String details);
}
