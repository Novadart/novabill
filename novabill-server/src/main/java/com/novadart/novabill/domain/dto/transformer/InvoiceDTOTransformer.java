package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceDTOTransformer extends AbstractInvoiceDTOTransformer {
	
	public static InvoiceDTO toDTO(Invoice invoice, boolean copyItems){
		if(invoice == null)
			return null;
		InvoiceDTO invoiceDTO = new InvoiceDTO(); 
		AbstractInvoiceDTOTransformer.copyToDTO(invoice, invoiceDTO, copyItems);
		invoiceDTO.setPaymentTypeName(invoice.getPaymentTypeName());
		invoiceDTO.setPaymentDateGenerator(invoice.getPaymentDateGenerator());
		invoiceDTO.setPaymentDateDelta(invoice.getPaymentDateDelta());
		invoiceDTO.setPaymentDeltaType(invoice.getPaymentDeltaType());
		invoiceDTO.setSecondaryPaymentDateDelta(invoice.getSecondaryPaymentDateDelta());
		invoiceDTO.setCreatedFromTransportDocuments(invoice.isCreatedFromTransportDocuments());
		invoiceDTO.setBusiness(BusinessDTOTransformer.toDTO(invoice.getBusiness()));
		invoiceDTO.setClient(ClientDTOTransformer.toDTO(invoice.getClient()));
		invoiceDTO.setSeenByClientTime(invoice.getSeenByClientTime());
		invoiceDTO.setEmailedToClient(invoice.getEmailedToClient());
		invoiceDTO.setSplitPayment(invoice.isSplitPayment());
		invoiceDTO.setWitholdTax(invoice.isWitholdTax());
		invoiceDTO.setWitholdTaxPercentFirstLevel(invoice.getWitholdTaxPercentFirstLevel());
		invoiceDTO.setWitholdTaxPercentSecondLevel(invoice.getWitholdTaxPercentSecondLevel());
		invoiceDTO.setPensionContribution(invoice.isPensionContribution());
		invoiceDTO.setPensionContributionPercent(invoice.getPensionContributionPercent());
		invoiceDTO.setWitholdTaxTotal(invoice.getWitholdTaxTotal());
		invoiceDTO.setPensionContributionTotal(invoice.getPensionContributionTotal());
		return invoiceDTO;
	}
	
	public static void copyFromDTO(Invoice invoice, InvoiceDTO invoiceDTO, boolean addItems){
		AbstractInvoiceDTOTransformer.copyFromDTO(invoice, invoiceDTO, addItems);
		invoice.setPaymentTypeName(invoiceDTO.getPaymentTypeName());
		invoice.setPaymentDateGenerator(invoiceDTO.getPaymentDateGenerator());
		invoice.setPaymentDateDelta(invoiceDTO.getPaymentDateDelta());
		invoice.setPaymentDeltaType(invoiceDTO.getPaymentDeltaType());
		invoice.setSecondaryPaymentDateDelta(invoiceDTO.getSecondaryPaymentDateDelta());
		invoice.setCreatedFromTransportDocuments(invoiceDTO.isCreatedFromTransportDocuments());
		invoice.setSplitPayment(invoiceDTO.isSplitPayment());
		invoice.setWitholdTax(invoiceDTO.isWitholdTax());
		invoice.setWitholdTaxPercentFirstLevel(invoiceDTO.getWitholdTaxPercentFirstLevel());
		invoice.setWitholdTaxPercentSecondLevel(invoiceDTO.getWitholdTaxPercentSecondLevel());
		invoice.setPensionContribution(invoiceDTO.isPensionContribution());
		invoice.setPensionContributionPercent(invoiceDTO.getPensionContributionPercent());
		invoice.setWitholdTaxTotal(invoiceDTO.getWitholdTaxTotal());
		invoice.setPensionContributionTotal(invoiceDTO.getPensionContributionTotal());
	}
	
}
