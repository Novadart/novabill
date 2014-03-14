package com.novadart.novabill.report;

import java.util.Date;
import java.util.List;

import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.shared.client.data.FilteringDateType;

public class PaymentsProspectJRDataSource extends AbstractJRDataSource {
	
	public PaymentsProspectJRDataSource(List<Invoice> invoices, Date startDate, Date endDate, FilteringDateType filteringDateType) {
		this.invoices = invoices;
		this.startDate = startDate;
		this.endDate = endDate;
		this.filteringDateType = filteringDateType;
	}

	private List<Invoice> invoices;
	
	private Date startDate;
	
	private Date endDate;
	
	private FilteringDateType filteringDateType;

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public FilteringDateType getFilteringDateType() {
		return filteringDateType;
	}

}
