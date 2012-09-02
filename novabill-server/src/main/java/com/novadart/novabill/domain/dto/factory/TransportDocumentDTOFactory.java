package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentDTOFactory extends AccountingDocumentDTOFactory {
	
	public static TransportDocumentDTO toDTO(TransportDocument transportDocument) {
		if(transportDocument == null)
			return null;
		TransportDocumentDTO transportDocumentDTO = new TransportDocumentDTO();
		AccountingDocumentDTOFactory.copyToDTO(transportDocument, transportDocumentDTO);
		transportDocumentDTO.setBusiness(BusinessDTOFactory.toDTO(transportDocument.getBusiness()));
		transportDocumentDTO.setClient(ClientDTOFactory.toDTO(transportDocument.getClient()));
		return transportDocumentDTO; 
	}

}
