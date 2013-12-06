package com.novadart.novabill.frontend.client.bridge.server.autobean;


public interface BusinessStats {

	Integer getClientsCount();

	void setClientsCount(Integer clientsCount);

	Integer getInvoicesCountForYear();

	void setInvoicesCountForYear(Integer invoicesCountForYear);

	Integer getCommoditiesCount();

	void setCommoditiesCount(Integer commoditiesCount);

	Double getTotalAfterTaxesForYear();

	void setTotalAfterTaxesForYear(Double totalAfterTaxesForYear);

	LogRecordList getLogRecords();

	void setLogRecords(LogRecordList logRecords);
}
