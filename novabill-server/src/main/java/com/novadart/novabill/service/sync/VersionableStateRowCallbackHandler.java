package com.novadart.novabill.service.sync;

import com.novadart.novabill.android.shared.data.CRUDOperationType;
import com.novadart.novabill.android.shared.data.SyncEntityType;
import com.novadart.novabill.android.shared.dto.SyncDeltaStateDTO;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class VersionableStateRowCallbackHandler implements RowCallbackHandler {
	
	public static final String ID = "id";
	public static final String ENTITY_ID = "entity_id";
	public static final String ENTITY = "entity";
	public static final String OPERATION = "operation";
	public static final String VERSION = "version";
	
	public static final String CREATE = "create";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";

	private Map<SyncEntityType, Map<CRUDOperationType, Map<Long, Integer>>> deltaState = initDeltaState();
	
	private Long mark = -1l;
	
	public SyncDeltaStateDTO getResult() {
		SyncDeltaStateDTO result = new SyncDeltaStateDTO();
		result.setMark(mark);
		result.setDeltaState(deltaState);
		return result;
	}
	
	private Map<SyncEntityType, Map<CRUDOperationType, Map<Long, Integer>>> initDeltaState(){
		deltaState = new HashMap<>();
		for(SyncEntityType entityType: SyncEntityType.values()){
			Map<CRUDOperationType, Map<Long, Integer>> entityDelta = new HashMap<>();
			for(CRUDOperationType operationType: CRUDOperationType.values())
				entityDelta.put(operationType, new HashMap<Long, Integer>());
			deltaState.put(entityType, entityDelta);
		}
		return deltaState;
	}
	
	@Override
	public void processRow(ResultSet rs) throws SQLException {
		initDeltaState();
		mark = -1l;
		do{
			String operation = rs.getString(OPERATION);
			Long entityID = rs.getLong(ENTITY_ID);
			Integer version = rs.getInt(VERSION);
			String entity = rs.getString(ENTITY);
			Map<CRUDOperationType, Map<Long, Integer>> entityDelta = deltaState.get(SyncEntityType.valueOf(entity.toUpperCase()));
			Map<Long, Integer> created = entityDelta.get(CRUDOperationType.CREATE);
			Map<Long, Integer> updated = entityDelta.get(CRUDOperationType.UPDATE);
			Map<Long, Integer> deleted = entityDelta.get(CRUDOperationType.DELETE);
			switch (operation) {
			case CREATE:
				created.put(entityID, version);
				break;
			case UPDATE:
				if(created.containsKey(entityID))
					created.put(entityID, version); //update version
				else
					updated.put(entityID, version);
				break;
			case DELETE:
				if(created.containsKey(entityID))
					created.remove(entityID);
				if(updated.containsKey(entityID))
					updated.remove(entityID);
				deleted.put(entityID, version);
				break;
			default:
				throw new RuntimeException("No such operation:" + operation);
			}
			Long id = rs.getLong(ID);
			if(mark < id) mark = id;
		} while(rs.next());
	}

}
