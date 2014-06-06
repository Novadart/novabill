package com.novadart.novabill.email;

import java.util.Map;

import com.novadart.novabill.domain.Invoice;

public class InvoiceClientNameProducer implements ValueProducer {

	@Override
	public String produceValue(Map<String, Object> context) {
		Invoice invoice = (Invoice)context.get(EmailFormatter.INVOICE_CONTEXT_PARAMETER_NAME);
		return invoice.getClient().getName();
	}

}
