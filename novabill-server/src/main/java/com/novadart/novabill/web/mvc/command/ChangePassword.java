package com.novadart.novabill.web.mvc.command;

import org.hibernate.validator.constraints.NotEmpty;

import com.novadart.novabill.annotation.Hash;

public class ChangePassword {
	
	private String email;
	
	@NotEmpty(message = "{required.password}")
	private String password;
	
	@NotEmpty(message = "{required.password}")
	private String newPassword;
	
	@NotEmpty(message = "{required.password}")
	private String confirmNewPassword;
	
	private Long creationTime = System.currentTimeMillis();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	@Hash(saltMethod = "getCreationTime")
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

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

}
