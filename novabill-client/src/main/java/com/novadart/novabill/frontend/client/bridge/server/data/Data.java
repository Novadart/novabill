package com.novadart.novabill.frontend.client.bridge.server.data;

import java.util.List;

import com.novadart.novabill.shared.client.dto.IAccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class Data {
	
	public static IAutoBeanInvoiceDTO convert(InvoiceDTO invoice, List<IAccountingDocumentItemDTO> items) {
		IAutoBeanInvoiceDTO abi = new AutoBeanInvoiceDTO();
		
		abi.setAccountingDocumentDate(invoice.getAccountingDocumentDate());
		AccountingDocumentItemData aid = new AccountingDocumentItemData();
		aid.setItems(items);
		abi.setItems(aid);
		abi.setBusiness(invoice.getBusiness());
		abi.setClient(invoice.getClient());
		abi.setDocumentID(invoice.getDocumentID());
		abi.setId(invoice.getId());
		abi.setLayoutType(invoice.getLayoutType().name());
		abi.setNote(invoice.getNote());
		abi.setPayed(invoice.getPayed());
		abi.setPaymentDateDelta(invoice.getPaymentDateDelta());
		abi.setPaymentDateGenerator(invoice.getPaymentDateGenerator().name());
		abi.setPaymentDueDate(invoice.getPaymentDueDate());
		abi.setPaymentNote(invoice.getPaymentNote());
		abi.setPaymentTypeName(invoice.getPaymentTypeName());
		abi.setTotal(invoice.getTotal());
		abi.setTotalBeforeTax(invoice.getTotalBeforeTax());
		abi.setTotalTax(invoice.getTotalTax());
		
		return abi;
	}

}
