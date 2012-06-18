package com.novadart.novabill.domain;

import java.util.ArrayList;
import java.util.List;

import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;

public abstract class AccountingDocumentDTOFactory {
	
	protected static void copyToDTO(AccountingDocument accountingDocument, AccountingDocumentDTO accountingDocumentDTO) {
		accountingDocumentDTO.setId(accountingDocument.getId());
		accountingDocumentDTO.setDocumentID(accountingDocument.getDocumentID());
		accountingDocumentDTO.setAccountingDocumentDate(accountingDocument.getAccountingDocumentDate());
		accountingDocumentDTO.setNote(accountingDocument.getNote());
		List<InvoiceItemDTO> items = new ArrayList<InvoiceItemDTO>(accountingDocument.getInvoiceItems().size());
		for(InvoiceItem item: accountingDocument.getInvoiceItems())
			items.add(InvoiceItemDTOFactory.toDTO(item));
		accountingDocumentDTO.setItems(items);
		accountingDocumentDTO.setTotal(accountingDocument.getTotal());
		accountingDocumentDTO.setTotalTax(accountingDocument.getTotalTax());
		accountingDocumentDTO.setTotalBeforeTax(accountingDocument.getTotalBeforeTax());
	}
	
	public static void copyFromDTO(AccountingDocument accountDocument, AccountingDocumentDTO accountingDocumentDTO, boolean copyItems){
		accountDocument.setDocumentID(accountingDocumentDTO.getDocumentID());
		accountDocument.setAccountingDocumentDate(accountingDocumentDTO.getAccountingDocumentDate());
		accountDocument.setNote(accountingDocumentDTO.getNote());
		if(copyItems){
			for(InvoiceItemDTO itemDTO: accountingDocumentDTO.getItems()){
				InvoiceItem invoiceItem = new InvoiceItem();
				InvoiceItemDTOFactory.copyFromDTO(invoiceItem, itemDTO);
				invoiceItem.setInvoice(accountDocument);
				accountDocument.getInvoiceItems().add(invoiceItem);
			}
		}
		accountDocument.setTotal(accountingDocumentDTO.getTotal());
		accountDocument.setTotalTax(accountingDocumentDTO.getTotalTax());
		accountDocument.setTotalBeforeTax(accountingDocumentDTO.getTotalBeforeTax());
	}

}
