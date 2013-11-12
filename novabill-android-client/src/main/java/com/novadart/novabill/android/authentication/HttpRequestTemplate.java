package com.novadart.novabill.android.authentication;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.novadart.novabill.android.R;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class HttpRequestTemplate {
	
	private Context context;

	public HttpRequestTemplate(Context context) {
		this.context = context;
	}
	
	public static interface HttpRequestExecutor<T> {
		public String computePath(String restServicePath);
		public T execute(URL url, HttpEntity<?> requestEntity);
		public Object getHttpRequestBody();
	}
	
	public <T> T request(String name, String password, HttpRequestExecutor<T> executor){
		String host = context.getResources().getString(R.string.novabill_host);
		String protocol = context.getResources().getString(R.string.protocol);
		int port = Integer.parseInt(context.getResources().getString(R.string.port));
		String path = executor.computePath(context.getResources().getString(R.string.rest_services_path));
		URL url = null;
		try {
			url = new URL(protocol, host, port, path);
		}catch (MalformedURLException e1) {}
		HttpAuthentication authHeader = new HttpBasicAuthentication(name, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(executor.getHttpRequestBody(), requestHeaders);
		return executor.execute(url, requestEntity);
	}
	
	public <T> T request(Account account , HttpRequestExecutor<T> executor){
		AccountManager accountManager = AccountManager.get(context);
		return request(account.name, accountManager.getPassword(account), executor);
	}

}
