package com.novadart.novabill.web.mvc.command;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class SharingRequest {

	@NotEmpty(message = "{required.email}")
	@Email(message = "{invalid.format.email}")
	private String email;
	
	@NotEmpty(message = "{required.vatID}")
	private String vatID;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVatID() {
		return vatID;
	}

	public void setVatID(String vatID) {
		this.vatID = vatID;
	}
	
}
