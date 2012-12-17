package com.novadart.novabill.service;

import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
public class XsrfTokenService {
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@SuppressWarnings("unchecked")
	private Set<String> getTokensSet(HttpSession session, String tokensSessionField){
		Set<String> set;
		synchronized (WebUtils.getSessionMutex(session)) {
			set = (Set<String>) session.getAttribute(tokensSessionField);
			if (set == null) {
				set = new ConcurrentSkipListSet<String>();
				session.setAttribute(tokensSessionField, set);
			}
		}
		return set;
	}
	
	public boolean verifyAndRemoveToken(String token, HttpSession session, String tokensSessionField){
		Set<String> tokens = getTokensSet(session, tokensSessionField);
		boolean wasPresent = tokens.remove(token);
		if(wasPresent)
			session.setAttribute(tokensSessionField, tokens);
		return wasPresent;
	}
	
	public String generateToken(HttpSession session, String tokensSessionField) throws NoSuchAlgorithmException{
		Set<String> tokens = getTokensSet(session, tokensSessionField);
		String token;
		while(!tokens.add(token = tokenGenerator.generateToken()));
		session.setAttribute(tokensSessionField, tokens);
		return token;
	}

}
