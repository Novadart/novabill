package com.novadart.novabill.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartManagerServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startManagerService = new Intent(context, ManagerService.class);
		context.startService(startManagerService);
	}

}
