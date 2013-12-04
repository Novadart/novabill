package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.util.Map;

public interface PricesMap {
	
	public Map<String, Price> getPrices();
	
	public void setPrices(Map<String, Price> prices);

}
