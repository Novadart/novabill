package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;

public class SharingPermitDTOTransformer {
	
	public static SharingPermitDTO toDTO(SharingPermit sharingPermit) {
		if(sharingPermit == null)
			return null;
		SharingPermitDTO sharingPermitDTO = new SharingPermitDTO();
		sharingPermitDTO.setEmail(sharingPermit.getEmail());
		sharingPermitDTO.setDescription(sharingPermit.getDescription());
		sharingPermitDTO.setCreatedOn(sharingPermit.getCreatedOn());
		sharingPermitDTO.setId(sharingPermit.getId());
		return sharingPermitDTO;
	}

	public static void copyFromDTO(SharingPermit sharingPermit, SharingPermitDTO sharingPermitDTO) {
		if(sharingPermit == null || sharingPermitDTO == null)
			return;
		sharingPermit.setEmail(sharingPermitDTO.getEmail());
		sharingPermit.setDescription(sharingPermitDTO.getDescription());
		sharingPermit.setCreatedOn(sharingPermitDTO.getCreatedOn());
	}
	
}
