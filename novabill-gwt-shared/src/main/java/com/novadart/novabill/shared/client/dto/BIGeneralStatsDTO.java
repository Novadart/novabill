package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.novadart.novabill.shared.client.tuple.Pair;


public class BIGeneralStatsDTO {
	
	private Map<Integer, BigDecimal[]> totalsPerMonths;
	
	private Pair<BigDecimal, BigDecimal> totals;
	
	private Pair<Integer, Integer> clientsVsReturningClients;

	public Map<Integer, BigDecimal[]> getTotalsPerMonths() {
		return totalsPerMonths;
	}

	public void setTotalsPerMonths(Map<Integer, BigDecimal[]> totalsPerMonth) {
		this.totalsPerMonths = totalsPerMonth;
	}

	public Pair<BigDecimal, BigDecimal> getTotals() {
		return totals;
	}

	public void setTotals(Pair<BigDecimal, BigDecimal> totals) {
		this.totals = totals;
	}

	public Pair<Integer, Integer> getClientsVsReturningClients() {
		return clientsVsReturningClients;
	}

	public void setClientsVsReturningClients(
			Pair<Integer, Integer> clientsVsReturningClients) {
		this.clientsVsReturningClients = clientsVsReturningClients;
	}
	
	
	
}
