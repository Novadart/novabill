package com.novadart.novabill.domain;

import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationDTOFactory extends AbstractInvoiceDTOFactory {
	
	public static EstimationDTO toDTO(Estimation estimation) {
		if(estimation == null)
			return null;
		EstimationDTO estimationDTO = new EstimationDTO();
		AbstractInvoiceDTOFactory.copyToDTO(estimation, estimationDTO);
		estimationDTO.setBusiness(BusinessDTOFactory.toDTO(estimation.getBusiness()));
		estimationDTO.setClient(ClientDTOFactory.toDTO(estimation.getClient()));
		return estimationDTO; 
	}

}
