package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.novadart.novabill.shared.client.tuple.Triple;

public class BICommodityStatsDTO {
	
	private BigDecimal totalBeforeTaxes;

	private BigDecimal totalBeforeTaxesCurrentYear;
	
	private Map<Integer, BigDecimal[]> totalsPerMonths;
	
	private List<Triple<ClientDTO, BigDecimal, BigDecimal>> clientStatsForCurrentYear;
	
	private List<Triple<ClientDTO, BigDecimal, BigDecimal>> clientStatsForPrevYear;
	
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

	public List<Triple<ClientDTO, BigDecimal, BigDecimal>> getClientStatsForCurrentYear() {
		return clientStatsForCurrentYear;
	}

	public void setClientStatsForCurrentYear(
			List<Triple<ClientDTO, BigDecimal, BigDecimal>> clientStatsForCurrentYear) {
		this.clientStatsForCurrentYear = clientStatsForCurrentYear;
	}

	public List<Triple<ClientDTO, BigDecimal, BigDecimal>> getClientStatsForPrevYear() {
		return clientStatsForPrevYear;
	}

	public void setClientStatsForPrevYear(
			List<Triple<ClientDTO, BigDecimal, BigDecimal>> clientStatsForPrevYear) {
		this.clientStatsForPrevYear = clientStatsForPrevYear;
	}

}
