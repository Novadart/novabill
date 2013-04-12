package com.novadart.novabill.frontend.client.demo.facade.service;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;

public class DemoBusinessService implements BusinessServiceAsync {

	@Override
	public void countClients(Long businessID, AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void countInvoices(Long businessID, AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void countInvoicesForYear(Long BusinessID, Integer year,
			AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateExportToken(AsyncCallback<String> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void generatePDFToken(AsyncCallback<String> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getStats(Long businessID,
			AsyncCallback<BusinessStatsDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTotalAfterTaxesForYear(Long businessID, Integer year,
			AsyncCallback<BigDecimal> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(BusinessDTO businessDTO, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCreditNotes(Long businessID,
			AsyncCallback<List<CreditNoteDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getEstimations(Long businessID,
			AsyncCallback<List<EstimationDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getInvoices(Long businessID,
			AsyncCallback<List<InvoiceDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTransportDocuments(Long businessID,
			AsyncCallback<List<TransportDocumentDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getClients(Long businessID,
			AsyncCallback<List<ClientDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(Long businessID, AsyncCallback<BusinessDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNotesBitMask(Long notesBitMask,
			AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateLogoOpToken(AsyncCallback<String> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPaymentTypes(Long businessID,
			AsyncCallback<List<PaymentTypeDTO>> callback) {
		// TODO Auto-generated method stub

	}

}
