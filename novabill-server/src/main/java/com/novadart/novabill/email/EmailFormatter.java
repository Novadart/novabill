package com.novadart.novabill.email;

import java.util.HashMap;
import java.util.Map;


public class EmailFormatter {

	public static final String INVOICE_CLIENT_NAME = "$NomeCliente";
	
	public static final String INVOICE_DATE = "$DataFattura";
	
	public static final String INVOICE_NUMBER = "$NumeroFattura";
	
	public static final String INVOICE_TOTAL = "$TotaleFattura";
	
	public static final String INVOICE_CONTEXT_PARAMETER_NAME = "invoice";
	
	
	@SuppressWarnings("serial")
	private final Map<String, ValueProducer> valueProducers = new HashMap<String, ValueProducer>(){{
		put(INVOICE_CLIENT_NAME, new InvoiceClientNameProducer());
		put(INVOICE_DATE, new InvoiceDateProducer());
		put(INVOICE_NUMBER, new InvoiceNumberProducer());
		put(INVOICE_TOTAL, new InvoiceTotalProducer());
	}};
	
	
	private static void replaceAll(StringBuilder builder, String from, String to){
		int index = builder.indexOf(from);
		while (index != -1)
	    {
	        builder.replace(index, index + from.length(), to);
	        index += to.length(); // Move to the end of the replacement
	        index = builder.indexOf(from, index);
	    }
	}
	
	public String format(String text, Map<String, Object> context){
		StringBuilder builder = new StringBuilder(text);
		for(String var: valueProducers.keySet())
			replaceAll(builder, var, valueProducers.get(var).produceValue(context));
		return builder.toString();
	}
	
}
