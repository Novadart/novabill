package com.novadart.novabill.android.content.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.novadart.novabill.android.content.provider.DBSchema.ClientTbl;
import com.novadart.novabill.android.content.provider.NovabillContract.Clients;

public class NovabillContentProvider extends ContentProvider {
	
	
	private static final int CLIENT_LIST = 1; 
	private static final int CLIENT_ID = 2;
	private static final int ITEM_LIST = 3;
	private static final int ITEM_ID = 4;
	private static final UriMatcher URI_MATCHER;
	
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(NovabillContract.AUTHORITY, "users/#/clients", CLIENT_LIST);
		URI_MATCHER.addURI(NovabillContract.AUTHORITY, "users/#/clients/#", CLIENT_ID);
		URI_MATCHER.addURI(NovabillContract.AUTHORITY, "items", ITEM_LIST);
		URI_MATCHER.addURI(NovabillContract.AUTHORITY, "items/#", ITEM_ID);
		
	}
	
	private NovabillDBHelper dbHelper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Long userID;
		int delCount = 0;
		switch (URI_MATCHER.match(uri)) {
		case CLIENT_LIST:
			userID = Long.parseLong(uri.getPathSegments().get(1));
			delCount = dbHelper.deleteClients(userID);
			break;
		case CLIENT_ID:
			userID = Long.parseLong(uri.getPathSegments().get(1));
			Long clientID = Long.parseLong(uri.getLastPathSegment());
			delCount = dbHelper.deleteClient(userID, clientID);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return delCount;
	}

	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
		case CLIENT_LIST:
			return Clients.CONTENT_TYPE;
		case CLIENT_ID:
			return Clients.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	private Uri constructUriAndRegisterChange(Long id, Uri uri){
		if(id > 0){
			Uri itemUri = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(itemUri, null);
			return itemUri;
		}
		throw new SQLException("Problem while inserting into uri: " + uri);
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (URI_MATCHER.match(uri)){
		case CLIENT_LIST:
			Long userID = Long.parseLong(uri.getPathSegments().get(1));
			values.put(ClientTbl.USER_ID, userID);
			Long id = db.insert(ClientTbl.TABLE_NAME, null, values);
			return constructUriAndRegisterChange(id, uri);
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public boolean onCreate() {
		dbHelper = NovabillDBHelper.getInstance(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor;
		Long userID;
		switch (URI_MATCHER.match(uri)) {
		case CLIENT_LIST:
			userID = Long.parseLong(uri.getPathSegments().get(1));
			cursor = dbHelper.getClients(userID, projection, selection, selectionArgs, sortOrder); 
			break;
		case CLIENT_ID:
			userID = Long.parseLong(uri.getPathSegments().get(1));
			Long clientID = Long.parseLong(uri.getLastPathSegment());
			cursor = dbHelper.getClient(userID, clientID, projection, selection, selectionArgs, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int updateCount = 0;
		switch (URI_MATCHER.match(uri)) {
		case CLIENT_LIST:
			
			break;
		case CLIENT_ID:
			
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return updateCount;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int insertCount = 0;
		switch (URI_MATCHER.match(uri)) {
		case CLIENT_LIST:
			Long userID = Long.parseLong(uri.getPathSegments().get(1));
			db.beginTransaction();
			try{
				for(ContentValues cv: values){
					cv.put(ClientTbl.USER_ID, userID);
					if(db.insert(ClientTbl.TABLE_NAME, null, cv) <= 0)
						throw new SQLException("Error to add: " + uri); 
					insertCount++;
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return insertCount;
	}
	
	

}
