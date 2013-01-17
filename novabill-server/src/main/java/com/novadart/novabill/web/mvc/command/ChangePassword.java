package com.novadart.novabill.web.mvc.command;

import org.hibernate.validator.constraints.NotEmpty;

public class ChangePassword {
	
	private String email;
	
	@NotEmpty(message = "{required.password}")
	private String password;
	
	@NotEmpty(message = "{required.password}")
	private String newPassword;
	
	@NotEmpty(message = "{required.password}")
	private String confirmNewPassword;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

}
