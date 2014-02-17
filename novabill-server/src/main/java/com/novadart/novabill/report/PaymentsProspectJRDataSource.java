package com.novadart.novabill.report;

import java.util.Date;
import java.util.List;

import com.novadart.novabill.domain.Invoice;

public class PaymentsProspectJRDataSource extends AbstractJRDataSource {
	
	public PaymentsProspectJRDataSource(List<Invoice> invoices, Date startDate, Date endDate) {
		this.invoices = invoices;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	private List<Invoice> invoices;
	
	private Date startDate;
	
	private Date endDate;

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

}
