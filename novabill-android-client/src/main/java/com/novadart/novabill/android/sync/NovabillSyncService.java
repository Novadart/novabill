package com.novadart.novabill.android.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NovabillSyncService extends Service {

	private static final Object lock = new Object();
	
	private static NovabillSyncAdapter novabillSyncAdapter = null;
	
	@Override
	public void onCreate() {
		synchronized (lock) {
			if(novabillSyncAdapter == null)
				novabillSyncAdapter = new NovabillSyncAdapter(getApplicationContext(), true);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return novabillSyncAdapter.getSyncAdapterBinder();
	}

}
