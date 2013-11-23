package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;
import java.util.Map;

public interface Commodity {
	
	public Long getId();

	public void setId(Long id);

	public String getDescription();

	public void setDescription(String description);

	public String getUnitOfMeasure();

	public void setUnitOfMeasure(String unitOfMeasure);

	public BigDecimal getTax();

	public void setTax(BigDecimal tax);

	public boolean isService();

	public void setService(boolean service);

	public Business getBusiness();

	public void setBusiness(Business business);

	public Map<String, BigDecimal> getCustomPrices();

	public void setCustomPrices(Map<String, BigDecimal> customPrices);

}
