package com.novadart.novabill.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XsrfTokenService {
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@SuppressWarnings("unchecked")
	private Set<String> getTokensSet(HttpSession session, String tokensSessionField){
		Set<String> set = (Set<String>)session.getAttribute(tokensSessionField);
		if(set == null){
			set = new HashSet<String>();
			session.setAttribute(tokensSessionField, set);
		}
		return set;
	}
	
	public synchronized boolean verifyAndRemoveToken(String token, HttpSession session, String tokensSessionField){
		Set<String> tokens = getTokensSet(session, tokensSessionField);
		return tokens.remove(token);
	}
	
	public synchronized String generateToken(HttpSession session, String tokensSessionField) throws NoSuchAlgorithmException{
		Set<String> tokens = getTokensSet(session, tokensSessionField);
		String token;
		while(!tokens.add(token = tokenGenerator.generateToken()));
		return token;
	}

}
