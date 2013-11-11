package com.novadart.novabill.android.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.novadart.novabill.android.authentication.NovabillAccountAuthenticator;
import com.novadart.novabill.android.content.provider.DBSchema.SyncMarkTbl;
import com.novadart.novabill.android.content.provider.NovabillContract;
import com.novadart.novabill.android.content.provider.NovabillDBHelper;

public class NovabillSyncAdapter extends AbstractThreadedSyncAdapter {

	private AccountManager accountManager;
	
	private Context context;
	
	public static void setAccountSyncSettings(Context context, Account account, Long userID){
		ContentResolver.setIsSyncable(account, NovabillContract.AUTHORITY, NovabillContract.DEFAULT_SYNC_STATUS);
        ContentResolver.setSyncAutomatically(account, NovabillContract.AUTHORITY, true);
        NovabillDBHelper dbHelper = NovabillDBHelper.getInstance(context);
        ContentValues values = new ContentValues();
        values.put(SyncMarkTbl.USER_ID, userID);
        values.put(SyncMarkTbl.MARK, SyncMarkTbl.INIT_MARK);
        dbHelper.getWritableDatabase().insert(SyncMarkTbl.TABLE_NAME, null, values);
        
	}
	
	public NovabillSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		this.context = context;
		accountManager = AccountManager.get(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient provider, SyncResult syncResult) {
		
		try {
			accountManager.blockingGetAuthToken(account, NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE, true);
			SyncManager syncManager = new SyncManager(context);
			syncManager.performSync(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
			

	}

}
