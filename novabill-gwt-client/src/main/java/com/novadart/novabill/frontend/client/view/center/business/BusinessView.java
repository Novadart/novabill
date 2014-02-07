package com.novadart.novabill.frontend.client.view.center.business;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Image;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextArea;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.validation.AlternativeSsnVatIdValidation;

public interface BusinessView extends View<BusinessView.Presenter>, HasUILocking {

	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
		void onLoad();
		
		void onUpdateLogoClicked();
		
		void onRemoveLogoClicked();
		
		void onExportClientDataClicked();
		
		void onExportInvoiceDataClicked();
		
		void onExportEstimationDataClicked();
		
		void onExportCreditNoteDataClicked();
		
		void onExportTransportDocumentDataClicked();
		
		void onSaveDataClicked();
		
		void onLogoSubmit();
		
		void onLogoSubmitComplete(String result);
	}

	Button getUpdateLogo();

	AlternativeSsnVatIdValidation getSsnOrVatIdValidation();

	Button getExportTransportDocumentData();

	Button getExportCreditNoteData();

	Button getExportEstimationData();

	Button getExportInvoiceData();

	Button getExportClientData();

	LoaderButton getSaveData();

	ValidatedTextBox getWeb();

	ValidatedTextBox getFax();

	ValidatedTextBox getMobile();

	ValidatedTextBox getEmail();

	ValidatedTextBox getPhone();

	ValidatedTextBox getPostcode();

	ValidatedListBox getCountry();

	ValidatedListBox getProvince();

	ValidatedTextBox getCity();

	ValidatedTextBox getAddress();

	ValidatedTextBox getVatID();

	ValidatedTextBox getSsn();

	ValidatedTextArea getName();

	InlineNotification getInlineNotification();

	Image getLogo();

	FormPanel getFormPanel();

	Button getRemoveLogo();

	Anchor getDeleteAccount();

	CheckBox getDiscountInDocsExplicit();

	CheckBox getIncognitoEnabled();

}
