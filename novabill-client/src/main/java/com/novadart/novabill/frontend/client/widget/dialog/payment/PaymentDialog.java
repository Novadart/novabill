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
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
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
	@UiField(provided=true) ValidatedTextBox days;
	
	@UiField FlowPanel paymentDelayValue;
	@UiField Label paymentDelayLabel;
	
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
		
		setWidget(uiBinder.createAndBindUi(this));
		
		if(payment != null){
			this.payment = payment;
			name.setText(payment.getName());
			paymentNote.setText(payment.getDefaultPaymentNote());
			dateGenerator.setSelectedIndex(payment.getPaymentDateGenerator().ordinal());
			if(PaymentDateType.CUSTOM.equals(payment.getPaymentDateGenerator())){
				paymentDelayLabel.setVisible(false);
				paymentDelayValue.setVisible(false);
			}
			days.setText(payment.getPaymentDateDelta()==null ? "" : String.valueOf(payment.getPaymentDateDelta()));
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
				p.setPaymentDateDelta(Integer.parseInt(days.getText()));
				break;
			}
			
			if(this.payment == null){
				
				ServerFacade.payment.add(p, new ManagedAsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						handler.onPaymentAdd(p);
						setLocked(false);
						hide();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						setLocked(false);
					}
				});
				
			} else {
				
				ServerFacade.payment.update(p, new ManagedAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						handler.onPaymentUpdate(p);
						setLocked(false);
						hide();
					}
					
					@Override
					public void onFailure(Throwable caught) {
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
		
		if(!PaymentDateType.CUSTOM.equals(PaymentDateType.valueOf(dateGenerator.getValue(dateGenerator.getSelectedIndex())))){
			days.validate();
			valid &= days.isValid();
		}
		
		return valid;
	}

	@Override
	public void setLocked(boolean value) {
		name.setEnabled(value);
		paymentNote.setEnabled(value);
		days.setEnabled(value);
	}
	
}
