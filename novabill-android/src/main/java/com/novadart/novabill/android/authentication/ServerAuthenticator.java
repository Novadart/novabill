package com.novadart.novabill.android.authentication;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ServerAuthenticator {
	
	public static final String BASIC_TOKEN = "basic token";
	
	public AuthenticationResult authenticate(String name, String password, String authTokenType){
		String url = "http://192.168.0.108:8080/novabill-server/rest/1/authenticate";
		HttpAuthentication authHeader = new HttpBasicAuthentication(name, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		try {
			restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			return new Success(BASIC_TOKEN, Status.OK);
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
