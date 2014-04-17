package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteDTOTransformer extends AbstractInvoiceDTOTransformer {
	
	public static CreditNoteDTO toDTO(CreditNote creditNote, boolean copyItems){
		if(creditNote == null)
			return null;
		CreditNoteDTO creditNoteDTO = new CreditNoteDTO(); 
		AbstractInvoiceDTOTransformer.copyToDTO(creditNote, creditNoteDTO, copyItems);
		creditNoteDTO.setBusiness(BusinessDTOTransformer.toDTO(creditNote.getBusiness()));
		creditNoteDTO.setClient(ClientDTOTransformer.toDTO(creditNote.getClient()));
		return creditNoteDTO;
	}

}
