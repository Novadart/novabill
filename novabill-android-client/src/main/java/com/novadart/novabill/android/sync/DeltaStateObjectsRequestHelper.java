package com.novadart.novabill.android.sync;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.novadart.novabill.android.authentication.HttpRequestTemplate;
import com.novadart.novabill.android.shared.data.SyncEntityType;
import com.novadart.novabill.android.shared.dto.SyncDeltaObjectsDTO;

public class DeltaStateObjectsRequestHelper implements HttpRequestTemplate.HttpRequestExecutor<SyncDeltaObjectsDTO>{
	
	private String path;
	
	private List<Long> clientIds;

	public DeltaStateObjectsRequestHelper(String path, List<Long> clientIds) {
		this.path = path;
		this.clientIds = clientIds;
	}

	@Override
	public String computePath(String restServicePath) {
		return restServicePath + path;
	}

	@Override
	public SyncDeltaObjectsDTO execute(URL url, HttpEntity<?> requestEntity) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return restTemplate.exchange(url.toString(), HttpMethod.POST, requestEntity, SyncDeltaObjectsDTO.class).getBody();
	}

	@Override
	public Object getHttpRequestBody() {
		Map<SyncEntityType, List<Long>> ids = new HashMap<SyncEntityType, List<Long>>();
		ids.put(SyncEntityType.CLIENT, clientIds);
		return ids;
	}

}
