package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

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
@ContextConfiguration(locations = "classpath*:authenticate-test-config.xml")
@ActiveProfiles("dev")
public class AuthenticatedTest {

	@Resource(name = "userPasswordMap")
	protected HashMap<String, String> userPasswordMap;
	
	protected Principal authenticatedPrincipal;
	
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