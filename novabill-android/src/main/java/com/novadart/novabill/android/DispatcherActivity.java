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
		if(secCtxMng.isSignIn()){
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else if(secCtxMng.getNovabillAccounts().length == 0){
			final Activity ctx = this;
			AccountManager.get(this).addAccount(NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE,
					NovabillAccountAuthenticator.AUTH_TOKEN_TYPE, null, null, this, new AccountManagerCallback<Bundle>() {
						@Override
						public void run(AccountManagerFuture<Bundle> bundle) {
							Intent intent = new Intent(ctx, DispatcherActivity.class);
							startActivity(intent);
							ctx.finish();
						}
					}, null);
		} else if(secCtxMng.getNovabillAccounts().length == 1){
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.ARG_REDIRECT_TO_DISPATCHER_ACTIVITY, true);
			intent.putExtra(LoginActivity.ARG_NAME, secCtxMng.getNovabillAccounts()[0].name);
			startActivity(intent);
			finish();
		}
	}
	
	
	
}

