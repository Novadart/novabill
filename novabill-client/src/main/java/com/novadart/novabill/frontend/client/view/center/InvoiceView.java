package com.novadart.novabill.frontend.client.view.center;

import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public interface InvoiceView extends View {

	public void setDataForNewInvoice(ClientDTO client, Long progressiveId);
	
	public void setDataForNewInvoice(ClientDTO client, Long progressiveId, InvoiceDTO invoice);
	
	public void setDataForNewInvoice(Long progressiveId, EstimationDTO estimation);
	
	public void setDataForNewInvoice(Long progressiveId, TransportDocumentDTO transportDocument);
	
	public void setInvoice(InvoiceDTO invoice);

}
