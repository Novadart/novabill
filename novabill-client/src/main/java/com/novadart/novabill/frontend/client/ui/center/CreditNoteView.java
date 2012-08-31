package com.novadart.novabill.frontend.client.ui.center;

import com.novadart.novabill.frontend.client.ui.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public interface CreditNoteView extends View {

	public void setDataForNewCreditNote(ClientDTO client, Long progressiveId);
	
	public void setDataForNewCreditNote(Long progressiveId, InvoiceDTO invoice);
	
	public void setCreditNote(CreditNoteDTO creditNote);

}
