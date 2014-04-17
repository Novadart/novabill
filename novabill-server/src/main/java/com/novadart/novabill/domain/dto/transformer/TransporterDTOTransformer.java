package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.shared.client.dto.TransporterDTO;

public class TransporterDTOTransformer {

	public static TransporterDTO toDTO(Transporter transporter){
		if(transporter == null)
			return null;
		TransporterDTO transporterDTO = new TransporterDTO();
		transporterDTO.setId(transporter.getId());
		transporterDTO.setDescription(transporter.getDescription());
		return transporterDTO;
	}
	
	public static void copyFromDTO(Transporter transporter, TransporterDTO transporterDTO) {
		if(transporter == null || transporterDTO == null)
			return;
		transporter.setDescription(transporterDTO.getDescription());
	}
	
}
