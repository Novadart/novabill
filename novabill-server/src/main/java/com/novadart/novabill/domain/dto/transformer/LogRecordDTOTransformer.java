package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.shared.client.dto.LogRecordDTO;

public class LogRecordDTOTransformer {
	
	public static LogRecordDTO toDTO(LogRecord record){
		if(record == null)
			return null;
		LogRecordDTO recordDTO = new LogRecordDTO();
		recordDTO.setEntityType(record.getEntityType());
		recordDTO.setOperationType(record.getOperationType());
		recordDTO.setEntityID(record.getEntityID());
		recordDTO.setTime(record.getTime());
		recordDTO.setDetails(record.getDetails());
		recordDTO.setReferringToDeletedEntity(record.isReferringToDeletedEntity());
		return recordDTO;
	}
	
}
