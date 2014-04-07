package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.Settings;
import com.novadart.novabill.shared.client.dto.SettingsDTO;

public class SettingsDTOFactory {

	public static SettingsDTO toDTO(Settings settings){
		SettingsDTO settingsDTO = new SettingsDTO();
		settingsDTO.setDefaultLayoutType(settings.getDefaultLayoutType());
		settingsDTO.setIncognitoEnabled(settings.isIncognitoEnabled());
		settingsDTO.setPriceDisplayInDocsMonolithic(settings.isPriceDisplayInDocsMonolithic());
		settingsDTO.setInvoiceFooterNote(settings.getInvoiceFooterNote());
		settingsDTO.setCreditNoteFooterNote(settings.getCreditNoteFooterNote());
		settingsDTO.setEstimationFooterNote(settings.getEstimationFooterNote());
		settingsDTO.setTransportDocumentFooterNote(settings.getTransportDocumentFooterNote());
		return settingsDTO;
	}
	
	public static void copyFromDTO(Settings settings, SettingsDTO settingsDTO){
		settings.setIncognitoEnabled(settingsDTO.isIncognitoEnabled());
		settings.setPriceDisplayInDocsMonolithic(settingsDTO.isPriceDisplayInDocsMonolithic());
		settings.setInvoiceFooterNote(settingsDTO.getInvoiceFooterNote());
		settings.setCreditNoteFooterNote(settingsDTO.getCreditNoteFooterNote());
		settings.setEstimationFooterNote(settingsDTO.getEstimationFooterNote());
		settings.setTransportDocumentFooterNote(settingsDTO.getTransportDocumentFooterNote());
	}
	
}
