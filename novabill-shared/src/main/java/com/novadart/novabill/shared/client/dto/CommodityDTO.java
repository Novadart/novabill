package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CommodityDTO implements IsSerializable {
	
	private Long id;
	
	private BigDecimal price;
	
	private String description;
	
	private String unitOfMeasure;
	
	private BigDecimal tax;
	
	private boolean service;
	
	private BusinessDTO business;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public boolean isService() {
		return service;
	}

	public void setService(boolean service) {
		this.service = service;
	}

	public BusinessDTO getBusiness() {
		return business;
	}

	public void setBusiness(BusinessDTO business) {
		this.business = business;
	}
	
}
