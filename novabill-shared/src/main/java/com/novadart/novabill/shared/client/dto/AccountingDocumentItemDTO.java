package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import com.google.gwt.user.client.rpc.IsSerializable;

public class AccountingDocumentItemDTO implements IsSerializable, IAccountingDocumentItemDTO {
	
	private Long id;
	
	private BigDecimal price;
	
	private String description;
	
	private String unitOfMeasure;
	
	private BigDecimal quantity;
	
	private BigDecimal tax;
	
	private BigDecimal totalBeforeTax;
	
	private BigDecimal totalTax;
	
	private BigDecimal total;
	
	@Override
	public BigDecimal getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	@Override
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	@Override
	public BigDecimal getTotalBeforeTax() {
		return totalBeforeTax;
	}

	@Override
	public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
		this.totalBeforeTax = totalBeforeTax;
	}

	@Override
	public BigDecimal getTotalTax() {
		return totalTax;
	}

	@Override
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	@Override
	public BigDecimal getTotal() {
		return total;
	}

	@Override
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public BigDecimal getTax(){
		return this.tax;
	}
	
	@Override
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	
	@Override
	public boolean isDescriptionOnly(){
		return price == null;
	}
	
	public AccountingDocumentItemDTO clone(){
		AccountingDocumentItemDTO i = new AccountingDocumentItemDTO();
		i.setDescription(getDescription());
		i.setPrice(getPrice());
		i.setQuantity(getQuantity());
		i.setTax(getTax());
		i.setTotal(getTotal());
		i.setTotalBeforeTax(getTotalBeforeTax());
		i.setTotalTax(getTotalTax());
		i.setUnitOfMeasure(getUnitOfMeasure());
		return i;
	}
}
