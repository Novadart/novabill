package com.novadart.novabill.shared.client.dto;

public class PriceListDTO {
	
	private Long id;
	
	private String name;

	private BusinessDTO business;

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
	
}
