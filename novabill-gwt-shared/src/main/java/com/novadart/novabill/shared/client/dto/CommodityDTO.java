package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CommodityDTO implements IsSerializable {
	
	private Long id;
	
	private String sku;
	
	private String description;
	
	private String unitOfMeasure;
	
	private BigDecimal tax;
	
	private boolean service;
	
	private BigDecimal weight;
	
	private BusinessDTO business;
	
	private Map<String, PriceDTO> prices;
	
	@JsonSerialize(using=ToStringSerializer.class)
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

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BusinessDTO getBusiness() {
		return business;
	}

	public void setBusiness(BusinessDTO business) {
		this.business = business;
	}

	public Map<String, PriceDTO> getPrices() {
		return prices;
	}

	public void setPrices(Map<String, PriceDTO> prices) {
		this.prices = prices;
	}
	
}
