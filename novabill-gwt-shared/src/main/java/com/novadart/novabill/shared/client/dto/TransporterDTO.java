package com.novadart.novabill.shared.client.dto;

public class TransporterDTO {
	
	private Long id;
	
	private String description;
	
	private BusinessDTO business;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BusinessDTO getBusiness() {
		return business;
	}

	public void setBusiness(BusinessDTO business) {
		this.business = business;
	}

}
