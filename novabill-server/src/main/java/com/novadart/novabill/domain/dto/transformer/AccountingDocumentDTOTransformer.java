package com.novadart.novabill.domain.dto.transformer;

import java.util.ArrayList;
import java.util.List;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Endpoint;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public abstract class AccountingDocumentDTOTransformer {
	
	protected static void copyToDTO(AccountingDocument accountingDocument, AccountingDocumentDTO accountingDocumentDTO, boolean copyItems) {
		accountingDocumentDTO.setId(accountingDocument.getId());
		accountingDocumentDTO.setDocumentID(accountingDocument.getDocumentID());
		accountingDocumentDTO.setAccountingDocumentDate(accountingDocument.getAccountingDocumentDate());
		accountingDocumentDTO.setNote(accountingDocument.getNote());
		accountingDocumentDTO.setLayoutType(accountingDocument.getLayoutType());
		if(copyItems){
			List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(accountingDocument.getAccountingDocumentItems().size());
			for(AccountingDocumentItem item: accountingDocument.getAccountingDocumentItems())
				items.add(AccountingDocumentItemDTOTransformer.toDTO(item));
			accountingDocumentDTO.setItems(items);
		}
		accountingDocumentDTO.setTotal(accountingDocument.getTotal());
		accountingDocumentDTO.setTotalTax(accountingDocument.getTotalTax());
		accountingDocumentDTO.setTotalBeforeTax(accountingDocument.getTotalBeforeTax());
		accountingDocumentDTO.setPaymentNote(accountingDocument.getPaymentNote());
		if(accountingDocument.getToEndpoint() != null)
			accountingDocumentDTO.setToEndpoint(EndpointDTOTransformer.toDTO(accountingDocument.getToEndpoint()));
	}
	
	public static void copyFromDTO(AccountingDocument accountingDocument, AccountingDocumentDTO accountingDocumentDTO, boolean addItems){
		accountingDocument.setDocumentID(accountingDocumentDTO.getDocumentID());
		accountingDocument.setAccountingDocumentDate(accountingDocumentDTO.getAccountingDocumentDate());
		accountingDocument.setNote(accountingDocumentDTO.getNote());
		accountingDocument.setLayoutType(accountingDocumentDTO.getLayoutType());
		if(addItems){
			for(AccountingDocumentItemDTO itemDTO: accountingDocumentDTO.getItems()){
				AccountingDocumentItem item = new AccountingDocumentItem();
				AccountingDocumentItemDTOTransformer.copyFromDTO(item, itemDTO);
				item.setAccountingDocument(accountingDocument);
				accountingDocument.getAccountingDocumentItems().add(item);
			}
		}
		accountingDocument.setTotal(accountingDocumentDTO.getTotal());
		accountingDocument.setTotalTax(accountingDocumentDTO.getTotalTax());
		accountingDocument.setTotalBeforeTax(accountingDocumentDTO.getTotalBeforeTax());
		accountingDocument.setPaymentNote(accountingDocumentDTO.getPaymentNote());
		if(accountingDocumentDTO.getToEndpoint() != null){
			if(accountingDocument.getToEndpoint() == null)
				accountingDocument.setToEndpoint(new Endpoint());
			EndpointDTOTransformer.copyFromDTO(accountingDocument.getToEndpoint(), accountingDocumentDTO.getToEndpoint());
		}
	}

}
