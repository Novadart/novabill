package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceDTOFactory extends AbstractInvoiceDTOFactory {
	
	public static InvoiceDTO toDTO(Invoice invoice){
		if(invoice == null)
			return null;
		InvoiceDTO invoiceDTO = new InvoiceDTO(); 
		AbstractInvoiceDTOFactory.copyToDTO(invoice, invoiceDTO);
		invoiceDTO.setPaymentTypeName(invoice.getPaymentTypeName());
		invoiceDTO.setPaymentDateGenerator(invoice.getPaymentDateGenerator());
		invoiceDTO.setPaymentDateDelta(invoice.getPaymentDateDelta());
		invoiceDTO.setBusiness(BusinessDTOFactory.toDTO(invoice.getBusiness()));
		invoiceDTO.setClient(ClientDTOFactory.toDTO(invoice.getClient()));
		return invoiceDTO;
	}
	
	public static void copyFromDTO(Invoice invoice, InvoiceDTO invoiceDTO, boolean addItems){
		AbstractInvoiceDTOFactory.copyFromDTO(invoice, invoiceDTO, addItems);
		invoice.setPaymentTypeName(invoiceDTO.getPaymentTypeName());
		invoice.setPaymentDateGenerator(invoiceDTO.getPaymentDateGenerator());
		invoice.setPaymentDateDelta(invoiceDTO.getPaymentDateDelta());
	}
	
}
