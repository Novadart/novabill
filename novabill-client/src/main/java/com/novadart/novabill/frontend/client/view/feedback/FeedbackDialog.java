package com.novadart.novabill.frontend.client.view.feedback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.widget.notification.Notification;

public class FeedbackDialog extends Dialog {

	private static FeedbackDialogUiBinder uiBinder = GWT
			.create(FeedbackDialogUiBinder.class);

	interface FeedbackDialogUiBinder extends UiBinder<Widget, FeedbackDialog> {
	}
	
	private static final int MESSAGE_MAX_LENGTH = 50;
	
	@UiField FlowPanel contacttable;
	@UiField FlowPanel contactForm;
	@UiField HTMLPanel contactHolder;
	@UiField HTMLPanel contactLoading;
	
	@UiField TextBox contactableName;
	@UiField TextBox contactableEmail;
	@UiField TextArea contactableMessage;
	
	@UiField SelectElement contactableDdown;
	
	public FeedbackDialog() {
		setAutoHideEnabled(true);
		setWidget(uiBinder.createAndBindUi(this));
		contactableMessage.setVisibleLines(5);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		contacttable.getElement().setId("contactable");
		contactForm.getElement().setId("contactable-contactForm");
		contactHolder.getElement().setId("contactable-holder");
		contactLoading.getElement().setId("contactable-loading");
		
		contactableName.getElement().setId("contactable-name");
		contactableEmail.getElement().setId("contactable-email");
		contactableMessage.getElement().setId("contactable-message");
	}
	
	@UiHandler("contactableMessage")
	void onContactableMessageKeyUp(KeyUpEvent e){
		String txt = contactableMessage.getText();
		if(txt.length() > MESSAGE_MAX_LENGTH) {
			contactableMessage.setText(txt.substring(0, MESSAGE_MAX_LENGTH));
		}
	}
	
	@UiHandler("contactableSubmit")
	void onSubmitClicked(ClickEvent clickEvent){
		boolean valid = true;
		
		if(contactableName.getText().trim().isEmpty()){
			valid = false;
			if(!contactableName.getStyleName().contains("contactable-invalid")){
				contactableName.addStyleName("contactable-invalid");
			}
		} else {
			contactableName.removeStyleName("contactable-invalid");
		}
		
		if(contactableEmail.getText().trim().isEmpty() || !Validation.isEmail(contactableEmail.getText())){
			valid = false;
			if(!contactableEmail.getStyleName().contains("contactable-invalid")){
				contactableEmail.addStyleName("contactable-invalid");
			}
		} else {
			contactableEmail.removeStyleName("contactable-invalid");
		}
		
		if(contactableMessage.getText().trim().isEmpty()){
			valid = false;
			if(!contactableMessage.getStyleName().contains("contactable-invalid")){
				contactableMessage.addStyleName("contactable-invalid");
			}
		} else {
			contactableMessage.removeStyleName("contactable-invalid");
		}
		
		if(valid){
			contactHolder.setVisible(false);
			contactLoading.setVisible(true);
			
			ServerFacade.sendFeedback(
					Window.Location.getHref(), 
					contactableName.getText().trim(), 
					contactableEmail.getText().trim(), 
					contactableMessage.getText().trim(), 
					contactableDdown.getValue(), 
					new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							hide();
							Notification.showMessage("Grazie per il tempo che ci hai dedicato!");
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Notification.showMessage("Ops.. c'è stato un errore con l'invio :P \n\nPotresti riprovare per favore? \nAltrimenti puoi mandare una email a help@novabill.it");
						}
					});
		}
	}
	
	@UiHandler("closeButton")
	void onCloseButtonClicked(ClickEvent e){
		hide();
	}
	
	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	@UiFactory
	ImageResources getRes(){
		return ImageResources.INSTANCE;
	}
	
}
