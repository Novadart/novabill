package com.novadart.novabill.domain;

import java.util.ArrayList;
import java.util.List;
import com.novadart.novabill.shared.client.dto.AbstractInvoiceDTO;
import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;

public abstract class AbstractInvoiceDTOFactory {
	
	protected static void copyToDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO) {
		abstractInvoiceDTO.setId(abstractInvoice.getId());
		abstractInvoiceDTO.setInvoiceID(abstractInvoice.getInvoiceID());
		abstractInvoiceDTO.setInvoiceDate(abstractInvoice.getInvoiceDate());
		abstractInvoiceDTO.setNote(abstractInvoice.getNote());
		List<InvoiceItemDTO> items = new ArrayList<InvoiceItemDTO>(abstractInvoice.getInvoiceItems().size());
		for(InvoiceItem item: abstractInvoice.getInvoiceItems())
			items.add(InvoiceItemDTOFactory.toDTO(item));
		abstractInvoiceDTO.setItems(items);
		abstractInvoiceDTO.setTotal(abstractInvoice.getTotal());
		abstractInvoiceDTO.setTotalTax(abstractInvoice.getTotalTax());
		abstractInvoiceDTO.setTotalBeforeTax(abstractInvoice.getTotalBeforeTax());
	}
	
	public static void copyFromDTO(AbstractInvoice abstractInvoice, AbstractInvoiceDTO abstractInvoiceDTO, boolean copyItems){
		abstractInvoice.setInvoiceID(abstractInvoiceDTO.getInvoiceID());
		abstractInvoice.setInvoiceDate(abstractInvoiceDTO.getInvoiceDate());
		abstractInvoice.setNote(abstractInvoiceDTO.getNote());
		if(copyItems){
			for(InvoiceItemDTO itemDTO: abstractInvoiceDTO.getItems()){
				InvoiceItem invoiceItem = new InvoiceItem();
				InvoiceItemDTOFactory.copyFromDTO(invoiceItem, itemDTO);
				invoiceItem.setInvoice(abstractInvoice);
				abstractInvoice.getInvoiceItems().add(invoiceItem);
			}
		}
		abstractInvoice.setTotal(abstractInvoiceDTO.getTotal());
		abstractInvoice.setTotalTax(abstractInvoiceDTO.getTotalTax());
		abstractInvoice.setTotalBeforeTax(abstractInvoiceDTO.getTotalBeforeTax());
	}

}
