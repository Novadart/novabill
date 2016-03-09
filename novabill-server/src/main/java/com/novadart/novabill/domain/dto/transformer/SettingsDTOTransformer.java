package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.Settings;
import com.novadart.novabill.shared.client.dto.SettingsDTO;

public class SettingsDTOTransformer {

	public static SettingsDTO toDTO(Settings settings){
		SettingsDTO settingsDTO = new SettingsDTO();
		settingsDTO.setDefaultLayoutType(settings.getDefaultLayoutType());
		settingsDTO.setIncognitoEnabled(settings.isIncognitoEnabled());
		settingsDTO.setPriceDisplayInDocsMonolithic(settings.isPriceDisplayInDocsMonolithic());
		settingsDTO.setInvoiceFooterNote(settings.getInvoiceFooterNote());
		settingsDTO.setCreditNoteFooterNote(settings.getCreditNoteFooterNote());
		settingsDTO.setEstimationFooterNote(settings.getEstimationFooterNote());
		settingsDTO.setTransportDocumentFooterNote(settings.getTransportDocumentFooterNote());
		settingsDTO.setDefaultTermsAndConditionsForEstimation(settings.getDefaultTermsAndConditionsForEstimation());
		settingsDTO.setEmailSubject(settings.getEmailSubject());
		settingsDTO.setEmailText(settings.getEmailText());
		settingsDTO.setEmailReplyTo(settings.getEmailReplyTo());
		settingsDTO.setNonFreeAccountExpirationTime(settings.getNonFreeAccountExpirationTime());
		settingsDTO.setWitholdTax(settings.isWitholdTax());
		settingsDTO.setWitholdTaxPercentFirstLevel(settings.getWitholdTaxPercentFirstLevel());
		settingsDTO.setWitholdTaxPercentSecondLevel(settings.getWitholdTaxPercentSecondLevel());
		settingsDTO.setPensionContribution(settings.isPensionContribution());
		settingsDTO.setPensionContributionTax(settings.getPensionContributionPercent());
		return settingsDTO;
	}
	
	public static void copyFromDTO(Settings settings, SettingsDTO settingsDTO){
		settings.setIncognitoEnabled(settingsDTO.isIncognitoEnabled());
		settings.setPriceDisplayInDocsMonolithic(settingsDTO.isPriceDisplayInDocsMonolithic());
		settings.setInvoiceFooterNote(settingsDTO.getInvoiceFooterNote());
		settings.setCreditNoteFooterNote(settingsDTO.getCreditNoteFooterNote());
		settings.setEstimationFooterNote(settingsDTO.getEstimationFooterNote());
		settings.setTransportDocumentFooterNote(settingsDTO.getTransportDocumentFooterNote());
		settings.setDefaultTermsAndConditionsForEstimation(settingsDTO.getDefaultTermsAndConditionsForEstimation());
		settings.setEmailSubject(settingsDTO.getEmailSubject());
		settings.setEmailText(settingsDTO.getEmailText());
		settings.setEmailReplyTo(settingsDTO.getEmailReplyTo());
		settings.setWitholdTax(settingsDTO.isWitholdTax());
		settings.setWitholdTaxPercentFirstLevel(settingsDTO.getWitholdTaxPercentFirstLevel());
		settings.setWitholdTaxPercentSecondLevel(settingsDTO.getWitholdTaxPercentSecondLevel());
		settings.setPensionContribution(settingsDTO.isPensionContribution());
		settings.setPensionContributionPercent(settingsDTO.getPensionContributionTax());
	}
	
}
