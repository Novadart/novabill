package com.novadart.novabill.android.shared.dto;

import java.math.BigDecimal;

public class BusinessStatsDTO {
	
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
