package com.novadart.novabill.test.suite;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-test-config.xml")
@ActiveProfiles("dev")
public class ServiceTest extends AuthenticatedTest {
	
	@Resource(name = "testProps")
	protected HashMap<String, String> testProps;
	
	protected Integer getYear(){
		return Integer.valueOf(testProps.get("year"));
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, String> parseLogRecordDetailsJson(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, Map.class);
	}
	
	
	
}
