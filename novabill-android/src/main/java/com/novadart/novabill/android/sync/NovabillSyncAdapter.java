package com.novadart.novabill.android.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.novadart.novabill.android.authentication.NovabillAccountAuthenticator;
import com.novadart.novabill.android.content.provider.NovabillContract;

public class NovabillSyncAdapter extends AbstractThreadedSyncAdapter {

	private AccountManager accountManager;
	
	public static void setAccountSyncSettings(Account account){
		ContentResolver.setIsSyncable(account, NovabillContract.AUTHORITY, NovabillContract.DEFAULT_SYNC_STATUS);
        ContentResolver.setSyncAutomatically(account, NovabillContract.AUTHORITY, true);
	}
	
	public NovabillSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		accountManager = AccountManager.get(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient provider, SyncResult syncResult) {
		
		try {
			String authToken = accountManager.blockingGetAuthToken(account, NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE, true);
			Log.i("Sync adapter", "Syncing...");			
		} catch (Exception e) {}
			

	}

}
