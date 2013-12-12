package com.novadart.novabill.frontend.client.bridge.server.autobean;



public interface PriceList {
	
	Long getId();

	void setId(Long id);

	String getName();

	void setName(String name);

	Business getBusiness();

	void setBusiness(Business business);

	PricesList getPrices();

	void setPrices(PricesList prices);
	

}
