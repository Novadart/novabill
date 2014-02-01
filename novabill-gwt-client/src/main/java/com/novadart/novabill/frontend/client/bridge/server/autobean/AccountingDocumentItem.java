package com.novadart.novabill.frontend.client.bridge.server.autobean;


public interface AccountingDocumentItem {

	public String getQuantity();

	public void setQuantity(String quantity);
	
	public String getWeight();

	public void setWeight(String weight);

	public Long getId();

	public void setId(Long id);

	public String getPrice();

	public void setPrice(String price);

	public String getDescription();

	public void setDescription(String description);

	public String getUnitOfMeasure();

	public void setUnitOfMeasure(String unitOfMeasure);

	public String getTotalBeforeTax();

	public void setTotalBeforeTax(String totalBeforeTax);

	public String getTotalTax();

	public void setTotalTax(String totalTax);

	public String getTotal();

	public void setTotal(String total);

	public String getTax();

	public void setTax(String tax);

	public boolean isDescriptionOnly();
	
	public String getSku();

	public void setSku(String sku);

}
