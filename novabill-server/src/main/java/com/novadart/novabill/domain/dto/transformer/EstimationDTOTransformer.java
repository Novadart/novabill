package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class EstimationDTOTransformer extends AccountingDocumentDTOTransformer {
	
	public static EstimationDTO toDTO(Estimation estimation, boolean copyItems) {
		if(estimation == null)
			return null;
		EstimationDTO estimationDTO = new EstimationDTO();
		AccountingDocumentDTOTransformer.copyToDTO(estimation, estimationDTO, copyItems);
		estimationDTO.setLimitations(estimation.getLimitations());
		estimationDTO.setValidTill(estimation.getValidTill());
		estimationDTO.setIncognito(estimation.isIncognito());
		estimationDTO.setBusiness(BusinessDTOTransformer.toDTO(estimation.getBusiness()));
		estimationDTO.setClient(ClientDTOTransformer.toDTO(estimation.getClient()));
		return estimationDTO; 
	}
	
	public static void copyFromDTO(Estimation estimation, EstimationDTO estimationDTO, boolean addItems){
		AccountingDocumentDTOTransformer.copyFromDTO(estimation, estimationDTO, addItems);
		estimation.setLimitations(estimationDTO.getLimitations());
		estimation.setValidTill(estimationDTO.getValidTill());
		estimation.setIncognito(estimationDTO.isIncognito());
	}
	
	public static InvoiceDTO toInvoiceDTO(EstimationDTO estimationDTO, boolean copyItems){
		AccountingDocument accountingDocument = new Estimation();
		AccountingDocumentDTOTransformer.copyFromDTO(accountingDocument, estimationDTO, true);
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		AccountingDocumentDTOTransformer.copyToDTO(accountingDocument, invoiceDTO, copyItems);
		invoiceDTO.setBusiness(estimationDTO.getBusiness());
		invoiceDTO.setClient(estimationDTO.getClient());
		return invoiceDTO;
	}

}
