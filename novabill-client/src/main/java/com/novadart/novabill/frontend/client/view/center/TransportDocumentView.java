package com.novadart.novabill.frontend.client.view.center;

import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public interface TransportDocumentView extends View {
	
	public void setDataForNewTransportDocument(ClientDTO client, Long transportDocumentProgressiveId);
	
	public void setDataForNewTransportDocument(ClientDTO client, Long transportDocumentProgressiveId, TransportDocumentDTO document);
	
	public void setTransportDocument(TransportDocumentDTO document);
}
