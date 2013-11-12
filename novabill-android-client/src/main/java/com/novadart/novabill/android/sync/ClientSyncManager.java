package com.novadart.novabill.android.sync;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.novadart.novabill.android.content.provider.DBSchema.ClientTbl;
import com.novadart.novabill.android.shared.data.CRUDOperationType;
import com.novadart.novabill.android.shared.dto.ClientDTO;

public class ClientSyncManager extends EntitySyncManager<ClientDTO> {

	public ClientSyncManager(Long userId, Map<CRUDOperationType, Map<Long, Integer>> deltaState) {
		super(userId, deltaState);
	}

	@Override
	protected Cursor loadEntities(SQLiteDatabase db) {
		return db.query(ClientTbl.TABLE_NAME, null, ClientTbl.USER_ID + "=?", new String[]{userId.toString()}, null, null, null);
	}

	@Override
	protected String getIDColumnName() {
		return ClientTbl._ID;
	}

	@Override
	protected String getServerIdColumnName() {
		return ClientTbl.SERVER_ID;
	}

	@Override
	protected String getVersionColumnName() {
		return ClientTbl.VERSION;
	}
	
	@Override
	protected String getUserIdColumnName() {
		return ClientTbl.USER_ID;
	}
	
	@Override
	protected String getTableName() {
		return ClientTbl.TABLE_NAME;
	}

	@Override
	protected ContentValues convertToContValues(ClientDTO serverDTO) {
		ContentValues values = new ContentValues();
		if(!TextUtils.isEmpty(serverDTO.getName()))
			values.put(ClientTbl.NAME, serverDTO.getName());
		if(!TextUtils.isEmpty(serverDTO.getAddress()))
			values.put(ClientTbl.ADDRESS, serverDTO.getAddress());
		if(!TextUtils.isEmpty(serverDTO.getCity()))
			values.put(ClientTbl.CITY, serverDTO.getCity());
		if(!TextUtils.isEmpty(serverDTO.getProvince()))
			values.put(ClientTbl.PROVINCE, serverDTO.getProvince());
		if(!TextUtils.isEmpty(serverDTO.getCountry()))
			values.put(ClientTbl.COUNTRY, serverDTO.getCountry());
		if(!TextUtils.isEmpty(serverDTO.getPostcode()))
			values.put(ClientTbl.POSTCODE, serverDTO.getPostcode());
		if(!TextUtils.isEmpty(serverDTO.getPhone()))
			values.put(ClientTbl.PHONE, serverDTO.getPhone());
		if(!TextUtils.isEmpty(serverDTO.getMobile()))
			values.put(ClientTbl.MOBILE, serverDTO.getMobile());
		if(!TextUtils.isEmpty(serverDTO.getFax()))
			values.put(ClientTbl.FAX, serverDTO.getFax());
		if(!TextUtils.isEmpty(serverDTO.getEmail()))
			values.put(ClientTbl.EMAIL, serverDTO.getEmail());
		if(!TextUtils.isEmpty(serverDTO.getWeb()))
			values.put(ClientTbl.WEB, serverDTO.getWeb());
		if(!TextUtils.isEmpty(serverDTO.getVatID()))
			values.put(ClientTbl.VAT_ID, serverDTO.getVatID());
		if(!TextUtils.isEmpty(serverDTO.getSsn()))
			values.put(ClientTbl.SSN, serverDTO.getSsn());
		return values;
	}

}
