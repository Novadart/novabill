package com.novadart.novabill.frontend.client.widget.dialog.payment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
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
	@UiField(provided=true) ValidatedTextBox delay;
	@UiField ListBox delayType;
	
	@UiField Label secondaryPaymentDelayLabel;
	@UiField(provided=true) ValidatedTextBox days;
	
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

		days = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);
		
		delay = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);
		
		ok = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		ok.getButton().addStyleName("btn green");
		
		setWidget(uiBinder.createAndBindUi(this));
		
		delayType.addItem("Mesi Commerciali", PaymentDeltaType.COMMERCIAL_MONTH.name());
		delayType.addItem("Giorni", PaymentDeltaType.DAYS.name());
		
		if(payment != null){
			this.payment = payment;
			name.setText(payment.getName());
			paymentNote.setText(payment.getDefaultPaymentNote());
			dateGenerator.setSelectedIndex(payment.getPaymentDateGenerator().ordinal());
			
			switch (payment.getPaymentDateGenerator()) {
			case CUSTOM:
				paymentDelayLabel.setVisible(false);
				delay.setVisible(false);
				delayType.setVisible(false);
				break;

			case END_OF_MONTH:
				secondaryPaymentDelayLabel.setVisible(true);
				delay.setText(String.valueOf(payment.getPaymentDateDelta()));
				delayType.setSelectedIndex(payment.getPaymentDeltaType().ordinal());
				days.setVisible(true);
				days.setText(String.valueOf(payment.getSecondaryPaymentDateDelta()));
				break;

			case IMMEDIATE:
				delay.setText(String.valueOf(payment.getPaymentDateDelta()));
				delayType.setSelectedIndex(payment.getPaymentDeltaType().ordinal());
				break;

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
				p.setSecondaryPaymentDateDelta(0);
				break;	

			case END_OF_MONTH:
				p.setSecondaryPaymentDateDelta(Integer.parseInt(days.getText()));
				p.setPaymentDateDelta(Integer.parseInt(delay.getText()));
				p.setPaymentDeltaType(PaymentDeltaType.valueOf(delayType.getValue(delayType.getSelectedIndex())));
				break;
				
			case IMMEDIATE:
				p.setPaymentDateDelta(Integer.parseInt(delay.getText()));
				p.setPaymentDeltaType(PaymentDeltaType.valueOf(delayType.getValue(delayType.getSelectedIndex())));
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
			delay.setVisible(false);
			delayType.setVisible(false);
			secondaryPaymentDelayLabel.setVisible(false);
			days.setVisible(false);
			break;

		case END_OF_MONTH:
			secondaryPaymentDelayLabel.setVisible(true);
			days.setVisible(true);
			paymentDelayLabel.setVisible(true);
			delay.setVisible(true);
			delayType.setVisible(true);
			break;
			
		case IMMEDIATE:
			secondaryPaymentDelayLabel.setVisible(false);
			days.setVisible(false);
			paymentDelayLabel.setVisible(true);
			delay.setVisible(true);
			delayType.setVisible(true);
			break;
		}
		
	}
	
	private boolean validate(){
		boolean valid = true;
		
		name.validate();
		paymentNote.validate();
		
		valid = name.isValid() && paymentNote.isValid();
		
		switch(PaymentDateType.valueOf(dateGenerator.getValue(dateGenerator.getSelectedIndex()))){
		case END_OF_MONTH:
			delay.validate();
			days.validate();
			valid = valid && days.isValid() && delay.isValid();
			break;
			
		case IMMEDIATE:
			delay.validate();
			valid = valid && delay.isValid();
			break;
			
		default:
			break;
		}
		
		return valid;
	}

	@Override
	public void setLocked(boolean value) {
		name.setEnabled(value);
		paymentNote.setEnabled(value);
		delay.setEnabled(value);
		delayType.setEnabled(value);
		days.setEnabled(value);
	}
	
}
