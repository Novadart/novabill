package com.novadart.novabill.android.sync;

import java.net.URL;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.novadart.novabill.android.authentication.HttpRequestTemplate;
import com.novadart.novabill.android.shared.dto.SyncDeltaStateDTO;

class DeltaStateRequestHelper implements HttpRequestTemplate.HttpRequestExecutor<SyncDeltaStateDTO>{
	
	private String path;
	private Long mark;

	DeltaStateRequestHelper(String path, Long mark){
		this.path = path;
		this.mark = mark;
	}

	@Override
	public String computePath(String restServicePath) {
		return restServicePath + path + (mark > 0? "?mark=" + mark: "");
	}

	@Override
	public SyncDeltaStateDTO execute(URL url, HttpEntity<?> requestEntity) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate.exchange(url.toString(), HttpMethod.GET, requestEntity, SyncDeltaStateDTO.class).getBody();
	}

	@Override
	public Object getHttpRequestBody() {
		return null;
	}
	
}