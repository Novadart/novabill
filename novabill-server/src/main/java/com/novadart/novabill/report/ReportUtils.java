package com.novadart.novabill.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ReportUtils {
	
	public static String removeTrailingZeros(BigDecimal decimal, Locale locale){
		DecimalFormat format = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		format.applyPattern("0.#");
		return format.format(decimal.doubleValue());
	}

}
