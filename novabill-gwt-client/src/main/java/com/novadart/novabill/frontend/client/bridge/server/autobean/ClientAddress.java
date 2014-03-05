package com.novadart.novabill.frontend.client.bridge.server.autobean;



public interface ClientAddress {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public String getCompanyName();

	public void setCompanyName(String companyName);

	public String getAddress();

	public void setAddress(String address);

	public String getPostcode();

	public void setPostcode(String postcode);

	public String getCity();

	public void setCity(String city);

	public String getProvince();

	public void setProvince(String province);

	public String getCountry();

	public void setCountry(String country);

	public Client getClient();

	public void setClient(Client client);

}
