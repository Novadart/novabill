package com.novadart.novabill.android;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.novadart.novabill.android.authentication.LoginActivity;
import com.novadart.novabill.android.authentication.NovabillAccountAuthenticator;
import com.novadart.novabill.android.authentication.SecurityContextManager;

public class DispatcherActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SecurityContextManager secCtxMng = new SecurityContextManager(this);
		if(secCtxMng.isSignIn()){ //signed in
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else if(secCtxMng.getNovabillAccounts().length == 0){ //no accounts, add one
			final Activity ctx = this;
			Bundle options = new Bundle();
			options.putBoolean(LoginActivity.ARG_REDIRECT_TO_DISPATCHER_ACTIVITY, true);
			AccountManager.get(this).addAccount(NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE,
					NovabillAccountAuthenticator.AUTH_TOKEN_TYPE, null, options, this, new AccountManagerCallback<Bundle>() {
						@Override
						public void run(AccountManagerFuture<Bundle> bundle) {
							ctx.finish();
						}
					}, null);
		} else { // unsigned and at least one account, select or add one
			Intent intent = new Intent(this, SelectAccountActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	
	
}

