package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.AbstractInvoice;
import com.novadart.novabill.shared.client.dto.AbstractInvoiceDTO;

public class AbstractInvoiceDTOFactory extends AccountingDocumentDTOFactory {
	
	protected static void copyToDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO){
		AccountingDocumentDTOFactory.copyToDTO(abstractInvoice, abstractInvoiceDTO);
		abstractInvoiceDTO.setPaymentDueDate(abstractInvoice.getPaymentDueDate());
		abstractInvoiceDTO.setPayed(abstractInvoice.getPayed());
		abstractInvoiceDTO.setPaymentDateGenerator(abstractInvoice.getPaymentDateGenerator());
		abstractInvoiceDTO.setPaymentDateDelta(abstractInvoice.getPaymentDateDelta());
	}

	public static void copyFromDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO, boolean addItems){
		AccountingDocumentDTOFactory.copyFromDTO(abstractInvoice, abstractInvoiceDTO, addItems);
		abstractInvoice.setPaymentDueDate(abstractInvoiceDTO.getPaymentDueDate());
		abstractInvoice.setPayed(abstractInvoiceDTO.getPayed());
		abstractInvoice.setPaymentDateGenerator(abstractInvoiceDTO.getPaymentDateGenerator());
		abstractInvoice.setPaymentDateDelta(abstractInvoiceDTO.getPaymentDateDelta());
	}
	
}
