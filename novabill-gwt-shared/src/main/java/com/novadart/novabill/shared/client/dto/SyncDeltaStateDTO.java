package com.novadart.novabill.shared.client.dto;

import java.util.Map;

import com.novadart.novabill.shared.client.data.CRUDOperationType;
import com.novadart.novabill.shared.client.data.SyncEntityType;

public class SyncDeltaStateDTO {
	
	private Long mark;
	
	private Map<SyncEntityType, Map<CRUDOperationType, Map<Long, Integer>>> deltaState;

	public Long getMark() {
		return mark;
	}

	public void setMark(Long mark) {
		this.mark = mark;
	}

	public Map<SyncEntityType, Map<CRUDOperationType, Map<Long, Integer>>> getDeltaState() {
		return deltaState;
	}

	public void setDeltaState(Map<SyncEntityType, Map<CRUDOperationType, Map<Long, Integer>>> deltaState) {
		this.deltaState = deltaState;
	}
	
}
