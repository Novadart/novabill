package com.novadart.novabill.frontend.client.bridge.server.autobean;


public interface Price {
	
	public Long getId();

	public void setId(Long id);

	public Double getPriceValue();

	public void setPriceValue(Double priceValue);

	public String getPriceType();

	public void setPriceType(String priceType);

	public Long getCommodityID();

	public void setCommodityID(Long commodityID);

	public Long getPriceListID();

	public void setPriceListID(Long priceListID);

}
