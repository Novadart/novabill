package com.novadart.novabill.web.mvc.command;

import org.hibernate.validator.constraints.NotEmpty;

public class DeleteAccount {
	
	@NotEmpty(message = "{required.password}")
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
