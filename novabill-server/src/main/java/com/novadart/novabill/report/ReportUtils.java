package com.novadart.novabill.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.novadart.novabill.domain.TransportDocument;

public class ReportUtils {
	
	public static String removeTrailingZeros(BigDecimal decimal, Locale locale){
		DecimalFormat format = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		format.applyPattern("0.#");
		return format.format(decimal.doubleValue());
	}
	
	public static List<TransportDetails> createTransportDetailsDataSource(TransportDocument transDoc){
		TransportDetails tranDetails = new TransportDetails(transDoc);
		List<TransportDetails> result = new ArrayList<>(1);
		result.add(tranDetails);
		return result;
	}

}
