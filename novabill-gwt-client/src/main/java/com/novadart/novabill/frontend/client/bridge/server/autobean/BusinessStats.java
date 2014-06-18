package com.novadart.novabill.frontend.client.bridge.server.autobean;


public interface BusinessStats {

	Integer getClientsCount();

	void setClientsCount(Integer clientsCount);

	Integer getInvoicesCountForYear();

	void setInvoicesCountForYear(Integer invoicesCountForYear);

	Integer getCommoditiesCount();

	void setCommoditiesCount(Integer commoditiesCount);

	String getTotalAfterTaxesForYear();

	void setTotalAfterTaxesForYear(String totalAfterTaxesForYear);
	
	String getTotalBeforeTaxesForYear();

	void setTotalBeforeTaxesForYear(String totalBeforeTaxesForYear);

	LogRecordList getLogRecords();

	void setLogRecords(LogRecordList logRecords);
	
	InvoiceTotalsPerMonthList getInvoiceTotalsPerMonth();

	void setInvoiceTotalsPerMonth(InvoiceTotalsPerMonthList totals);
}
