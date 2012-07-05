package com.novadart.novabill.service;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
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
			set = Collections.synchronizedSet(new HashSet<String>());
			session.setAttribute(tokensSessionField, set);
		}
		return set;
	}
	
	public boolean verifyAndRemoveToken(String token, HttpSession session, String tokensSessionField){
		Set<String> tokens = getTokensSet(session, tokensSessionField);
		if(!tokens.contains(token))
			return false;
		tokens.remove(token);
		return true;
	}
	
	public String generateToken(HttpSession session, String tokensSessionField) throws NoSuchAlgorithmException{
		String token = tokenGenerator.generateToken();
		Set<String> tokens = getTokensSet(session, tokensSessionField);
		tokens.add(token);
		return token;
	}

}
