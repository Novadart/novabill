package com.novadart.novabill.domain;

import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;


public class InvoiceItemDTOFactory {
	
	public static InvoiceItemDTO toDTO(InvoiceItem item){
		if(item == null)
			return null;
		InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO();
		invoiceItemDTO.setId(item.getId());
		invoiceItemDTO.setPrice(item.getPrice());
		invoiceItemDTO.setDescription(item.getDescription());
		invoiceItemDTO.setQuantity(item.getQuantity());
		invoiceItemDTO.setUnitOfMeasure(item.getUnitOfMeasure());
		invoiceItemDTO.setTax(item.getTax());
		invoiceItemDTO.setTotalBeforeTax(item.getTotalBeforeTax());
		invoiceItemDTO.setTotalTax(item.getTotalTax());
		invoiceItemDTO.setTotal(item.getTotal());
		return invoiceItemDTO;
	}
	
	public static void copyFromDTO(InvoiceItem invoiceItem, InvoiceItemDTO invoiceItemDTO){
		invoiceItem.setPrice(invoiceItemDTO.getPrice());
		invoiceItem.setDescription(invoiceItemDTO.getDescription());
		invoiceItem.setQuantity(invoiceItemDTO.getQuantity());
		invoiceItem.setUnitOfMeasure(invoiceItemDTO.getUnitOfMeasure());
		invoiceItem.setTax(invoiceItemDTO.getTax());
		invoiceItem.setTotalBeforeTax(invoiceItemDTO.getTotalBeforeTax());
		invoiceItem.setTotalTax(invoiceItemDTO.getTotalTax());
		invoiceItem.setTotal(invoiceItemDTO.getTotal());
	}

}
