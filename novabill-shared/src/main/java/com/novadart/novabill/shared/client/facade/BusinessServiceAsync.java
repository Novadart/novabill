package com.novadart.novabill.shared.client.facade;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

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

	void getCreditNotes(Long businessID,
			AsyncCallback<List<CreditNoteDTO>> callback);

	void getEstimations(Long businessID,
			AsyncCallback<List<EstimationDTO>> callback);

	void getInvoices(Long businessID, AsyncCallback<List<InvoiceDTO>> callback);

	void getTransportDocuments(Long businessID,
			AsyncCallback<List<TransportDocumentDTO>> callback);

	void getClients(Long businessID, AsyncCallback<List<ClientDTO>> callback);

	void get(Long businessID, AsyncCallback<BusinessDTO> callback);

	void updateNotesBitMask(Long notesBitMask, AsyncCallback<Long> callback);

	void generateLogoOpToken(AsyncCallback<String> callback);

}
