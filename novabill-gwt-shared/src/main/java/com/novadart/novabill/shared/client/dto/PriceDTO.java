package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.data.PriceType;

public class PriceDTO implements IsSerializable {
	
	private Long id;
	
	private BigDecimal quantity;
	
	private PriceType priceType;
	
	private Long commodityID;
	
	private Long priceListID;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quatity) {
		this.quantity = quatity;
	}

	public PriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
	}

	public Long getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(Long commodityID) {
		this.commodityID = commodityID;
	}

	public Long getPriceListID() {
		return priceListID;
	}

	public void setPriceListID(Long priceListID) {
		this.priceListID = priceListID;
	}
	
}
