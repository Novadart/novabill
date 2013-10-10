package com.novadart.novabill.android.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

public class SecurityContextManager {
	
	private static Object lock = new Object();
	
	public static final String SECURITY_CONTEXT_PREFS = "SecurityContextPrefs";
	
	private static final String SIGNEDIN_ACCOUNT_NAME = "SignInName";
	
	private SharedPreferences securityContextPrefs;
	
	private AccountManager accountManager;
	
	public SecurityContextManager(Context context){
		securityContextPrefs = context.getSharedPreferences(SECURITY_CONTEXT_PREFS, Context.MODE_PRIVATE);
		accountManager = AccountManager.get(context);
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
	
	public void setSignInName(String name){
		synchronized(lock){
			SharedPreferences.Editor editor = securityContextPrefs.edit();
			editor.putString(SIGNEDIN_ACCOUNT_NAME, name);
			editor.commit();
		}
	}
	
	public void clearSignInName(){
		synchronized(lock){
			SharedPreferences.Editor editor = securityContextPrefs.edit();
			editor.remove(SIGNEDIN_ACCOUNT_NAME);
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
