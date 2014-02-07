package com.novadart.novabill.frontend.client.bridge.server.autobean;



public interface Commodity {
	
	public Long getId();

	public void setId(Long id);

	public String getDescription();

	public void setDescription(String description);
	
	public String getSku();

	public void setSku(String sku);

	public String getUnitOfMeasure();

	public void setUnitOfMeasure(String unitOfMeasure);

	public String getTax();

	public void setTax(String tax);
	
	public String getWeight();

	public void setWeight(String weight);

	public boolean isService();

	public void setService(boolean service);
	
	public PricesMap getPricesMap();
	
	public void setPricesMap(PricesMap pricesMap);

}
