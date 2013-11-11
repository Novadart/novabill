package com.novadart.novabill.android.authentication;

import com.novadart.novabill.android.content.provider.NovabillDBHelper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

public class SecurityContextManager {
	
	private static Object lock = new Object();
	
	public static final String SECURITY_CONTEXT_PREFS = "SecurityContextPrefs";
	
	private static final String SIGNEDIN_ACCOUNT_NAME = "SignInName";
	
	private static final String SIGNEDIN_ACCOUNT_ID = "SignInId";
	
	private SharedPreferences securityContextPrefs;
	
	private AccountManager accountManager;
	
	private Context context;
	
	public SecurityContextManager(Context context){
		securityContextPrefs = context.getSharedPreferences(SECURITY_CONTEXT_PREFS, Context.MODE_PRIVATE);
		accountManager = AccountManager.get(context);
		this.context = context;
	}
	
	public boolean isSignIn(){
		synchronized(lock){
			return securityContextPrefs.contains(SIGNEDIN_ACCOUNT_NAME);
		}
	}
	
	public String getSignInName(){
		synchronized(lock){
			if(!isSignIn())
				throw new IllegalStateException();
			return securityContextPrefs.getString(SIGNEDIN_ACCOUNT_NAME, null);
		}
	}
	
	public Long getSignInId(){
		synchronized(lock){
			if(!isSignIn())
				throw new IllegalStateException();
			return  securityContextPrefs.getLong(SIGNEDIN_ACCOUNT_ID, -1);
		}
	}
	
	public void signIn(String name){
		synchronized(lock){
			SharedPreferences.Editor editor = securityContextPrefs.edit();
			editor.putString(SIGNEDIN_ACCOUNT_NAME, name);
			editor.putLong(SIGNEDIN_ACCOUNT_ID, NovabillDBHelper.getInstance(context).getUserId(name));
			editor.commit();
		}
	}
	
	public void signOut(){
		synchronized(lock){
			SharedPreferences.Editor editor = securityContextPrefs.edit();
			editor.remove(SIGNEDIN_ACCOUNT_NAME);
			editor.remove(SIGNEDIN_ACCOUNT_ID);
			editor.commit();
		}
	}
	
	public Account[] getNovabillAccounts(){
		return accountManager.getAccountsByType(NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE);
	}
	
	public boolean existsNovabillAccount(String name){
		for(Account account: accountManager.getAccountsByType(NovabillAccountAuthenticator.NOVABILL_ACCOUNT_TYPE))
			if(account.name.equals(name))
				return true;
		return false;
	}
	
}
