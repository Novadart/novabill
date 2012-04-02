package com.novadart.novabill.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

@Service
public class AuthenticationTokenGenerator {
	
	private BASE64Encoder base64Encoder;
	
	private SecureRandom random;
	
	@PostConstruct
	public void init() throws NoSuchAlgorithmException{
		base64Encoder = new BASE64Encoder();
		random = SecureRandom.getInstance("SHA1PRNG");
	}
	
	public String generateToken() throws NoSuchAlgorithmException{
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(salt);
		byte[] tokenBytes = new byte[64];
		random.nextBytes(tokenBytes);
		return base64Encoder.encode(digest.digest(tokenBytes));
	}

}
