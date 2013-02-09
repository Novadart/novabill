package com.novadart.novabill.frontend.client.view.center.business;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Image;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
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
		
		void onLogoSubmitComplete(int resultCode);
	}

	public Button getUpdateLogo();

	public AlternativeSsnVatIdValidation getSsnOrVatIdValidation();

	public Button getExportTransportDocumentData();

	public Button getExportCreditNoteData();

	public Button getExportEstimationData();

	public Button getExportInvoiceData();

	public Button getExportClientData();

	public LoaderButton getSaveData();

	public ValidatedTextBox getWeb();

	public ValidatedTextBox getFax();

	public ValidatedTextBox getMobile();

	public ValidatedTextBox getEmail();

	public ValidatedTextBox getPhone();

	public ValidatedTextBox getPostcode();

	public ValidatedListBox getCountry();

	public ValidatedListBox getProvince();

	public ValidatedTextBox getCity();

	public ValidatedTextBox getAddress();

	public ValidatedTextBox getVatID();

	public ValidatedTextBox getSsn();

	public ValidatedTextBox getName();

	public InlineNotification getInlineNotification();

	public Image getLogo();

	public FormPanel getFormPanel();

	public Button getRemoveLogo();

}
