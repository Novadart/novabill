package com.novadart.novabill.frontend.client.view.center.estimation;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.AccountDocumentCss;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class EstimationViewImpl extends AccountDocument implements EstimationView {

	private static EstimationViewImplUiBinder uiBinder = GWT
			.create(EstimationViewImplUiBinder.class);

	interface EstimationViewImplUiBinder extends UiBinder<Widget, EstimationViewImpl> {
	}
	
	@UiField FlowPanel docControls;

	@UiField Label clientName;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField(provided=true) ValidatedDateBox date;
	@UiField(provided=true) ValidatedDateBox validTill;
	@UiField ValidatedTextArea note;
	@UiField ValidatedTextArea paymentNote;
	@UiField ValidatedTextArea limitations;
	
	@UiField(provided=true) ItemInsertionForm itemInsertionForm;

	@UiField Label pdfOptionsLabel;
	@UiField CheckBox overrideIncognitoModeCheckbox;
	
	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;
	
	@UiField(provided=true) LoaderButton createEstimation;
	@UiField Button abort;
	
	
	private Presenter presenter;

	public EstimationViewImpl() {
		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {
			
			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				CalcUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}
			
		});
		
		number = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);

		date = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		validTill = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		validTill.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		createEstimation = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName(CSS.accountDocumentView());
		
		createEstimation.getButton().setStyleName(CSS.createButton()+" btn green");
		
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
	}
	
	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}
	
	@UiFactory
	AccountDocumentCss getAccountDocumentCss(){
		return CSS;
	}

	@Override
	public ValidatedTextBox getNumber() {
		return number;
	}

	@UiHandler("createEstimation")
	void onCreateEstimationClicked(ClickEvent e){
		presenter.onCreateDocumentClicked();
	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		presenter.onCancelClicked();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


	@Override
	public void reset() {
		overrideIncognitoModeCheckbox.setVisible(Configuration.getBusiness().isIncognitoEnabled());
		pdfOptionsLabel.setVisible(Configuration.getBusiness().isIncognitoEnabled());
		overrideIncognitoModeCheckbox.setValue(false);
		
		number.reset();

		//reset widget statuses
		date.reset();
		validTill.reset();

		//reset widget contents		
		note.setText("");
		paymentNote.setText("");
		limitations.setText("");
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		itemInsertionForm.reset();
		
		createEstimation.reset();
		setLocked(false);
	}

	@Override
	public void setLocked(boolean value) {
		number.setEnabled(!value);
		date.setEnabled(!value);
		validTill.setEnabled(!value);
		note.setEnabled(!value);
		paymentNote.setEnabled(!value);
		limitations.setEnabled(!value);
		
		itemInsertionForm.setLocked(value);
		overrideIncognitoModeCheckbox.setEnabled(!value);
		abort.setEnabled(!value);
	}

	@Override
	public Button getAbort() {
		return abort;
	}

	@Override
	public ValidatedDateBox getDate() {
		return date;
	}

	@Override
	public Label getClientName() {
		return clientName;
	}

	@Override
	public Label getTotalAfterTaxes() {
		return totalAfterTaxes;
	}

	@Override
	public Label getTotalTax() {
		return totalTax;
	}

	@Override
	public Label getTotalBeforeTaxes() {
		return totalBeforeTaxes;
	}

	@Override
	public ItemInsertionForm getItemInsertionForm() {
		return itemInsertionForm;
	}

	@Override
	public LoaderButton getCreateDocument() {
		return createEstimation;
	}
	
	@Override
	public ValidatedTextArea getNote() {
		return note;
	}

	@Override
	public ValidatedDateBox getValidTill() {
		return validTill;
	}

	@Override
	public ValidatedTextArea getPaymentNote() {
		return paymentNote;
	}

	@Override
	public ValidatedTextArea getLimitations() {
		return limitations;
	}
	
	@Override
	public CheckBox getOverrideIncognitoModeCheckbox() {
		return overrideIncognitoModeCheckbox;
	}
}
