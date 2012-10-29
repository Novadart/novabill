package com.novadart.novabill.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Embeddable
public class Endpoint {
	
	@Size(max = 255)
	private String companyName;
	
	@Size(max = 255)
	private String street;
	
	@Size(max = 10)
	private String postcode;
	
	@Size(max = 60)
	private String city;
	
	@Size(max = 2)
	private String province;
	
	@Size(max = 3)
	private String country;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
