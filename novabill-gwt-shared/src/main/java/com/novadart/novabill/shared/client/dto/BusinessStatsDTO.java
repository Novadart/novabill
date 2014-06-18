package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BusinessStatsDTO implements IsSerializable {
	
	private Integer clientsCount;
	
	private Integer invoicesCountForYear;
	
	private Integer commoditiesCount;
	
	private BigDecimal totalAfterTaxesForYear;
	
	private BigDecimal totalBeforeTaxesForYear;
	
	private List<LogRecordDTO> logRecords;
	
	private List<BigDecimal> invoiceTotalsPerMonth;
	
	public Integer getClientsCount() {
		return clientsCount;
	}
	
	public void setClientsCount(Integer clientsCount) {
		this.clientsCount = clientsCount;
	}
	
	public Integer getInvoicesCountForYear() {
		return invoicesCountForYear;
	}
	
	public void setInvoicesCountForYear(Integer invoicesCountForYear) {
		this.invoicesCountForYear = invoicesCountForYear;
	}
	
	public Integer getCommoditiesCount() {
		return commoditiesCount;
	}
	
	public void setCommoditiesCount(Integer commoditiesCount) {
		this.commoditiesCount = commoditiesCount;
	}
	
	public BigDecimal getTotalAfterTaxesForYear() {
		return totalAfterTaxesForYear;
	}
	
	public void setTotalAfterTaxesForYear(BigDecimal totalAfterTaxesForYear) {
		this.totalAfterTaxesForYear = totalAfterTaxesForYear;
	}

	public List<LogRecordDTO> getLogRecords() {
		return logRecords;
	}

	public void setLogRecords(List<LogRecordDTO> logRecords) {
		this.logRecords = logRecords;
	}

	public List<BigDecimal> getInvoiceTotalsPerMonth() {
		return invoiceTotalsPerMonth;
	}

	public void setInvoiceTotalsPerMonth(List<BigDecimal> invoiceTotalsPerMonth) {
		this.invoiceTotalsPerMonth = invoiceTotalsPerMonth;
	}

	public BigDecimal getTotalBeforeTaxesForYear() {
		return totalBeforeTaxesForYear;
	}

	public void setTotalBeforeTaxesForYear(BigDecimal totalBeforeTaxesForYear) {
		this.totalBeforeTaxesForYear = totalBeforeTaxesForYear;
	}
	
}
