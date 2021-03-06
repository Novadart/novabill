package com.novadart.novabill.shared.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;

public class LogRecordDTO implements IsSerializable{
	
	private EntityType entityType;
	
	private OperationType operationType;
	
	private Long entityID;
	
	private Long time;
	
	private String details;
	
	private Boolean referringToDeletedEntity;

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Long getEntityID() {
		return entityID;
	}

	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Boolean getReferringToDeletedEntity() {
		return referringToDeletedEntity;
	}

	public void setReferringToDeletedEntity(Boolean referringToDeletedEntity) {
		this.referringToDeletedEntity = referringToDeletedEntity;
	}
	
}
