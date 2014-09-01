package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class BIGeneralStatsDTO {
	
	private Map<Integer, BigDecimal[]> totalsPerMonths;
	
	private Map<String, BigDecimal> totals;
	
	private Map<String, Integer> clientsVsReturningClients;
	
	private List<Map<String, Object>> clientRankingByRevenue;
	
	private List<Map<String, Object>> commodityRankingByRevenue;

	public Map<Integer, BigDecimal[]> getTotalsPerMonths() {
		return totalsPerMonths;
	}

	public void setTotalsPerMonths(Map<Integer, BigDecimal[]> totalsPerMonth) {
		this.totalsPerMonths = totalsPerMonth;
	}

	public Map<String, BigDecimal> getTotals() {
		return totals;
	}

	public void setTotals(Map<String, BigDecimal> totals) {
		this.totals = totals;
	}

	public Map<String, Integer> getClientsVsReturningClients() {
		return clientsVsReturningClients;
	}

	public void setClientsVsReturningClients(Map<String, Integer> clientsVsReturningClients) {
		this.clientsVsReturningClients = clientsVsReturningClients;
	}
	
	public List<Map<String, Object>> getClientRankingByRevenue() {
		return clientRankingByRevenue;
	}

	public void setClientRankingByRevenue(List<Map<String, Object>> clientRankingByRevenue) {
		this.clientRankingByRevenue = clientRankingByRevenue;
	}

	public List<Map<String, Object>> getCommodityRankingByRevenue() {
		return commodityRankingByRevenue;
	}

	public void setCommodityRankingByRevenue(List<Map<String, Object>> commodityRankingByRevenue) {
		this.commodityRankingByRevenue = commodityRankingByRevenue;
	}
	
}
