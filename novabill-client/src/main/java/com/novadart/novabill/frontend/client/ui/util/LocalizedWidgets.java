package com.novadart.novabill.frontend.client.ui.util;

import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.util.CountryUtils;
import com.novadart.novabill.shared.client.data.Province;

public class LocalizedWidgets {
	
	public static ValidatedListBox createProvinceListBox(String defaultItem) {
		ValidatedListBox listBox = new ValidatedListBox(defaultItem, I18N.INSTANCE.notEmptyValidationError());
		for (Province p : Province.values()) {
			listBox.addItem(p.name());
		}
		return listBox;
	}

	
	public static ValidatedListBox createCountryListBox(String defaultItem) {
		ValidatedListBox listBox = new ValidatedListBox(defaultItem, I18N.INSTANCE.notEmptyValidationError());
		
		for (String cc : CountryUtils.getLocalizedCountryCodes()) {
			listBox.addItem(CountryUtils.getRegionName(cc), cc);
		}
		return listBox;
	}
	
}
