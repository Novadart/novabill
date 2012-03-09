package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BusinessStatsDTO implements IsSerializable {
	
	private Long clientsCount;
	private Long invoicesCountForYear;
	private BigDecimal totalAfterTaxesForYear;
	
	public Long getClientsCount() {
		return clientsCount;
	}
	public void setClientsCount(Long clientsCount) {
		this.clientsCount = clientsCount;
	}
	public Long getInvoicesCountForYear() {
		return invoicesCountForYear;
	}
	public void setInvoicesCountForYear(Long invoicesCountForYear) {
		this.invoicesCountForYear = invoicesCountForYear;
	}
	public BigDecimal getTotalAfterTaxesForYear() {
		return totalAfterTaxesForYear;
	}
	public void setTotalAfterTaxesForYear(BigDecimal totalAfterTaxesForYear) {
		this.totalAfterTaxesForYear = totalAfterTaxesForYear;
	}
	
}
