package com.novadart.novabill.frontend.client.demo.facade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.Configuration;
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
		callback.onSuccess(Long.valueOf(Data.getClients().values().size()));
	}

	@Override
	public void countInvoices(Long businessID, AsyncCallback<Long> callback) {
		callback.onSuccess(Long.valueOf(Data.getInvoices().values().size()));
	}

	@Override
	public void countInvoicesForYear(Long BusinessID, Integer year,
			AsyncCallback<Long> callback) {
		callback.onSuccess(Long.valueOf(Data.getInvoices().values().size()));
	}

	@Override
	public void generateExportToken(AsyncCallback<String> callback) {
		callback.onSuccess("token");
	}

	@Override
	public void generatePDFToken(AsyncCallback<String> callback) {
		callback.onSuccess("token");
	}

	@Override
	public void getStats(Long businessID,
			AsyncCallback<BusinessStatsDTO> callback) {
		
		BusinessStatsDTO bs = new BusinessStatsDTO();
		bs.setClientsCount(Long.valueOf(Data.getClients().size()));
		bs.setInvoicesCountForYear(Long.valueOf(Data.getInvoices().size()));
		
		BigDecimal total = BigDecimal.ZERO;
		
		for (List<InvoiceDTO> il : Data.getInvoices().values()) {
			for (InvoiceDTO i : il) {
				total = total.add(i.getTotal());
			}
		}
		
		bs.setTotalAfterTaxesForYear(total);
		callback.onSuccess(bs);
	}

	@Override
	public void getTotalAfterTaxesForYear(Long businessID, Integer year,
			AsyncCallback<BigDecimal> callback) {
		BigDecimal total = BigDecimal.ZERO;
		
		for (List<InvoiceDTO> il : Data.getInvoices().values()) {
			for (InvoiceDTO i : il) {
				total = total.add(i.getTotal());
			}
		}
		callback.onSuccess(total);
	}

	@Override
	public void update(BusinessDTO businessDTO, AsyncCallback<Void> callback) {
		Configuration.setBusiness(businessDTO);
		Data.setBusiness(businessDTO);
		callback.onSuccess(null);
	}

	@Override
	public void getCreditNotes(Long businessID,	AsyncCallback<List<CreditNoteDTO>> callback) {
		List<CreditNoteDTO> result = new ArrayList<CreditNoteDTO>();
		for (List<CreditNoteDTO> partial : Data.getCreditNotes().values()) {
			result.addAll(partial);
		}
		callback.onSuccess(result);
	}

	@Override
	public void getEstimations(Long businessID,
			AsyncCallback<List<EstimationDTO>> callback) {
		List<EstimationDTO> result = new ArrayList<EstimationDTO>();
		for (List<EstimationDTO> partial : Data.getEstimations().values()) {
			result.addAll(partial);
		}
		callback.onSuccess(result);
	}

	@Override
	public void getInvoices(Long businessID,
			AsyncCallback<List<InvoiceDTO>> callback) {
		List<InvoiceDTO> result = new ArrayList<InvoiceDTO>();
		for (List<InvoiceDTO> partial : Data.getInvoices().values()) {
			result.addAll(partial);
		}
		callback.onSuccess(result);
	}

	@Override
	public void getTransportDocuments(Long businessID,
			AsyncCallback<List<TransportDocumentDTO>> callback) {
		List<TransportDocumentDTO> result = new ArrayList<TransportDocumentDTO>();
		for (List<TransportDocumentDTO> partial : Data.getTransportDocs().values()) {
			result.addAll(partial);
		}
		callback.onSuccess(result);
	}

	@Override
	public void getClients(Long businessID,
			AsyncCallback<List<ClientDTO>> callback) {
		callback.onSuccess(new ArrayList<ClientDTO>(Data.getClients().values()));
	}

	@Override
	public void get(Long businessID, AsyncCallback<BusinessDTO> callback) {
		callback.onSuccess(Data.getBusiness());
	}

	@Override
	public void updateNotesBitMask(Long notesBitMask,
			AsyncCallback<Long> callback) {
		callback.onSuccess(notesBitMask);
	}

	@Override
	public void generateLogoOpToken(AsyncCallback<String> callback) {
		callback.onSuccess("token");
	}

	@Override
	public void getPaymentTypes(Long businessID, AsyncCallback<List<PaymentTypeDTO>> callback) {
		callback.onSuccess(new ArrayList<PaymentTypeDTO>(Data.getPayments().values()));
	}

}
