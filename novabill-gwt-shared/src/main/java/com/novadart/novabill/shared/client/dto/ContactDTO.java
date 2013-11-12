package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContactDTO implements IsSerializable {
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;
	
	private String fax;
	
	private String mobile;
	
	
	public String getFirstName() {
		return firstName;
	}

	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getLastName() {
		return lastName;
	}

	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getEmail() {
		return email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getPhone() {
		return phone;
	}

	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getFax() {
		return fax;
	}

	
	public void setFax(String fax) {
		this.fax = fax;
	}

	
	public String getMobile() {
		return mobile;
	}

	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
