package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BIClientStatsDTO {

	private BigDecimal totalBeforeTaxes;

	private BigDecimal totalBeforeTaxesCurrentYear;
	
	private Map<Integer, BigDecimal[]> totalsPerMonths;
	
	private List<Map<String, Object>> commodityStatsForCurrentYear;
	
	private List<Map<String, Object>> commodityStatsForPrevYear;
	
	private Long creationTime;

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	public BigDecimal getTotalBeforeTaxes() {
		return totalBeforeTaxes;
	}

	public void setTotalBeforeTaxes(BigDecimal totalBeforeTaxes) {
		this.totalBeforeTaxes = totalBeforeTaxes;
	}

	public Map<Integer, BigDecimal[]> getTotalsPerMonths() {
		return totalsPerMonths;
	}

	public void setTotalsPerMonths(Map<Integer, BigDecimal[]> totalsPerMonths) {
		this.totalsPerMonths = totalsPerMonths;
	}

	public List<Map<String, Object>> getCommodityStatsForCurrentYear() {
		return commodityStatsForCurrentYear;
	}

	public void setCommodityStatsForCurrentYear(List<Map<String, Object>> commodityStatsForCurrentYear) {
		this.commodityStatsForCurrentYear = commodityStatsForCurrentYear;
	}

	public List<Map<String, Object>> getCommodityStatsForPrevYear() {
		return commodityStatsForPrevYear;
	}

	public void setCommodityStatsForPrevYear(List<Map<String, Object>> commodityStatsForPrevYear) {
		this.commodityStatsForPrevYear = commodityStatsForPrevYear;
	}

	public BigDecimal getTotalBeforeTaxesCurrentYear() {
		return totalBeforeTaxesCurrentYear;
	}

	public void setTotalBeforeTaxesCurrentYear(BigDecimal totalBeforeTaxesCurrentYear) {
		this.totalBeforeTaxesCurrentYear = totalBeforeTaxesCurrentYear;
	}
}
