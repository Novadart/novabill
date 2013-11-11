package com.novadart.novabill.android.authentication;

import java.net.URL;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import android.content.Context;
import com.novadart.novabill.android.R;
import com.novadart.novabill.android.authentication.HttpRequestTemplate.HttpRequestExecutor;

public class ServerAuthenticator {
	
	private Context context;
	
	public ServerAuthenticator(Context context){
		this.context = context;
	}
	
	private class AuthenticationHelper implements HttpRequestExecutor<AuthenticationResult> {

		@Override
		public String computePath(String restServicePath) {
			return restServicePath + context.getResources().getString(R.string.authenticate_relative_path);
		}

		@Override
		public AuthenticationResult execute(URL url, HttpEntity<?> requestEntity) {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			try {
				restTemplate.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
				return new Success(NovabillAccountAuthenticator.BASIC_TOKEN, Status.OK);
			} catch (RestClientException e) {
				return new Failure(Status.ERROR);
			}
		}

		@Override
		public Object getHttpRequestBody() {
			return null;
		}
		
	}
	
	public AuthenticationResult authenticate(String name, String password, String authTokenType) {
		HttpRequestTemplate template = new HttpRequestTemplate(context);
		return template.request(name, password, new AuthenticationHelper());
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
