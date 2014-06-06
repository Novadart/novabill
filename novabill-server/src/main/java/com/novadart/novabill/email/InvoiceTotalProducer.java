package com.novadart.novabill.email;

import java.util.Locale;
import java.util.Map;

import com.novadart.novabill.domain.Invoice;

public class InvoiceTotalProducer implements ValueProducer {

	@Override
	public String produceValue(Map<String, Object> context) {
		Invoice invoice = (Invoice)context.get(EmailFormatter.INVOICE_CONTEXT_PARAMETER_NAME);
		return java.text.NumberFormat.getCurrencyInstance(Locale.ITALY).format(invoice.getTotal().doubleValue());
	}

}
