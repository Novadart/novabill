package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.shared.client.dto.LogRecordDTO;

public class LogRecordDTOFactory {
	
	public static LogRecordDTO toDTO(LogRecord record){
		if(record == null)
			return null;
		LogRecordDTO recordDTO = new LogRecordDTO();
		recordDTO.setEntityType(record.getEntityType());
		recordDTO.setOperationType(record.getOperationType());
		recordDTO.setEntityID(record.getEntityID());
		recordDTO.setTime(record.getTime());
		recordDTO.setDetails(record.getDetails());
		return recordDTO;
	}
	
}
