package com.novadart.novabill.springsecurity;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Deprecated
public class MigrationPasswordEncoder implements PasswordEncoder {
	
	@Autowired
	private ShaPasswordEncoder legacyEncoder;
	
	@Autowired
	private DataSource dataSource;
	
	private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	@Override
	public String encodePassword(String rawPass, Object salt) {
		return bcryptEncoder.encode(rawPass);
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		if(legacyEncoder.isPasswordValid(encPass, rawPass, salt)){
			JdbcTemplate template = new JdbcTemplate(dataSource);
			template.update("update principal set password = ? where password = ?", bcryptEncoder.encode(rawPass), encPass);
			return true;
		}
		return bcryptEncoder.matches(rawPass, encPass);
	}

}
