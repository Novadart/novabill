package com.novadart.novabill.android.content.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.novadart.novabill.android.R;
import com.novadart.novabill.android.content.provider.DBSchema.ClientTbl;
import com.novadart.novabill.android.content.provider.DBSchema.UserTbl;
import com.novadart.novabill.android.content.provider.NovabillContract.Clients;

public class NovabillDBHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "novabill";
	
    public static final int DATABASE_VERSION = 1;
    
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
		if(TextUtils.isEmpty(name))
			throw new IllegalArgumentException("Empty name!");
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserTbl.EMAIL, name);
		return db.insertOrThrow(UserTbl.TABLE_NAME, null, values);
	}
	
	public Long getUserId(String name){
		if(TextUtils.isEmpty(name))
			throw new IllegalArgumentException("Empty name!");
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(UserTbl.TABLE_NAME, new String[]{UserTbl._ID}, UserTbl.EMAIL + "=?", new String[]{name}, null, null, null);
		if(cursor.getCount() > 1)
			throw new IllegalStateException("More than one user with name " + name);
		if(!cursor.moveToFirst())
			throw new IllegalArgumentException("No such user");
		int columnIndex = cursor.getColumnIndex(UserTbl._ID);
		return cursor.getLong(columnIndex);
	}
	
	private String extendSelectionWithEquality(String selection, String column){
		String eqClause = column + "=?"; 
		if(TextUtils.isEmpty(selection))
			return eqClause;
		else
			return selection + " and " + eqClause;
	}
	
	private String[] extendSelectionArgs(String[] selectionArgs, String value){
		if(null == selectionArgs)
			return new String[]{value};
		else{
			String[] extSelectionArgs = new String[selectionArgs.length + 1];
			System.arraycopy(selectionArgs, 0, extSelectionArgs, 0, selectionArgs.length);
			extSelectionArgs[extSelectionArgs.length - 1] = value;
			return extSelectionArgs;
		}
	}
	
	public Cursor getClients(Long userID, String[] projection, String selection, String[] selectionArgs, String sortOrder){
		SQLiteDatabase db = getReadableDatabase();
		return db.query(ClientTbl.TABLE_NAME,
				projection, //Columns
				extendSelectionWithEquality(selection, ClientTbl.USER_ID), //selection 
				extendSelectionArgs(selectionArgs, Long.toString(userID)), //selection args
				null,
				null,
				TextUtils.isEmpty(sortOrder)? Clients.SORT_ORDER_DEFAULT: sortOrder); //sort order
	}
	
	public Cursor getClient(Long userID, Long clientID, String[] projection, String selection, String[] selectionArgs, String sortOrder){
		SQLiteDatabase db = getReadableDatabase();
		return db.query(ClientTbl.TABLE_NAME,
				projection, //Columns
				extendSelectionWithEquality(extendSelectionWithEquality(selection, ClientTbl.USER_ID), ClientTbl._ID), //selection
				extendSelectionArgs(extendSelectionArgs(selectionArgs, Long.toString(userID)), Long.toString(clientID)), //selection args
				null,
				null,
				TextUtils.isEmpty(sortOrder)? Clients.SORT_ORDER_DEFAULT: sortOrder); //sort order
	}
	
	public int deleteClients(Long userID){
		SQLiteDatabase db = getWritableDatabase();
		return db.delete(ClientTbl.TABLE_NAME, ClientTbl.USER_ID + "=?", new String[]{userID.toString()});
	}
	
	public int deleteClient(Long userID, Long clientID){
		SQLiteDatabase db = getWritableDatabase();
		return db.delete(ClientTbl.TABLE_NAME, String.format("%s=? and %s=?", ClientTbl.USER_ID, ClientTbl._ID), new String[]{userID.toString(), clientID.toString()});
	}
	
}
