package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentDTOTransformer extends AccountingDocumentDTOTransformer {
	
	public static TransportDocumentDTO toDTO(TransportDocument transportDocument, boolean copyItems) {
		if(transportDocument == null)
			return null;
		TransportDocumentDTO transportDocumentDTO = new TransportDocumentDTO();
		AccountingDocumentDTOTransformer.copyToDTO(transportDocument, transportDocumentDTO, copyItems);
		transportDocumentDTO.setBusiness(BusinessDTOTransformer.toDTO(transportDocument.getBusiness()));
		transportDocumentDTO.setClient(ClientDTOTransformer.toDTO(transportDocument.getClient()));
		transportDocumentDTO.setNumberOfPackages(transportDocument.getNumberOfPackages());
		transportDocumentDTO.setFromEndpoint(EndpointDTOTransformer.toDTO(transportDocument.getFromEndpoint()));
		transportDocumentDTO.setTransporter(transportDocument.getTransporter());
		transportDocumentDTO.setTransportationResponsibility(transportDocument.getTransportationResponsibility());
		transportDocumentDTO.setTradeZone(transportDocument.getTradeZone());
		transportDocumentDTO.setTransportStartDate(transportDocument.getTransportStartDate());
		transportDocumentDTO.setCause(transportDocument.getCause());
		transportDocumentDTO.setInvoice(transportDocument.getInvoice() == null? null: transportDocument.getInvoice().getId());
		transportDocumentDTO.setTotalWeight(transportDocument.getTotalWeight());
		transportDocumentDTO.setAppearanceOfTheGoods(transportDocument.getAppearanceOfTheGoods());
		return transportDocumentDTO; 
	}
	
	public static void copyFromDTO(TransportDocument transportDocument, TransportDocumentDTO transportDocumentDTO, boolean addItems){
		AccountingDocumentDTOTransformer.copyFromDTO(transportDocument, transportDocumentDTO, addItems);
		transportDocument.setNumberOfPackages(transportDocumentDTO.getNumberOfPackages());
		EndpointDTOTransformer.copyFromDTO(transportDocument.getFromEndpoint(), transportDocumentDTO.getFromEndpoint());
		transportDocument.setTransporter(transportDocumentDTO.getTransporter());
		transportDocument.setTransportationResponsibility(transportDocumentDTO.getTransportationResponsibility());
		transportDocument.setTradeZone(transportDocumentDTO.getTradeZone());
		transportDocument.setTransportStartDate(transportDocumentDTO.getTransportStartDate());
		transportDocument.setCause(transportDocumentDTO.getCause());
		transportDocument.setTotalWeight(transportDocumentDTO.getTotalWeight());
		transportDocument.setAppearanceOfTheGoods(transportDocumentDTO.getAppearanceOfTheGoods());
	}

}
