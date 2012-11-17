package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.novadart.novabill.domain.security.Principal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-test-config.xml")
public class GWTServiceTest {
	
	@Resource(name = "userPasswordMap")
	protected HashMap<String, String> userPasswordMap;
	
	protected Principal authenticatedPrincipal;
	
	private Principal fetchRandomPrincipal(){
		List<Principal> allPrincipals = Principal.findAllPrincipals();
		return allPrincipals.get(new Random().nextInt(allPrincipals.size()));
	}
	
	@Before
	public void authenticate(){
		authenticatedPrincipal = fetchRandomPrincipal();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authenticatedPrincipal.getUsername(), userPasswordMap.get(authenticatedPrincipal.getUsername()));
		SecurityContextHolder.getContext().setAuthentication(token);
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
