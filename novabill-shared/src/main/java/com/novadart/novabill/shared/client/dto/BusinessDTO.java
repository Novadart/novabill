package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BusinessDTO implements IsSerializable, IBusinessDTO {
	
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
	
	private boolean premium;

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getAddress()
	 */
	@Override
	public String getAddress() {
		return address;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setAddress(java.lang.String)
	 */
	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getCity()
	 */
	@Override
	public String getCity() {
		return city;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setCity(java.lang.String)
	 */
	@Override
	public void setCity(String city) {
		this.city = city;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getProvince()
	 */
	@Override
	public String getProvince() {
		return province;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setProvince(java.lang.String)
	 */
	@Override
	public void setProvince(String province) {
		this.province = province;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getCountry()
	 */
	@Override
	public String getCountry() {
		return country;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setCountry(java.lang.String)
	 */
	@Override
	public void setCountry(String country) {
		this.country = country;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getPostcode()
	 */
	@Override
	public String getPostcode() {
		return postcode;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setPostcode(java.lang.String)
	 */
	@Override
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getPhone()
	 */
	@Override
	public String getPhone() {
		return phone;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setPhone(java.lang.String)
	 */
	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getMobile()
	 */
	@Override
	public String getMobile() {
		return mobile;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setMobile(java.lang.String)
	 */
	@Override
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getFax()
	 */
	@Override
	public String getFax() {
		return fax;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setFax(java.lang.String)
	 */
	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getWeb()
	 */
	@Override
	public String getWeb() {
		return web;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setWeb(java.lang.String)
	 */
	@Override
	public void setWeb(String web) {
		this.web = web;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getVatID()
	 */
	@Override
	public String getVatID() {
		return vatID;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setVatID(java.lang.String)
	 */
	@Override
	public void setVatID(String vatID) {
		this.vatID = vatID;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#getSsn()
	 */
	@Override
	public String getSsn() {
		return ssn;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setSsn(java.lang.String)
	 */
	@Override
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#isPremium()
	 */
	@Override
	public boolean isPremium() {
		return premium;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.shared.client.dto.IBusinessDTO#setPremium(boolean)
	 */
	@Override
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	
}
