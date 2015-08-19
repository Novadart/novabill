package com.novadart.novabill.test.suite;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-test-config.xml")
@ActiveProfiles("dev")
@DirtiesContext
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
