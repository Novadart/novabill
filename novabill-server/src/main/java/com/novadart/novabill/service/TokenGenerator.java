package com.novadart.novabill.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {
	
	private final Object lock = new Object();
	
	private SecureRandom random;
	
	@PostConstruct
	public void init() throws NoSuchAlgorithmException{
		random = SecureRandom.getInstance("SHA1PRNG");
	}
	
	public String generateToken() throws NoSuchAlgorithmException{
		synchronized (lock) {
			byte[] salt = new byte[8];
			random.nextBytes(salt);
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(salt);
			byte[] tokenBytes = new byte[64];
			random.nextBytes(tokenBytes);
			return Base64.encodeBase64String(digest.digest(tokenBytes));
		}
	}

}
