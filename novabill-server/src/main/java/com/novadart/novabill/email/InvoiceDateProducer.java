package com.novadart.novabill.email;

import java.util.Map;

import com.novadart.novabill.domain.Invoice;

public class InvoiceDateProducer implements ValueProducer {

	@Override
	public String produceValue(Map<String, Object> context) {
		Invoice invoice = (Invoice)context.get(EmailFormatter.INVOICE_CONTEXT_PARAMETER_NAME);
		return new java.text.SimpleDateFormat("dd/MM/yyyy").format(invoice.getAccountingDocumentDate());
	}

}
