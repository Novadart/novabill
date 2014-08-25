package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class BIClientStatsDTO {

	private Date firstInvoiceDate;
	
	private BigDecimal totalBeforeTaxes;
	
	private BigDecimal totalBeforeTaxesCurrentYear;
	
	private Map<Integer, BigDecimal[]> totalsPerMonths;

	public Date getFirstInvoiceDate() {
		return firstInvoiceDate;
	}

	public void setFirstInvoiceDate(Date firstInvoiceDate) {
		this.firstInvoiceDate = firstInvoiceDate;
	}

	public BigDecimal getTotalBeforeTaxes() {
		return totalBeforeTaxes;
	}

	public void setTotalBeforeTaxes(BigDecimal totalBeforeTaxes) {
		this.totalBeforeTaxes = totalBeforeTaxes;
	}

	public BigDecimal getTotalBeforeTaxesCurrentYear() {
		return totalBeforeTaxesCurrentYear;
	}

	public void setTotalBeforeTaxesCurrentYear(BigDecimal totalBeforeTaxesCurrentYear) {
		this.totalBeforeTaxesCurrentYear = totalBeforeTaxesCurrentYear;
	}

	public Map<Integer, BigDecimal[]> getTotalsPerMonths() {
		return totalsPerMonths;
	}

	public void setTotalsPerMonths(Map<Integer, BigDecimal[]> totalsPerMonths) {
		this.totalsPerMonths = totalsPerMonths;
	}

	
	
}
