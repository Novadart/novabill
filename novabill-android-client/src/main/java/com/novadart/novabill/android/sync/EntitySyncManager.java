package com.novadart.novabill.android.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.novadart.novabill.android.shared.data.CRUDOperationType;
import com.novadart.novabill.android.shared.dto.Versionable;

public abstract class EntitySyncManager<T extends Versionable> {
	
	protected Map<CRUDOperationType, Map<Long, Integer>> deltaState;
	
	protected Long userId;
	
	public EntitySyncManager(Long userId, Map<CRUDOperationType, Map<Long, Integer>> deltaState){
		this.deltaState = deltaState;
		this.userId = userId;
	}
	
	
	protected abstract Cursor loadEntities(SQLiteDatabase db);
	
	protected abstract String getIDColumnName();
	
	protected abstract String getServerIdColumnName();
	
	protected abstract String getVersionColumnName();
	
	protected abstract String getUserIdColumnName();
	
	protected abstract String getTableName();
	
	protected abstract ContentValues convertToContValues(T serverDTO);
	
	private void stateToMaps(Cursor cursor, Map<Long, Long> idMap, Map<Long, Integer> versionMap){
		String idColName = getIDColumnName();
		String serverIdColName = getServerIdColumnName();
		String versionColName = getVersionColumnName();
		while(cursor.moveToNext()){
			Long id = cursor.getLong(cursor.getColumnIndex(idColName));
			Long serverId = cursor.getLong(cursor.getColumnIndex(serverIdColName));
			Integer version = cursor.getInt(cursor.getColumnIndex(versionColName));
			idMap.put(serverId, id);
			versionMap.put(serverId, version);
		}
	}
	
	public void computeServerFetchOrder(SQLiteDatabase db, List<Long> addList, Map<Long, Long> updateMap){
		Cursor cursor = loadEntities(db);
		Map<Long, Long> idMap = new HashMap<Long, Long>();
		Map<Long, Integer> versionMap = new HashMap<Long, Integer>();
		stateToMaps(cursor, idMap, versionMap);
		for(Map.Entry<Long, Integer> entry: deltaState.get(CRUDOperationType.UPDATE).entrySet()){
			Long serverId = entry.getKey();
			Integer version = entry.getValue();
			if(version > versionMap.get(serverId)) //check for stale data
				updateMap.put(serverId, idMap.get(serverId));
		}
		for(Map.Entry<Long, Integer> entry: deltaState.get(CRUDOperationType.CREATE).entrySet()){
			Long serverId = entry.getKey();
			Integer version = entry.getValue();
			if(!idMap.containsKey(serverId))//does not exist, add it
				addList.add(serverId);
			else{
				if(version > versionMap.get(serverId)) //exists, check for stale data
					updateMap.put(serverId, idMap.get(serverId));
			}
		}
		
	}
	
	public void applyServerChanges(Long userId, SQLiteDatabase db, List<T> serverDTOs, List<Long> addList, Map<Long, Long> updateMap){
		Map<Long, T> idMap = new HashMap<Long, T>();
		for(T dto: serverDTOs)
			idMap.put(dto.getId(), dto);
		for(Long addId: addList){
			T serverDTO = idMap.get(addId);
			ContentValues values = convertToContValues(serverDTO);
			values.put(getUserIdColumnName(), userId);
			values.put(getServerIdColumnName(), serverDTO.getId());
			values.put(getVersionColumnName(), serverDTO.getVersion());
			db.insert(getTableName(), null, values);
		}
		for(Long serverId: updateMap.keySet()){
			T serverDTO = idMap.get(serverId);
			ContentValues values = convertToContValues(serverDTO);
			values.put(getVersionColumnName(), serverDTO.getVersion());
			db.update(getTableName(), values, "_id = ?", new String[]{updateMap.get(serverId).toString()});
		}
	}
	
}
