package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.AbstractInvoice;
import com.novadart.novabill.shared.client.dto.AbstractInvoiceDTO;

public class AbstractInvoiceDTOTransformer extends AccountingDocumentDTOTransformer {
	
	protected static void copyToDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO, boolean copyItems){
		AccountingDocumentDTOTransformer.copyToDTO(abstractInvoice, abstractInvoiceDTO, copyItems);
		abstractInvoiceDTO.setPaymentDueDate(abstractInvoice.getPaymentDueDate());
		abstractInvoiceDTO.setPayed(abstractInvoice.getPayed());
	}

	public static void copyFromDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO, boolean addItems){
		AccountingDocumentDTOTransformer.copyFromDTO(abstractInvoice, abstractInvoiceDTO, addItems);
		abstractInvoice.setPaymentDueDate(abstractInvoiceDTO.getPaymentDueDate());
		abstractInvoice.setPayed(abstractInvoiceDTO.getPayed());
	}
	
}
