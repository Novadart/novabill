package com.novadart.novabill.frontend.client.view.center.creditnote;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.AccountDocumentCss;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class CreditNoteViewImpl extends AccountDocument implements CreditNoteView {

	private static CreditNoteViewImplUiBinder uiBinder = GWT
			.create(CreditNoteViewImplUiBinder.class);

	interface CreditNoteViewImplUiBinder extends UiBinder<Widget, CreditNoteViewImpl> {
	}

	@UiField Label titleLabel;

	@UiField FlowPanel docControls;
	@UiField ScrollPanel docScroll;

	@UiField(provided=true) ItemInsertionForm itemInsertionForm;

	@UiField Label clientName;
	@UiField(provided=true) ValidatedDateBox date;
	@UiField Label creditNoteNumber;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField ValidatedTextArea note;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;

	@UiField Button abort;
	@UiField(provided=true) LoaderButton createCreditNote;

	private Presenter presenter;

	public CreditNoteViewImpl() {
		number = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);

		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {

			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				DocumentUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}

		});
		date = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		createCreditNote = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName(CSS.accountDocumentView());

		createCreditNote.getButton().setStyleName(CSS.createButton()+" "+GlobalBundle.INSTANCE.globalCss().button());
	}

	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Override
	protected Element getBody() {
		return docScroll.getElement();
	}

	@Override
	protected Element[] getNonBodyElements() {
		return new Element[]{titleLabel.getElement(), docControls.getElement()};
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiFactory
	AccountDocumentCss getAccountDocumentCss(){
		return CSS;
	}

	@Override
	public ScrollPanel getDocScroll() {
		return docScroll;
	}

	@Override
	public ValidatedTextBox getNumber() {
		return number;
	}

	@UiHandler("createCreditNote")
	void onCreateCreditNoteClicked(ClickEvent e){
		presenter.onCreateDocumentClicked();
	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		presenter.onCancelClicked();
	}
	
	@Override
	public void reset() {
		//reset widget statuses
		number.reset();

		//reset widget contents		
		note.setText("");
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		itemInsertionForm.reset();

		createCreditNote.reset();
		setLocked(false);
	}

	@Override
	public void setLocked(boolean value) {
		itemInsertionForm.setLocked(value);

		date.setEnabled(!value);
		number.setEnabled(!value);
		note.setEnabled(!value);

		abort.setEnabled(!value);
	}


	@Override
	public LoaderButton getCreateDocument() {
		return createCreditNote;
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
	public ValidatedTextArea getNote() {
		return note;
	}

	@Override
	public Label getTitleLabel() {
		return titleLabel;
	}

}
