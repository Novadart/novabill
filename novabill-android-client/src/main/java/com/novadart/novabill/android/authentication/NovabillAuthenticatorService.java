package com.novadart.novabill.android.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NovabillAuthenticatorService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		NovabillAccountAuthenticator authenticator = new NovabillAccountAuthenticator(this);
		return authenticator.getIBinder();
	}
	
}
