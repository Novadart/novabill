package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import com.google.gwt.user.client.rpc.IsSerializable;

public class InvoiceItemDTO implements IsSerializable {
	
	private Long id;
	
	private BigDecimal price;
	
	private String description;
	
	private String unitOfMeasure;
	
	private BigDecimal quantity;
	
	private BigDecimal tax;
	
	private BigDecimal totalBeforeTax;
	
	private BigDecimal totalTax;
	
	private BigDecimal total;
	
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

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

	public BigDecimal getTotalBeforeTax() {
		return totalBeforeTax;
	}

	public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
		this.totalBeforeTax = totalBeforeTax;
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTax(){
		return this.tax;
	}
	
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	
}
