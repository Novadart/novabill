package com.novadart.novabill.domain;

import com.novadart.novabill.shared.client.dto.AbstractInvoiceDTO;

public class AbstractInvoiceDTOFactory extends AccountingDocumentDTOFactory {
	
	protected static void copyToDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO){
		AccountingDocumentDTOFactory.copyToDTO(abstractInvoice, abstractInvoiceDTO);
		abstractInvoiceDTO.setPaymentType(abstractInvoice.getPaymentType());
		abstractInvoiceDTO.setPaymentNote(abstractInvoice.getPaymentNote());
		abstractInvoiceDTO.setPaymentDueDate(abstractInvoice.getPaymentDueDate());
		abstractInvoiceDTO.setPayed(abstractInvoice.getPayed());
	}

	public static void copyFromDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO, boolean copyItems){
		AccountingDocumentDTOFactory.copyFromDTO(abstractInvoice, abstractInvoiceDTO, copyItems);
		abstractInvoice.setPaymentType(abstractInvoiceDTO.getPaymentType());
		abstractInvoice.setPaymentNote(abstractInvoiceDTO.getPaymentNote());
		abstractInvoice.setPaymentDueDate(abstractInvoiceDTO.getPaymentDueDate());
		abstractInvoice.setPayed(abstractInvoiceDTO.getPayed());
	}
	
}
