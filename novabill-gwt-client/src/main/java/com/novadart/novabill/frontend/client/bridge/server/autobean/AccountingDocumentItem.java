package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;

public interface AccountingDocumentItem {

	public BigDecimal getQuantity();

	public void setQuantity(BigDecimal quantity);

	public Long getId();

	public void setId(Long id);

	public BigDecimal getPrice();

	public void setPrice(BigDecimal price);

	public String getDescription();

	public void setDescription(String description);

	public String getUnitOfMeasure();

	public void setUnitOfMeasure(String unitOfMeasure);

	public BigDecimal getTotalBeforeTax();

	public void setTotalBeforeTax(BigDecimal totalBeforeTax);

	public BigDecimal getTotalTax();

	public void setTotalTax(BigDecimal totalTax);

	public BigDecimal getTotal();

	public void setTotal(BigDecimal total);

	public BigDecimal getTax();

	public void setTax(BigDecimal tax);

	public boolean isDescriptionOnly();
	
	public String getSku();

	public void setSku(String sku);

}
