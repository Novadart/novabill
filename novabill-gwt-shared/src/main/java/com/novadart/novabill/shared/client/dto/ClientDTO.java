package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientDTO implements IsSerializable {

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
	
	private Long defaultPriceListID;

	private Long defaultDocumentIDClassID;
	
	private String note;

	private boolean splitPaymentClient;

	private ContactDTO contact;

	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public String getAddress() {
		return address;
	}

	
	public void setAddress(String address) {
		this.address = address;
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

	
	public String getPostcode() {
		return postcode;
	}

	
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	
	public String getPhone() {
		return phone;
	}

	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getMobile() {
		return mobile;
	}

	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	public String getFax() {
		return fax;
	}

	
	public void setFax(String fax) {
		this.fax = fax;
	}

	
	public String getEmail() {
		return email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getWeb() {
		return web;
	}

	
	public void setWeb(String web) {
		this.web = web;
	}

	
	public String getVatID() {
		return vatID;
	}

	
	public void setVatID(String vatID) {
		this.vatID = vatID;
	}

	
	public String getSsn() {
		return ssn;
	}

	
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	
	public Long getDefaultPaymentTypeID() {
		return defaultPaymentTypeID;
	}

	
	public void setDefaultPaymentTypeID(Long defaultPaymentTypeID) {
		this.defaultPaymentTypeID = defaultPaymentTypeID;
	}

	
	public Long getDefaultPriceListID() {
		return defaultPriceListID;
	}


	public void setDefaultPriceListID(Long defaultPriceListID) {
		this.defaultPriceListID = defaultPriceListID;
	}


	public String getNote() {
		return note;
	}

	
	public void setNote(String note) {
		this.note = note;
	}

	public boolean isSplitPaymentClient() {
		return splitPaymentClient;
	}

	public void setSplitPaymentClient(boolean splitPaymentClient) {
		this.splitPaymentClient = splitPaymentClient;
	}

	public ContactDTO getContact() {
		return contact;
	}

	
	public void setContact(ContactDTO contact) {
		this.contact = contact;
	}

	public Long getDefaultDocumentIDClassID() {
		return defaultDocumentIDClassID;
	}

	public void setDefaultDocumentIDClassID(Long defaultDocumentIDClassID) {
		this.defaultDocumentIDClassID = defaultDocumentIDClassID;
	}
}
