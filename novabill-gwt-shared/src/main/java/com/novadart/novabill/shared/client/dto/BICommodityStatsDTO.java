package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BICommodityStatsDTO {
	
	private BigDecimal totalBeforeTaxes;

	private BigDecimal totalBeforeTaxesCurrentYear;
	
	private Map<Integer, BigDecimal[]> totalsPerMonths;
	
	private List<Map<String, Object>> clientStatsForCurrentYear;
	
	private List<Map<String, Object>> clientStatsForPrevYear;
	
	public BigDecimal getTotalBeforeTaxes() {
		return totalBeforeTaxes;
	}

	public void setTotalBeforeTaxes(BigDecimal totalBeforeTaxes) {
		this.totalBeforeTaxes = totalBeforeTaxes;
	}

	public BigDecimal getTotalBeforeTaxesCurrentYear() {
		return totalBeforeTaxesCurrentYear;
	}

	public void setTotalBeforeTaxesCurrentYear(
			BigDecimal totalBeforeTaxesCurrentYear) {
		this.totalBeforeTaxesCurrentYear = totalBeforeTaxesCurrentYear;
	}

	public Map<Integer, BigDecimal[]> getTotalsPerMonths() {
		return totalsPerMonths;
	}

	public void setTotalsPerMonths(Map<Integer, BigDecimal[]> totalsPerMonths) {
		this.totalsPerMonths = totalsPerMonths;
	}

	public List<Map<String, Object>> getClientStatsForCurrentYear() {
		return clientStatsForCurrentYear;
	}

	public void setClientStatsForCurrentYear(List<Map<String, Object>> clientStatsForCurrentYear) {
		this.clientStatsForCurrentYear = clientStatsForCurrentYear;
	}

	public List<Map<String, Object>> getClientStatsForPrevYear() {
		return clientStatsForPrevYear;
	}

	public void setClientStatsForPrevYear(List<Map<String, Object>> clientStatsForPrevYear) {
		this.clientStatsForPrevYear = clientStatsForPrevYear;
	}


}
