package com.novadart.novabill.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ManagerService extends Service {

	public static final String MANAGER_SERVICE = "MANAGER_SERVICE";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(MANAGER_SERVICE, "Starting manager service!");
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
