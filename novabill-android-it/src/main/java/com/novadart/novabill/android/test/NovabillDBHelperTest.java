package com.novadart.novabill.android.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;

import com.novadart.novabill.android.content.provider.DBSchema;
import com.novadart.novabill.android.content.provider.NovabillContentProvider;
import com.novadart.novabill.android.content.provider.NovabillContract;
import com.novadart.novabill.android.content.provider.NovabillDBHelper;

public class NovabillDBHelperTest extends ProviderTestCase2<NovabillContentProvider> {

	private NovabillDBHelper dbHelper;
	

	public NovabillDBHelperTest() {
		super(NovabillContentProvider.class, NovabillContract.AUTHORITY);
	}
	
	public NovabillDBHelperTest(Class<NovabillContentProvider> providerClass, String providerAuthority) {
		super(providerClass, providerAuthority);
	}




	private boolean checkIfTableExists(SQLiteDatabase db, String tblName){
		Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name =?", new String[]{tblName});
		boolean result = cursor != null && cursor.getCount() > 0;
		cursor.close();
		return result;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
		dbHelper = NovabillDBHelper.getInstance(context);
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testOnCreate() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertTrue(checkIfTableExists(db, DBSchema.UserTbl.TABLE_NAME));
		assertTrue(checkIfTableExists(db, DBSchema.ClientTbl.TABLE_NAME));
	}

}
