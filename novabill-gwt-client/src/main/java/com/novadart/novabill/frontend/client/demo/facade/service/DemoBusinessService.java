package com.novadart.novabill.frontend.client.demo.facade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.LogRecordDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.facade.BusinessGwtServiceAsync;

public class DemoBusinessService implements BusinessGwtServiceAsync {


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
		
//		BusinessStatsDTO bs = new BusinessStatsDTO();
//		bs.setClientsCount(Long.valueOf(Data.getClients().size()));
//		bs.setInvoicesCountForYear(Data.countInvoices());
//		bs.setTotalAfterTaxesForYear(Data.calcTotal());
//		callback.onSuccess(bs);
	}

	@Override
	public void getTotalAfterTaxesForYear(Long businessID, Integer year,
			AsyncCallback<BigDecimal> callback) {
		callback.onSuccess(Data.calcTotal());
	}

	@Override
	public void update(BusinessDTO businessDTO, AsyncCallback<Void> callback) {
//		Configuration.setBusiness(businessDTO);
		Data.setBusiness(businessDTO);
		callback.onSuccess(null);
	}

	@Override
	public void getCreditNotes(Long businessID,	Integer year, AsyncCallback<List<CreditNoteDTO>> callback) {
		callback.onSuccess(Data.getAllDocs(CreditNoteDTO.class));
	}
	
	

	@Override
	public void getEstimations(Long businessID, Integer year,
			AsyncCallback<List<EstimationDTO>> callback) {
		callback.onSuccess(Data.getAllDocs(EstimationDTO.class));
	}

	@Override
	public void getInvoices(Long businessID, Integer year,
			AsyncCallback<List<InvoiceDTO>> callback) {
		callback.onSuccess(Data.getAllDocs(InvoiceDTO.class));
	}

	@Override
	public void getTransportDocuments(Long businessID, Integer year,
			AsyncCallback<List<TransportDocumentDTO>> callback) {
		callback.onSuccess(Data.getAllDocs(TransportDocumentDTO.class));
	}

	@Override
	public void getClients(Long businessID,
			AsyncCallback<List<ClientDTO>> callback) {
		callback.onSuccess(new ArrayList<ClientDTO>(Data.getClients()));
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
		callback.onSuccess(Data.getPayments());
	}

	@Override
	public void add(BusinessDTO businessDTO, AsyncCallback<Long> callback) {
//		Configuration.setBusiness(businessDTO);
		Data.setBusiness(businessDTO);
		callback.onSuccess(null);
	}

	@Override
	public void getCommodities(Long businessID,
			AsyncCallback<List<CommodityDTO>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void countClients(Long businessID, AsyncCallback<Integer> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void countInvoicesForYear(Long BusinessID, Integer year,
			AsyncCallback<Integer> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLogRecords(Long businessID, Integer numberOfDays,
			AsyncCallback<List<LogRecordDTO>> callback) {
		// TODO Auto-generated method stub
		
	}

}
