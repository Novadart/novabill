package com.novadart.novabill.springsecurity;

import com.novadart.novabill.domain.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Deprecated
public class MigrationPasswordEncoder implements PasswordEncoder {
	
	@Autowired
	private ShaPasswordEncoder legacyEncoder;
	
	@PersistenceContext
	private EntityManager em;
	
	private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	@Override
	public String encodePassword(String rawPass, Object salt) {
		return bcryptEncoder.encode(rawPass);
	}

	@Override
	@Transactional
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		if(legacyEncoder.isPasswordValid(encPass, rawPass, salt)){
			Principal principal = em.createQuery("select p from Principal p where p.password = :p", Principal.class).setParameter("p", encPass).getSingleResult();
			principal.setPasswordNonHashed(bcryptEncoder.encode(rawPass));
			return true;
		}
		return bcryptEncoder.matches(rawPass, encPass);
	}

}
