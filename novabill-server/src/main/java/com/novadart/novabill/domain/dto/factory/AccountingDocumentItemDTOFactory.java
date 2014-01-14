package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;


public class AccountingDocumentItemDTOFactory {
	
	public static AccountingDocumentItemDTO toDTO(AccountingDocumentItem item){
		if(item == null)
			return null;
		AccountingDocumentItemDTO itemDTO = new AccountingDocumentItemDTO();
		itemDTO.setId(item.getId());
		itemDTO.setPrice(item.getPrice());
		itemDTO.setDescription(item.getDescription());
		itemDTO.setQuantity(item.getQuantity());
		itemDTO.setUnitOfMeasure(item.getUnitOfMeasure());
		itemDTO.setTax(item.getTax());
		itemDTO.setTotalBeforeTax(item.getTotalBeforeTax());
		itemDTO.setTotalTax(item.getTotalTax());
		itemDTO.setTotal(item.getTotal());
		itemDTO.setDiscount(item.getDiscount());
		itemDTO.setSku(item.getSku());
		return itemDTO;
	}
	
	public static void copyFromDTO(AccountingDocumentItem accountingDocumentItem, AccountingDocumentItemDTO accountingDocumentItemDTO){
		accountingDocumentItem.setPrice(accountingDocumentItemDTO.getPrice());
		accountingDocumentItem.setDescription(accountingDocumentItemDTO.getDescription());
		accountingDocumentItem.setQuantity(accountingDocumentItemDTO.getQuantity());
		accountingDocumentItem.setUnitOfMeasure(accountingDocumentItemDTO.getUnitOfMeasure());
		accountingDocumentItem.setTax(accountingDocumentItemDTO.getTax());
		accountingDocumentItem.setTotalBeforeTax(accountingDocumentItemDTO.getTotalBeforeTax());
		accountingDocumentItem.setTotalTax(accountingDocumentItemDTO.getTotalTax());
		accountingDocumentItem.setTotal(accountingDocumentItemDTO.getTotal());
		accountingDocumentItem.setDiscount(accountingDocumentItemDTO.getDiscount());
		accountingDocumentItem.setSku(accountingDocumentItemDTO.getSku());
	}

}
