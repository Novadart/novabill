package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class EstimationDTOFactory extends AccountingDocumentDTOFactory {
	
	public static EstimationDTO toDTO(Estimation estimation) {
		if(estimation == null)
			return null;
		EstimationDTO estimationDTO = new EstimationDTO();
		AccountingDocumentDTOFactory.copyToDTO(estimation, estimationDTO);
		estimationDTO.setLimitations(estimation.getLimitations());
		estimationDTO.setValidTill(estimation.getValidTill());
		estimationDTO.setBusiness(BusinessDTOFactory.toDTO(estimation.getBusiness()));
		estimationDTO.setClient(ClientDTOFactory.toDTO(estimation.getClient()));
		return estimationDTO; 
	}
	
	public static void copyFromDTO(Estimation estimation, EstimationDTO estimationDTO, boolean addItems){
		AccountingDocumentDTOFactory.copyFromDTO(estimation, estimationDTO, addItems);
		estimation.setLimitations(estimationDTO.getLimitations());
		estimation.setValidTill(estimationDTO.getValidTill());
	}
	
	public static InvoiceDTO toInvoiceDTO(EstimationDTO estimationDTO){
		AccountingDocument accountingDocument = new Estimation();
		AccountingDocumentDTOFactory.copyFromDTO(accountingDocument, estimationDTO, true);
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		AccountingDocumentDTOFactory.copyToDTO(accountingDocument, invoiceDTO);
		invoiceDTO.setBusiness(estimationDTO.getBusiness());
		invoiceDTO.setClient(estimationDTO.getClient());
		return invoiceDTO;
	}

}
