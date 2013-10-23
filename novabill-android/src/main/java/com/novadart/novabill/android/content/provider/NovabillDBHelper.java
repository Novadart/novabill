package com.novadart.novabill.android.content.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.novadart.novabill.android.R;
import com.novadart.novabill.android.authentication.SecurityContextManager;

public class NovabillDBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "novabill";
	
    private static final int DATABASE_VERSION = 1;
    
    private static final String ON_UPDATE_ROOT_ELEMENT = "createStatements";
    
    private static NovabillDBHelper instance = null;
    
    private Context context;
    
    private String getStatements(XmlResourceParser parser, String tagName){
		try {
			StringBuilder builder = new StringBuilder();
			boolean appendStatments = false;
			int eventType;
			do {
				eventType = parser.next();
				if(eventType == XmlResourceParser.START_TAG && tagName.equals(parser.getName()))
					appendStatments = true;
				if(eventType == XmlResourceParser.END_TAG && tagName.equals(parser.getName()))
					appendStatments = false;
				if(eventType == XmlResourceParser.TEXT && appendStatments)
					builder.append(parser.getText());
					
			} while (eventType != XmlResourceParser.END_DOCUMENT);
			return builder.toString();
		} catch (Exception e) {
			return null;
		}
    }

    private void execMultipleSQL(SQLiteDatabase db, String[] sql){
    	for(String stmt: sql)
    		if(stmt.trim().length() > 0)
    			db.execSQL(stmt);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		XmlResourceParser parser = context.getResources().getXml(R.xml.novabilldb_oncreate);
		String[] stmts = getStatements(parser, ON_UPDATE_ROOT_ELEMENT).trim().split("\n");
		parser.close();
		db.beginTransaction();
		try {
			execMultipleSQL(db, stmts);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	private NovabillDBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	
	public static NovabillDBHelper getInstance(Context context){
		if(instance == null)
			instance = new NovabillDBHelper(context);
		return instance;
	}
	
	public Long addUser(String name){
		if(TextUtils.isEmpty(name.trim()))
			throw new IllegalArgumentException("Empty name!");
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBSchema.UserTbl.EMAIL, name);
		return db.insertOrThrow(DBSchema.UserTbl.TABLE_NAME, null, values);
	}
	
	public Long getUserId(String name){
		if(TextUtils.isEmpty(name.trim()))
			throw new IllegalArgumentException("Empty name!");
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(DBSchema.UserTbl.TABLE_NAME, new String[]{DBSchema.UserTbl._ID}, DBSchema.UserTbl.EMAIL + "=?", new String[]{name}, null, null, null);
		if(cursor.getCount() > 1)
			throw new IllegalStateException("More than one user with name " + name);
		if(!cursor.moveToFirst())
			throw new IllegalArgumentException("No such user");
		int columnIndex = cursor.getColumnIndex(DBSchema.UserTbl._ID);
		return cursor.getLong(columnIndex);
	}
	
	public Cursor getClients(String sortOrder){
		Long userID = new SecurityContextManager(context).getSignInId();
		SQLiteDatabase db = getReadableDatabase();
		return db.query(DBSchema.ClientTbl.TABLE_NAME, null, DBSchema.ClientTbl.USER_ID + "=?", new String[]{Long.toString(userID)}, null, null,
				TextUtils.isEmpty(sortOrder)? null: sortOrder);
	}
	
	public Cursor getClient(Long clientID){
		Long userID = new SecurityContextManager(context).getSignInId();
		SQLiteDatabase db = getReadableDatabase();
		return db.query(DBSchema.ClientTbl.TABLE_NAME, null, String.format("%s=? and %s=?", DBSchema.ClientTbl.USER_ID, DBSchema.ClientTbl._ID),
				new String[]{Long.toString(userID), Long.toString(clientID)}, null, null, null);
	}
	
}
