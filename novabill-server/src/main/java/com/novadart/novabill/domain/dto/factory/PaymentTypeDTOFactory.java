package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentTypeDTOFactory {
	
	public static PaymentTypeDTO toDTO(PaymentType paymentType){
		if(paymentType == null)
			return null;
		PaymentTypeDTO paymentTypeDTO = new PaymentTypeDTO();
		paymentTypeDTO.setId(paymentType.getId());
		paymentTypeDTO.setName(paymentType.getName());
		paymentTypeDTO.setDefaultPaymentNote(paymentType.getDefaultPaymentNote());
		paymentTypeDTO.setPaymentDateGenerator(paymentType.getPaymentDateGenerator());
		paymentTypeDTO.setPaymentDateDelta(paymentType.getPaymentDateDelta());
		paymentTypeDTO.setPaymentDeltaType(paymentType.getPaymentDeltaType());
		paymentTypeDTO.setSecondaryPaymentDateDelta(paymentType.getSecondaryPaymentDateDelta());
		return paymentTypeDTO;
	}
	
	public static void copyFromDTO(PaymentType paymentType, PaymentTypeDTO paymentTypeDTO){
		if(paymentType == null || paymentTypeDTO == null)
			return;
		paymentType.setName(paymentTypeDTO.getName());
		paymentType.setDefaultPaymentNote(paymentTypeDTO.getDefaultPaymentNote());
		paymentType.setPaymentDateGenerator(paymentTypeDTO.getPaymentDateGenerator());
		paymentType.setPaymentDateDelta(paymentTypeDTO.getPaymentDateDelta());
		paymentType.setPaymentDeltaType(paymentTypeDTO.getPaymentDeltaType());
		paymentType.setSecondaryPaymentDateDelta(paymentTypeDTO.getSecondaryPaymentDateDelta());
	}

}
