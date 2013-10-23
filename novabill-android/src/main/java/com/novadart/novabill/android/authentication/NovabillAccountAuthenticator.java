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
	
	public static final String NOVABILL_ACCOUNT_TYPE = "com.novadart.novabill";
	
	public static final String AUTH_TOKEN_TYPE = "basic";
	
	public static final String BASIC_TOKEN = "9a7099dc836218d205a91dcb4e9578d5eab3b73b77eaaff1eee888630a52193a";

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
	    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
	    intent.putExtra(LoginActivity.ARG_REDIRECT_TO_DISPATCHER_ACTIVITY, options.getBoolean(LoginActivity.ARG_REDIRECT_TO_DISPATCHER_ACTIVITY, false));
	    intent.putExtra(LoginActivity.ARG_IS_NAME_EDITABLE, options.getBoolean(LoginActivity.ARG_IS_NAME_EDITABLE, true));
	    if(options.containsKey(LoginActivity.ARG_NAME))
	    	intent.putExtra(LoginActivity.ARG_NAME, options.getString(LoginActivity.ARG_NAME));
	    final Bundle bundle = new Bundle();
	    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
	    return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		if(!authTokenType.equals(AUTH_TOKEN_TYPE)){
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }
		final AccountManager am = AccountManager.get(mContext);
        final String password = am.getPassword(account);
        if (password != null) {
            final boolean verified = new ServerAuthenticator(mContext).authenticate(account.name, password, authTokenType).isSuccessful();
            if (verified) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, NOVABILL_ACCOUNT_TYPE);
                result.putString(AccountManager.KEY_AUTHTOKEN, BASIC_TOKEN);
                return result;
            }
        }
        final Intent intent = new Intent(this.mContext, LoginActivity.class);
	    intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, NOVABILL_ACCOUNT_TYPE);
	    intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
	    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
	    final Bundle bundle = new Bundle();
	    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
	    return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

}
