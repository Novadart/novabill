package com.novadart.novabill.android.authentication;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.novadart.novabill.android.R;

import android.content.Context;

public class ServerAuthenticator {
	
	private Context context;
	
	public ServerAuthenticator(Context context){
		this.context = context;
	}
	
	public AuthenticationResult authenticate(String name, String password, String authTokenType) {
		String host = context.getResources().getString(R.string.novabill_host);
		String protocol = context.getResources().getString(R.string.protocol);
		int port = Integer.parseInt(context.getResources().getString(R.string.port));
		String path = context.getResources().getString(R.string.rest_services_path) + context.getResources().getString(R.string.authenticate_relative_path);
		URL url = null;
		try {
			url = new URL(protocol, host, port, path);
		} catch (MalformedURLException e1) {}
		HttpAuthentication authHeader = new HttpBasicAuthentication(name, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		try {
			restTemplate.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
			return new Success(NovabillAccountAuthenticator.BASIC_TOKEN, Status.OK);
		} catch (RestClientException e) {
			return new Failure(Status.ERROR);
		}
	}

	public enum Status {OK, ERROR}
	
	public abstract class AuthenticationResult {
		
		protected Status status;
		
		protected AuthenticationResult(Status status){
			this.status = status;
		}
		
		public abstract boolean isSuccessful();
		public abstract String getToken();
		public Status getStatus(){
			return status;
		}
	}
	
	public class Success extends AuthenticationResult {
		
		private String token;
		
		private Success(String token, Status status) {
			super(status);
			this.token = token;
		}

		@Override
		public boolean isSuccessful() {
			return true;
		}

		@Override
		public String getToken() {
			return token;
		}
	}
	
	public class Failure extends AuthenticationResult{

		private Failure(Status status){
			super(status);
		}
		
		@Override
		public boolean isSuccessful() {
			return false;
		}

		@Override
		public String getToken() {
			throw new UnsupportedOperationException();
		}
		
	}

}
