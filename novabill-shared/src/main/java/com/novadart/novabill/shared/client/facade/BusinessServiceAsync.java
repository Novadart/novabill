package com.novadart.novabill.shared.client.facade;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public interface BusinessServiceAsync {

	void update(BusinessDTO businessDTO, AsyncCallback<Void> callback);

	void countClients(AsyncCallback<Long> callback);

	void countInvoices(AsyncCallback<Long> callback);

	void countInvoicesForYear(int year, AsyncCallback<Long> callback);

	void getTotalAfterTaxesForYear(int year, AsyncCallback<BigDecimal> callback);

	void getStats(AsyncCallback<BusinessStatsDTO> callback);

	void generatePDFToken(AsyncCallback<String> callback);

	void generateExportToken(AsyncCallback<String> callback);

}
