package com.novadart.novabill.frontend.client.widget.payment;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.util.DocumentUtils;

public class PaymentSummary extends Composite {

	public static interface Handler {
		void onResetClicked();
	}
	
	private static PaymentSummaryUiBinder uiBinder = GWT
			.create(PaymentSummaryUiBinder.class);

	interface PaymentSummaryUiBinder extends UiBinder<Widget, PaymentSummary> {
	}
	
	@UiField SimplePanel paymentDateContainer;
	@UiField Label paymentName;
	@UiField Button reset;
	
	private boolean manual = false;
	private Handler handler;
	private Date paymentDueDate;

	public PaymentSummary(Handler handler) {
		this.handler = handler;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiFactory
	I18N getI18n(){
		return I18N.INSTANCE;
	}
	
	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}
	
	public void setPaymentName(String name){
		paymentName.setText(name);
	}
	
	public void setPaymentDueDate(Date date){
		paymentDueDate = date;
		Label l = new Label();
		l.setText(DocumentUtils.DOCUMENT_DATE_FORMAT.format(date));
		paymentDateContainer.setWidget(l);
	}

	public void setManual(){
		DateBox date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DocumentUtils.DOCUMENT_DATE_FORMAT));
		paymentDateContainer.setWidget(date);
	}
	
	public Date getPaymentDueDate(){
		if(manual) {
			DateBox db = (DateBox) paymentDateContainer.getWidget();
			return db.getValue();
		} else {
			return paymentDueDate;
		}
	}
	
	public void setEnabled(boolean value){
		reset.setEnabled(value);
	}
	
	@UiHandler("reset")
	void onResetClicked(ClickEvent e){
		handler.onResetClicked();
	}
	
}
