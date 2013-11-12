package com.novadart.novabill.frontend.client.bridge.server.autobean;

public interface EndPoint {
	
	public String getCompanyName();

	public void setCompanyName(String companyName);

	public String getStreet();

	public void setStreet(String street);

	public String getPostcode();

	public void setPostcode(String postcode);

	public String getCity();

	public void setCity(String city);

	public String getProvince();

	public void setProvince(String province);

	public String getCountry();

	public void setCountry(String country);

}
