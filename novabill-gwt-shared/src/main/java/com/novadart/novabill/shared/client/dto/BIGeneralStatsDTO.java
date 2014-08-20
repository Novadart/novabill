package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.Map;

public class BIGeneralStatsDTO {
	
	private Map<Integer, BigDecimal[]> totalsPerMonths;

	public Map<Integer, BigDecimal[]> getTotalsPerMonths() {
		return totalsPerMonths;
	}

	public void setTotalsPerMonths(Map<Integer, BigDecimal[]> totalsPerMonth) {
		this.totalsPerMonths = totalsPerMonth;
	}

}
