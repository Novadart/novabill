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
		transportDocumentDTO.setNumberOfPackages(transportDocument.getNumberOfPackages());
		transportDocumentDTO.setFromLocation(EndpointDTOFactory.toDTO(transportDocument.getFromLocation()));
		transportDocumentDTO.setToLocation(EndpointDTOFactory.toDTO(transportDocument.getToLocation()));
		transportDocumentDTO.setTransporter(transportDocument.getTransporter());
		transportDocumentDTO.setTransportationResponsibility(transportDocument.getTransportationResponsibility());
		transportDocumentDTO.setTradeZone(transportDocument.getTradeZone());
		return transportDocumentDTO; 
	}
	
	public static void copyFromDTO(TransportDocument transportDocument, TransportDocumentDTO transportDocumentDTO, boolean addItems){
		AccountingDocumentDTOFactory.copyFromDTO(transportDocument, transportDocumentDTO, addItems);
		transportDocument.setNumberOfPackages(transportDocumentDTO.getNumberOfPackages());
		EndpointDTOFactory.copyFromDTO(transportDocument.getFromLocation(), transportDocumentDTO.getFromLocation());
		EndpointDTOFactory.copyFromDTO(transportDocument.getToLocation(), transportDocumentDTO.getToLocation());
		transportDocument.setTransporter(transportDocumentDTO.getTransporter());
		transportDocument.setTransportationResponsibility(transportDocumentDTO.getTransportationResponsibility());
		transportDocument.setTradeZone(transportDocumentDTO.getTradeZone());
	}

}
