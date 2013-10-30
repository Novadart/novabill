package com.novadart.novabill.android.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.novadart.novabill.android.DispatcherActivity;
import com.novadart.novabill.android.R;
import com.novadart.novabill.android.authentication.ServerAuthenticator.AuthenticationResult;
import com.novadart.novabill.android.content.provider.NovabillContract;
import com.novadart.novabill.android.content.provider.NovabillDBHelper;

public class LoginActivity extends AccountAuthenticatorActivity {
	
	public static final String ARG_ACCOUNT_TYPE = "accountType";
	
	public static final String ARG_AUTH_TYPE = "authType";
	
	public static final String ARG_REDIRECT_TO_DISPATCHER_ACTIVITY = "redirectToDispatcherActivity";
	
	public static final String ARG_IS_NAME_EDITABLE = "isNameEditable";
	
	public static final String ARG_NAME = "novabillUsername";
	
	public static final String PARAM_USER_PASS = "password";
	
	private ServerAuthenticator sServerAuthenticate;
	
	private AccountManager mAccountManager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.novadart.novabill.android.R.layout.activity_login);
        EditText email = (EditText)findViewById(R.id.email);
        if(getIntent().hasExtra(ARG_NAME)){
        	if(email.getText().toString().trim().isEmpty())
        		email.setText(getIntent().getStringExtra(ARG_NAME));
        }
        email.setEnabled(getIntent().getBooleanExtra(ARG_IS_NAME_EDITABLE, true));
        sServerAuthenticate = new ServerAuthenticator(this);
    }
	
	
	@Override
	protected void onStart() {
		super.onStart();
		if(mAccountManager == null)
			mAccountManager = AccountManager.get(this);
	}
	
	public void submit(View view) {
	    final String userName = ((TextView) findViewById(R.id.email)).getText().toString();
	    final String userPass = ((TextView) findViewById(R.id.password)).getText().toString();
	    new AsyncTask<Void, Void, Intent>() {
	        @Override
	        protected Intent doInBackground(Void... params) {
	        	AuthenticationResult result = sServerAuthenticate.authenticate(userName, userPass, NovabillAccountAuthenticator.AUTH_TOKEN_TYPE); 
	        	if(result.isSuccessful()){
		            String authtoken = result.getToken(); 
		            final Intent res = new Intent();
		            res.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
		            res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE);
		            res.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
		            res.putExtra(PARAM_USER_PASS, userPass);
		            return res;
	        	}else{
	        		return null;
	        	}
	        		
	        }
	        @Override
	        protected void onPostExecute(Intent intent) {
	            finishLogin(intent);
	        }
	    }.execute();
	}
	
	private void alertOnBadLogin(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Error!")
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {}
			});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void finishLogin(Intent intent) {
		if(intent == null){
			alertOnBadLogin();
			return;
		}
	    String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	    String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
	    final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
	    SecurityContextManager secCtxMng = new SecurityContextManager(this);
	    if (!secCtxMng.existsNovabillAccount(accountName)) {
	        String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
	        String authtokenType = NovabillAccountAuthenticator.AUTH_TOKEN_TYPE;
	        // Creating the account on the device and setting the auth token we got
	        // (Not setting the auth token will cause another call to the server to authenticate the user)
	        mAccountManager.addAccountExplicitly(account, accountPassword, null);
	        mAccountManager.setAuthToken(account, authtokenType, authtoken);
	        NovabillDBHelper.getInstance(this).addUser(accountName);
	        ContentResolver.setIsSyncable(account, NovabillContract.AUTHORITY, 1);
	    } else {
	        mAccountManager.setPassword(account, accountPassword);
	    }
	    secCtxMng.signIn(accountName);
	    setAccountAuthenticatorResult(intent.getExtras());
	    setResult(RESULT_OK, intent);
	    if(getIntent().getBooleanExtra(ARG_REDIRECT_TO_DISPATCHER_ACTIVITY, false)){
	    	Intent dispatcher = new Intent(this, DispatcherActivity.class);
	    	startActivity(dispatcher);
	    }
	    finish();
	}

}
