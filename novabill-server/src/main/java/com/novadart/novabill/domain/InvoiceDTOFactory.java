package com.novadart.novabill.domain;

import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceDTOFactory extends AbstractInvoiceDTOFactory {
	
	public static InvoiceDTO toDTO(Invoice invoice){
		if(invoice == null)
			return null;
		InvoiceDTO invoiceDTO = new InvoiceDTO(); 
		AbstractInvoiceDTOFactory.copyToDTO(invoice, invoiceDTO);
		invoiceDTO.setBusiness(BusinessDTOFactory.toDTO(invoice.getBusiness()));
		invoiceDTO.setClient(ClientDTOFactory.toDTO(invoice.getClient()));
		invoiceDTO.setPaymentType(invoice.getPaymentType());
		invoiceDTO.setPaymentNote(invoice.getPaymentNote());
		invoiceDTO.setPaymentDueDate(invoice.getPaymentDueDate());
		return invoiceDTO;
	}
	
	public static void copyFromDTO(Invoice invoice, InvoiceDTO invoiceDTO, boolean copyItems){
		AbstractInvoiceDTOFactory.copyFromDTO(invoice, invoiceDTO, copyItems);
		invoice.setPaymentType(invoiceDTO.getPaymentType());
		invoice.setPaymentNote(invoiceDTO.getPaymentNote());
		invoice.setPaymentDueDate(invoiceDTO.getPaymentDueDate());
	}

}
