package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceDTOFactory extends AbstractInvoiceDTOFactory {
	
	public static InvoiceDTO toDTO(Invoice invoice, boolean copyItems){
		if(invoice == null)
			return null;
		InvoiceDTO invoiceDTO = new InvoiceDTO(); 
		AbstractInvoiceDTOFactory.copyToDTO(invoice, invoiceDTO, copyItems);
		invoiceDTO.setPaymentTypeName(invoice.getPaymentTypeName());
		invoiceDTO.setPaymentDateGenerator(invoice.getPaymentDateGenerator());
		invoiceDTO.setPaymentDateDelta(invoice.getPaymentDateDelta());
		invoiceDTO.setPaymentDeltaType(invoice.getPaymentDeltaType());
		invoiceDTO.setSecondaryPaymentDateDelta(invoice.getSecondaryPaymentDateDelta());
		invoiceDTO.setCreatedFromTransportDocuments(invoice.isCreatedFromTransportDocuments());
		invoiceDTO.setBusiness(BusinessDTOFactory.toDTO(invoice.getBusiness()));
		invoiceDTO.setClient(ClientDTOFactory.toDTO(invoice.getClient()));
		return invoiceDTO;
	}
	
	public static void copyFromDTO(Invoice invoice, InvoiceDTO invoiceDTO, boolean addItems){
		AbstractInvoiceDTOFactory.copyFromDTO(invoice, invoiceDTO, addItems);
		invoice.setPaymentTypeName(invoiceDTO.getPaymentTypeName());
		invoice.setPaymentDateGenerator(invoiceDTO.getPaymentDateGenerator());
		invoice.setPaymentDateDelta(invoiceDTO.getPaymentDateDelta());
		invoice.setPaymentDeltaType(invoiceDTO.getPaymentDeltaType());
		invoice.setSecondaryPaymentDateDelta(invoiceDTO.getSecondaryPaymentDateDelta());
		invoice.setCreatedFromTransportDocuments(invoiceDTO.isCreatedFromTransportDocuments());
	}
	
}
