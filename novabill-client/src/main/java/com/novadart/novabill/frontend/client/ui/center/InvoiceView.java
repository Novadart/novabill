package com.novadart.novabill.frontend.client.ui.center;

import com.novadart.novabill.frontend.client.ui.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public interface InvoiceView extends View {

	public void setDataForNewInvoice(ClientDTO client, Long progressiveId);
	
	public void setInvoice(InvoiceDTO invoice);
}
