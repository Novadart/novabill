package com.novadart.novabill.shared.client.dto;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PriceListDTO implements IsSerializable {
	
	private Long id;
	
	private String name;

	private BusinessDTO business;
	
	private List<CommodityDTO> commodities;
	
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

	public BusinessDTO getBusiness() {
		return business;
	}

	public void setBusiness(BusinessDTO business) {
		this.business = business;
	}

	public List<CommodityDTO> getCommodities() {
		return commodities;
	}

	public void setCommodities(List<CommodityDTO> commodities) {
		this.commodities = commodities;
	}

}
