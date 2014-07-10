package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.data.PriceType;

public class PriceDTO implements IsSerializable {
	
	private Long id;
	
	private BigDecimal priceValue;
	
	private PriceType priceType;
	
	private Long commodityID;
	
	private Long priceListID;
	
	public PriceDTO(){}
	
	public PriceDTO(Long commodityID, Long priceListID) {
		this.commodityID = commodityID;
		this.priceListID = priceListID;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Long getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(Long commodityID) {
		this.commodityID = commodityID;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Long getPriceListID() {
		return priceListID;
	}

	public void setPriceListID(Long priceListID) {
		this.priceListID = priceListID;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(BigDecimal priceValue) {
		this.priceValue = priceValue;
	}
	
}
