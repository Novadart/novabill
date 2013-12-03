package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CommodityDTO implements IsSerializable {
	
	private Long id;
	
	private String sku;
	
	private String description;
	
	private String unitOfMeasure;
	
	private BigDecimal tax;
	
	private boolean service;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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

}
