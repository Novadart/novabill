package com.novadart.novabill.frontend.client.widget.payment;

import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaidInFull extends PaymentTypeDTO {
	
	private static final PaymentTypeDTO instance = new PaidInFull();
	
	public static PaymentTypeDTO instance() {
		return instance;
	}
	
	private PaidInFull() {
		setBusiness(Configuration.getBusiness());
		setDefaultPaymentNote(I18N.INSTANCE.payed());
		setId(null);
		setName("* "+I18N.INSTANCE.payed()+" *");
		setPaymentDateDelta(0);
		setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
		setSecondaryPaymentDateDelta(null);
	}

}
