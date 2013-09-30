package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientDTO implements IsSerializable, IClientDTO {

	private Long id;
	
	private String name;
	
	private String address;
	
	private String city;
	
	private String province;
	
	private String country;
	
	private String postcode;
	
	private String phone;
	
	private String mobile;
	
	private String fax;
	
	private String email;
	
	private String web;
	
	private String vatID;
	
	private String ssn;
	
	private Long defaultPaymentTypeID;
	
	private String note;
	
	private ContactDTO contact;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getProvince() {
		return province;
	}

	@Override
	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getPostcode() {
		return postcode;
	}

	@Override
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String getMobile() {
		return mobile;
	}

	@Override
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getWeb() {
		return web;
	}

	@Override
	public void setWeb(String web) {
		this.web = web;
	}

	@Override
	public String getVatID() {
		return vatID;
	}

	@Override
	public void setVatID(String vatID) {
		this.vatID = vatID;
	}

	@Override
	public String getSsn() {
		return ssn;
	}

	@Override
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	@Override
	public Long getDefaultPaymentTypeID() {
		return defaultPaymentTypeID;
	}

	@Override
	public void setDefaultPaymentTypeID(Long defaultPaymentTypeID) {
		this.defaultPaymentTypeID = defaultPaymentTypeID;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public ContactDTO getContact() {
		return contact;
	}

	@Override
	public void setContact(ContactDTO contact) {
		this.contact = contact;
	}
	
}
