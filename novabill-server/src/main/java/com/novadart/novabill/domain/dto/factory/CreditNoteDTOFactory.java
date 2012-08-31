package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteDTOFactory extends AbstractInvoiceDTOFactory {
	
	public static CreditNoteDTO toDTO(CreditNote creditNote){
		if(creditNote == null)
			return null;
		CreditNoteDTO creditNoteDTO = new CreditNoteDTO(); 
		AbstractInvoiceDTOFactory.copyToDTO(creditNote, creditNoteDTO);
		creditNoteDTO.setBusiness(BusinessDTOFactory.toDTO(creditNote.getBusiness()));
		creditNoteDTO.setClient(ClientDTOFactory.toDTO(creditNote.getClient()));
		return creditNoteDTO;
	}

}
