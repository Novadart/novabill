package com.novadart.novabill.shared.client.dto;

public interface IBusinessDTO {

	public abstract Long getId();

	public abstract void setId(Long id);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getAddress();

	public abstract void setAddress(String address);

	public abstract String getCity();

	public abstract void setCity(String city);

	public abstract String getProvince();

	public abstract void setProvince(String province);

	public abstract String getCountry();

	public abstract void setCountry(String country);

	public abstract String getPostcode();

	public abstract void setPostcode(String postcode);

	public abstract String getPhone();

	public abstract void setPhone(String phone);

	public abstract String getMobile();

	public abstract void setMobile(String mobile);

	public abstract String getFax();

	public abstract void setFax(String fax);

	public abstract String getEmail();

	public abstract void setEmail(String email);

	public abstract String getWeb();

	public abstract void setWeb(String web);

	public abstract String getVatID();

	public abstract void setVatID(String vatID);

	public abstract String getSsn();

	public abstract void setSsn(String ssn);

	public abstract boolean isPremium();

	public abstract void setPremium(boolean premium);

}