package com.novadart.novabill.frontend.client.widget.dialog.payment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentDialog extends Dialog implements HasUILocking {

	private static PaymentDialogUiBinder uiBinder = GWT
			.create(PaymentDialogUiBinder.class);

	interface PaymentDialogUiBinder extends UiBinder<Widget, PaymentDialog> {
	}
	
	public interface Handler {
		void onPaymentAdd(PaymentTypeDTO payment);
		void onPaymentUpdate(PaymentTypeDTO payment);
	}

	@UiField(provided=true) ValidatedTextBox name;
	@UiField(provided=true) ValidatedTextArea paymentNote;
	@UiField(provided=true) ListBox dateGenerator;
	@UiField(provided=true) ListBox months;
	@UiField Label daysLabel;
	
	@UiField FlowPanel paymentDelayValue;
	@UiField Label paymentDelayLabel;
	@UiField(provided=true) LoaderButton ok;
	
	private Handler handler;
	private PaymentTypeDTO payment = null;
	
	public PaymentDialog(Handler handler, PaymentTypeDTO payment) {
		super(GlobalBundle.INSTANCE.dialog());
		GlobalBundle.INSTANCE.dialog().ensureInjected();

		this.handler = handler;
		
		name = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		
		paymentNote = new ValidatedTextArea();
		
		dateGenerator = new ListBox();
		dateGenerator.addItem(I18N.INSTANCE.dateGenerationImmediate(), PaymentDateType.IMMEDIATE.name());
		dateGenerator.addItem(I18N.INSTANCE.dateGenerationEndOfMonth(), PaymentDateType.END_OF_MONTH.name());
		dateGenerator.addItem(I18N.INSTANCE.dateGenerationManual(), PaymentDateType.CUSTOM.name());

		months = new ListBox();
		months.addItem(I18N.INSTANCE.immediate());
		months.addItem("30");
		months.addItem("60");
		months.addItem("90");
		months.addItem("120");
		months.addItem("150");
		months.addItem("180");
		months.addItem("210");
		months.addItem("240");
		
		ok = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		ok.getButton().addStyleName("btn green");
		
		setWidget(uiBinder.createAndBindUi(this));
		
		if(payment != null){
			this.payment = payment;
			name.setText(payment.getName());
			paymentNote.setText(payment.getDefaultPaymentNote());
			dateGenerator.setSelectedIndex(payment.getPaymentDateGenerator().ordinal());
			if(PaymentDateType.CUSTOM.equals(payment.getPaymentDateGenerator())){
				paymentDelayLabel.setVisible(false);
				paymentDelayValue.setVisible(false);
			} else {
				months.setSelectedIndex(payment.getPaymentDateDelta());
				daysLabel.setVisible(months.getSelectedIndex()!=0);
			}
			
			ok.setText(I18N.INSTANCE.saveModifications());
		}
	}
	
	public PaymentDialog(Handler handler) {
		this(handler, null);
	}
	
	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	@UiFactory
	GlobalCss getGlobalCss() {
		return GlobalBundle.INSTANCE.globalCss();
	}

	@UiHandler("cancel")
	void onCancelClicked(ClickEvent e){
		hide();
	}
	
	@UiHandler("months")
	void onMonthsChanged(ChangeEvent e){
		daysLabel.setVisible(months.getSelectedIndex() != 0);
	}
	
	@UiHandler("ok")
	void onOkClicked(ClickEvent e){
		if(validate()) {
			ok.showLoader(true);
			setLocked(true);
			
			final PaymentTypeDTO p = this.payment == null ? new PaymentTypeDTO() : this.payment;
			p.setBusiness(Configuration.getBusiness());
			p.setDefaultPaymentNote(paymentNote.getText());
			p.setName(name.getText());
			
			PaymentDateType dt = 
					PaymentDateType.valueOf(dateGenerator.getValue(dateGenerator.getSelectedIndex()));
			
			p.setPaymentDateGenerator(dt);
			
			switch(dt){
			
			case CUSTOM:
				break;	
			
			case END_OF_MONTH:
			case IMMEDIATE:
				p.setPaymentDateDelta(months.getSelectedIndex());
				break;
			}
			
			if(this.payment == null){
				
				ServerFacade.INSTANCE.getPaymentService().add(p, new ManagedAsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						ok.showLoader(false);
						handler.onPaymentAdd(p);
						setLocked(false);
						hide();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						ok.showLoader(false);
						super.onFailure(caught);
						setLocked(false);
					}
				});
				
			} else {
				
				ServerFacade.INSTANCE.getPaymentService().update(p, new ManagedAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						ok.showLoader(false);
						handler.onPaymentUpdate(p);
						setLocked(false);
						hide();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						ok.showLoader(false);
						super.onFailure(caught);
						setLocked(false);
					}
				});
				
			}
			
		}
	}
	
	@UiHandler("dateGenerator")
	void onChange(ChangeEvent e){
		switch(PaymentDateType.valueOf(dateGenerator.getValue(dateGenerator.getSelectedIndex()))){
		case CUSTOM:
			paymentDelayLabel.setVisible(false);
			paymentDelayValue.setVisible(false);
			break;
			
		case END_OF_MONTH:
		case IMMEDIATE:
			paymentDelayLabel.setVisible(true);
			paymentDelayValue.setVisible(true);
			break;
		}
	}
	
	private boolean validate(){
		boolean valid = true;
		
		name.validate();
		paymentNote.validate();
		
		valid = name.isValid() && paymentNote.isValid();
		
		return valid;
	}

	@Override
	public void setLocked(boolean value) {
		name.setEnabled(value);
		paymentNote.setEnabled(value);
		months.setEnabled(value);
	}
	
}
