package com.novadart.novabill.email;

import java.util.Map;

public interface ValueProducer {

	public String produceValue(Map<String, Object> context);
	
}
