package com.novadart.novabill.frontend.client.util;

import com.google.gwt.i18n.client.DefaultLocalizedNames;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.LocalizedNames;

public class CountryUtils {

	private static final DefaultLocalizedNames DEFA_LOCALIZED_NAMES = new DefaultLocalizedNames();
	private static final LocalizedNames LOCALIZED_NAMES = LocaleInfo.getCurrentLocale().getLocalizedNames();

	public static String[] getLocalizedCountryCodes(){
		return LocaleInfo.getCurrentLocale().getLocalizedNames().getSortedRegionCodes();
	}

	public static String getRegionName(String countryCode){
		String regionName = LOCALIZED_NAMES.getRegionName(countryCode);
		if(regionName != null){
			return regionName;
		} else {
			return DEFA_LOCALIZED_NAMES.getRegionName(countryCode);
		}
	}

}
