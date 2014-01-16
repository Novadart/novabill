package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.novadart.novabill.domain.security.Principal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-test-config.xml")
@ActiveProfiles("dev")
public class GWTServiceTest {
	
	@Resource(name = "userPasswordMap")
	protected HashMap<String, String> userPasswordMap;
	
	@Resource(name = "testProps")
	protected HashMap<String, String> testProps;
	
	protected Principal authenticatedPrincipal;
	
	protected Integer getYear(){
		return Integer.valueOf(testProps.get("year"));
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, String> parseLogRecordDetailsJson(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, Map.class);
	}
	
	/**
	 * There has to be at least two businesses
	 * @return
	 */
	protected Long getUnathorizedBusinessID(){
		for(Principal principal: Principal.findAllPrincipals())
			if(principal.getBusiness().getId() != authenticatedPrincipal.getBusiness().getId())
				return principal.getBusiness().getId();
		return -1l;
	}
	
	protected Principal fetchRandomPrincipal(){
		List<Principal> allPrincipals = Principal.findAllPrincipals();
		return allPrincipals.get(new Random().nextInt(allPrincipals.size()));
	}
	
	protected void authenticatePrincipal(Principal principal){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, userPasswordMap.get(principal.getUsername()));
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
	@Before
	public void authenticate(){
		authenticatedPrincipal = fetchRandomPrincipal();
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@After
	public void logout(){
		authenticatedPrincipal = null;
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	@Test
	public void autowiringTest(){
		assertNotNull(userPasswordMap);
	}
	
}
