package com.novadart.novabill.frontend.client.bridge.server.autobean;


public interface Business {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public String getAddress();

	public void setAddress(String address);

	public String getCity();

	public void setCity(String city);

	public String getProvince();

	public void setProvince(String province);

	public String getCountry();

	public void setCountry(String country);

	public String getPostcode();

	public void setPostcode(String postcode);

	public String getPhone();

	public void setPhone(String phone);

	public String getMobile();

	public void setMobile(String mobile);

	public String getFax();

	public void setFax(String fax);

	public String getEmail();

	public void setEmail(String email);

	public String getWeb();

	public void setWeb(String web);

	public String getVatID();

	public void setVatID(String vatID);

	public String getSsn();

	public void setSsn(String ssn);

	public boolean getPremium();

	public void setPremium(boolean premium);
	
	public Settings getSettings();

	public void setSettings(Settings settings);

}
