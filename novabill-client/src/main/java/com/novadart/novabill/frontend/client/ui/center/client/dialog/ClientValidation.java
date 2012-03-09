package com.novadart.novabill.frontend.client.ui.center.client.dialog;

import com.google.gwt.regexp.shared.RegExp;

public class ClientValidation {

	private static final RegExp VATID_REGEXP = RegExp.compile("\\d{11}");
	private static final RegExp SSN_REGEXP = RegExp.compile("[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]");
	
	
	public static boolean validateSSN(String ssn){
		return SSN_REGEXP.exec(ssn) != null;
	}
	
	public static boolean validateVATID(String vatid){
		return VATID_REGEXP.exec(vatid) != null;
	}
	
}
