package com.novadart.novabill.web.mvc;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.novadart.novabill.service.TokenGenerator;

public abstract class AbstractXsrfContoller {
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	protected abstract String getTokensSessionField();
	
	@SuppressWarnings("unchecked")
	protected Set<String> getTokensSet(HttpSession session){
		String tokensSessionField = getTokensSessionField();
		Set<String> set = (Set<String>)session.getAttribute(tokensSessionField);
		if(set == null){
			set = Collections.synchronizedSet(new HashSet<String>());
			session.setAttribute(tokensSessionField, set);
		}
		return set;
	}
	
	protected boolean verifyAndRemoveToken(String token, HttpSession session){
		Set<String> tokens = getTokensSet(session);
		if(!tokens.contains(token))
			return false;
		tokens.remove(token);
		return true;
	}

	@RequestMapping("/token")
	@ResponseBody
	public String generateToken(HttpSession session) throws NoSuchAlgorithmException{
		String token = tokenGenerator.generateToken();
		Set<String> tokens = getTokensSet(session);
		tokens.add(token);
		return token;
	}
	
}
