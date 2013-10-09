package com.novadart.novabill.android.authentication;


import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NovabillAccountAuthenticator extends AbstractAccountAuthenticator {
	
	public static final String ACCOUNT_TYPE = "com.novadart.novabill";
	
	public static final String AUTH_TOKEN_TYPE = "basic";

	private Context mContext;
	
	public NovabillAccountAuthenticator(Context context) {
		super(context);
		mContext = context;
	}
	

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		final Intent intent = new Intent(this.mContext, LoginActivity.class);
	    intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
	    intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
	    intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
	    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
	    final Bundle bundle = new Bundle();
	    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
	    return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		return null;
	}

}
