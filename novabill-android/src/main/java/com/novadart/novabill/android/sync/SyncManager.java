package com.novadart.novabill.android.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.novadart.novabill.android.R;
import com.novadart.novabill.android.authentication.HttpRequestTemplate;
import com.novadart.novabill.android.content.provider.DBSchema.SyncMarkTbl;
import com.novadart.novabill.android.content.provider.NovabillDBHelper;
import com.novadart.novabill.shared.client.data.SyncEntityType;
import com.novadart.novabill.shared.client.dto.SyncDeltaObjectsDTO;
import com.novadart.novabill.shared.client.dto.SyncDeltaStateDTO;

public class SyncManager {
	
	private Context context;

	public SyncManager(Context context) {
		this.context = context;
	}
	
	private SyncDeltaStateDTO getServerDeltaState(Account account, Long mark) {
		DeltaStateRequestHelper helper = new DeltaStateRequestHelper(context.getResources().getString(R.string.sync_relative_path) + "/state", mark);
		HttpRequestTemplate template = new HttpRequestTemplate(context);
		return template.request(account, helper);
	}
	
	private SyncDeltaObjectsDTO getServerDeltaObjects(Account account, List<Long> clientsAddIdsList, Map<Long, Long> clientsUpdateIdsMap){
		List<Long> clientIds = new ArrayList<Long>(clientsAddIdsList);
		clientIds.addAll(clientsUpdateIdsMap.keySet());
		DeltaStateObjectsRequestHelper helper = new DeltaStateObjectsRequestHelper(context.getResources().getString(R.string.sync_relative_path) + "/objects", clientIds);
		HttpRequestTemplate template = new HttpRequestTemplate(context);
		return template.request(account, helper);
	}

	
	private Long getMark(SQLiteDatabase db, Long userID){
		Cursor cursor = db.query(SyncMarkTbl.TABLE_NAME, null, SyncMarkTbl.USER_ID + "=?", new String[]{userID.toString()}, null, null, null);
		cursor.moveToFirst();
		return cursor.getLong(cursor.getColumnIndex(SyncMarkTbl.MARK));
	}
	
	private void updateMark(SQLiteDatabase db, Long mark, Long userID){
		ContentValues values = new ContentValues();
		values.put(SyncMarkTbl.MARK, mark);
		db.update(SyncMarkTbl.TABLE_NAME, values, SyncMarkTbl.USER_ID + "=?", new String[]{userID.toString()});
	}
	
	public void performSync(Account account){
		NovabillDBHelper dbHelper = NovabillDBHelper.getInstance(context);
		ClientSyncManager clientSyncMng = null;
		List<Long> clientsAddIdsList = new ArrayList<Long>();
		Map<Long, Long> clientsUpdateIdsMap = new HashMap<Long, Long>();
		Long userId = dbHelper.getUserId(account.name);
		//read db stage
		SQLiteDatabase readDb = dbHelper.getReadableDatabase();
		SyncDeltaStateDTO deltaState = getServerDeltaState(account, getMark(readDb, userId));
		if(deltaState.getMark() == null || deltaState.getMark() < 1) return;
		clientSyncMng = new ClientSyncManager(userId, deltaState.getDeltaState().get(SyncEntityType.CLIENT));
		clientSyncMng.computeServerFetchOrder(readDb, clientsAddIdsList, clientsUpdateIdsMap);
		SyncDeltaObjectsDTO serverDeltaObjects = getServerDeltaObjects(account, clientsAddIdsList, clientsUpdateIdsMap);
		//write db stage
		SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
		try{
			writeDb.beginTransaction();
			clientSyncMng.applyServerChanges(userId, writeDb, serverDeltaObjects.getClients(), clientsAddIdsList, clientsUpdateIdsMap);
			updateMark(writeDb, deltaState.getMark(), userId);
			writeDb.setTransactionSuccessful();
		} finally {
			writeDb.endTransaction();
		}
	}
	
	

}
