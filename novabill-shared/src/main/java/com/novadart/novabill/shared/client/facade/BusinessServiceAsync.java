package com.novadart.novabill.shared.client.facade;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public interface BusinessServiceAsync {

	void countClients(Long businessID, AsyncCallback<Long> callback);

	void countInvoices(Long businessID, AsyncCallback<Long> callback);

	void countInvoicesForYear(Long BusinessID, Integer year,
			AsyncCallback<Long> callback);

	void generateExportToken(AsyncCallback<String> callback);

	void generatePDFToken(AsyncCallback<String> callback);

	void getStats(Long businessID, AsyncCallback<BusinessStatsDTO> callback);

	void getTotalAfterTaxesForYear(Long businessID, Integer year,
			AsyncCallback<BigDecimal> callback);

	void update(BusinessDTO businessDTO, AsyncCallback<Void> callback);

}
