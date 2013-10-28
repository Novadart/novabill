package com.novadart.novabill.android.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.novadart.novabill.android.content.provider.DBSchema;
import com.novadart.novabill.android.content.provider.DBSchema.ClientTbl;
import com.novadart.novabill.android.content.provider.NovabillContentProvider;
import com.novadart.novabill.android.content.provider.NovabillContract;
import com.novadart.novabill.android.content.provider.NovabillDBHelper;
import com.novadart.novabill.android.content.provider.UriUtils;

public class NovabillDBHelperTest extends ProviderTestCase2<NovabillContentProvider> {

	private NovabillDBHelper dbHelper;
	
	private static MockContentResolver resolve;
	

	public NovabillDBHelperTest() {
		super(NovabillContentProvider.class, NovabillContract.AUTHORITY);
	}
	
	public NovabillDBHelperTest(Class<NovabillContentProvider> providerClass, String providerAuthority) {
		super(providerClass, providerAuthority);
	}

	private void clearDB(SQLiteDatabase db){
		db.delete(DBSchema.ClientTbl.TABLE_NAME, null, null);
		db.delete(DBSchema.UserTbl.TABLE_NAME, null, null);
	}


	private boolean checkIfTableExists(SQLiteDatabase db, String tblName){
		Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name =?", new String[]{tblName});
		boolean result = cursor != null && cursor.getCount() > 0;
		cursor.close();
		return result;
	}
	
	private boolean checkIfColumnsExist(SQLiteDatabase db, String tblName, String[] columns){
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from " + tblName, null);
			for(String column: columns)
				if(cursor.getColumnIndex(column) == -1)
					return false;
			return true;
		} finally {
			cursor.close();
		}
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dbHelper = NovabillDBHelper.getInstance(getContext());
		resolve = getMockContentResolver();
		
	}

	@Override
	protected void tearDown() throws Exception {
		clearDB(dbHelper.getWritableDatabase());
		super.tearDown();
	}


	public void testOnCreate() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertTrue(checkIfTableExists(db, DBSchema.UserTbl.TABLE_NAME));
		assertTrue(checkIfColumnsExist(db, DBSchema.UserTbl.TABLE_NAME, new String[]{DBSchema.UserTbl.EMAIL}));
		assertTrue(checkIfTableExists(db, DBSchema.ClientTbl.TABLE_NAME));
		assertTrue(checkIfColumnsExist(db, ClientTbl.TABLE_NAME, new String[]{ClientTbl.ADDRESS, ClientTbl.CITY, ClientTbl.COUNTRY,
				ClientTbl.EMAIL, ClientTbl.FAX, ClientTbl.MOBILE, ClientTbl.NAME, ClientTbl.PHONE, ClientTbl.POSTCODE,
				ClientTbl.PROVINCE, ClientTbl.SSN, ClientTbl.USER_ID, ClientTbl.VAT_ID, ClientTbl.VERSION, ClientTbl.WEB}));
	}
	
	public void testAddGetUser() {
		String username = "foo@bar";
		Long id = dbHelper.addUser(username);
		assertEquals(1l, DatabaseUtils.queryNumEntries(dbHelper.getReadableDatabase(), DBSchema.UserTbl.TABLE_NAME));
		assertEquals(id.longValue(), dbHelper.getUserId(username).longValue());
	}

	public void testAddUserEmpty() {
		try{
			dbHelper.addUser(null);
			fail();
		} catch(IllegalArgumentException e){}
	}
	
	public void testGetUserEmpty() {
		try{
			dbHelper.getUserId(null);
			fail();
		} catch(IllegalArgumentException e){}
	}
	
	public void testGetUserNotExists() {
		try{
			dbHelper.getUserId("foo@bar");
			fail();
		} catch(IllegalArgumentException e){}
	}
	
	public void testAddUserTwice() {
		try {
			dbHelper.addUser("foo@bar");
			dbHelper.addUser("foo@bar");
		} catch (SQLiteConstraintException e) {}
	}
	
	public void testAddGetClient() {
		Long userID = dbHelper.addUser("foo@bar"); 
		ContentValues client = new ContentValues();
		client.put(ClientTbl.NAME, "John Doe");
		Uri clientsUri = UriUtils.clientsContentUriBuilder(userID).build();
		Uri uri = resolve.insert(clientsUri, client);
		Cursor cursor =  resolve.query(clientsUri, null, null, null, null);
		assertEquals(1, cursor.getCount());
		cursor = resolve.query(uri, null, null, null, null);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		assertEquals("John Doe", cursor.getString(cursor.getColumnIndex(ClientTbl.NAME)));
		assertEquals(userID.longValue(), cursor.getLong(cursor.getColumnIndex(ClientTbl.USER_ID)));
	}
	
	public void testAddDeleteClients(){
		Long userID = dbHelper.addUser("foo@bar");
		//insert first client
		ContentValues client = new ContentValues();
		client.put(ClientTbl.NAME, "John Doe");
		Uri clientsUri = UriUtils.clientsContentUriBuilder(userID).build();
		resolve.insert(clientsUri, client);
		//insert second client
		client = new ContentValues();
		client.put(ClientTbl.NAME, "Jane Doe");
		resolve.insert(clientsUri, client);
		assertEquals(2, dbHelper.getClients(userID, null, null, null, null).getCount());
		assertEquals(2, resolve.delete(UriUtils.clientsContentUriBuilder(userID).build(), null, null));
		
	}
	
	public void testAddDeleteClient(){
		Long userID = dbHelper.addUser("foo@bar");
		ContentValues client = new ContentValues();
		client.put(ClientTbl.NAME, "John Doe");
		Uri clientsUri = UriUtils.clientsContentUriBuilder(userID).build();
		Uri clientUri = resolve.insert(clientsUri, client);
		assertEquals(1, dbHelper.getClient(userID, Long.parseLong(clientUri.getLastPathSegment()), null, null, null, null).getCount());
		assertEquals(1, resolve.delete(clientUri, null, null));
	}
	
	public void testBulkInsertClients(){
		Long userID = dbHelper.addUser("foo@bar");
		Uri clientsUri = UriUtils.clientsContentUriBuilder(userID).build();
		ContentValues[] values = new ContentValues[2];
		ContentValues client = new ContentValues();
		client.put(ClientTbl.NAME, "John Doe");
		values[0] = client;
		client = new ContentValues();
		client.put(ClientTbl.NAME, "Jane Doe");
		values[1] = client;
		resolve.bulkInsert(clientsUri, values);
		assertEquals(2, resolve.query(clientsUri, null, null, null, null).getCount());
	}
	
}
