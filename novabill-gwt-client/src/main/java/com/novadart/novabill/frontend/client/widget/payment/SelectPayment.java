package com.novadart.novabill.frontend.client.widget.payment;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class SelectPayment extends Composite implements PaymentSummary.Handler {
	
	public interface Style extends CssResource {
		String container();
		String paymentList();
		String label();
		String value();
		String changePayment();
	}
	
	public static interface Handler {
		void onPaymentSelected(PaymentTypeDTO payment);
		void onPaymentClear();
	}
	
	private static SelectPaymentUiBinder uiBinder = GWT
			.create(SelectPaymentUiBinder.class);

	interface SelectPaymentUiBinder extends UiBinder<Widget, SelectPayment> {
	}
	
	@UiField SimplePanel container;
	@UiField Style style;
	
	private PaymentSummary paymentSummary;
	private ValidatedListBox paymentList;
	private Image loader;
	
	private Map<Long, PaymentTypeDTO> payments;
	private Date documentCreationDate;
	private PaymentTypeDTO selectedPayment;
	private boolean showingSummary = false;
	private Handler handler;
	
	public SelectPayment(Handler handler) {
		this.handler = handler;
		initWidget(uiBinder.createAndBindUi(this));
		paymentSummary = new PaymentSummary(style, this);
	}
	
	public void setEnabled(boolean value) {
		if(paymentSummary != null){
			paymentSummary.setEnabled(value);
		}
		
		if(paymentList != null) {
			paymentList.setEnabled(value);
		}
	}
	
	public void init(){
		selectedPayment = null;
		setupPaymentView();
	}
	
	public void init(PaymentTypeDTO payment){
		selectedPayment = payment;
		setupPaymentSummaryView();
	}
	
	public void init(String paymentName, PaymentDateType dateGenerator, Integer paymentDateDelta, PaymentDeltaType paymentDeltaType, 
			Integer secondaryPaymentDateDelta, Date paymentDueDate){
		selectedPayment = new PaymentTypeDTO();
		selectedPayment.setName(paymentName);
		selectedPayment.setPaymentDateDelta(paymentDateDelta);
		selectedPayment.setPaymentDateGenerator(dateGenerator);
		selectedPayment.setPaymentDeltaType(paymentDeltaType);
		selectedPayment.setSecondaryPaymentDateDelta(secondaryPaymentDateDelta);
		setupPaymentSummaryView(paymentDueDate);
	}
	
	public void init(String paymentName, PaymentDateType dateGenerator, Integer paymentDateDelta, PaymentDeltaType paymentDeltaType, 
			Integer secondaryPaymentDateDelta){
		selectedPayment = new PaymentTypeDTO();
		selectedPayment.setName(paymentName);
		selectedPayment.setPaymentDateDelta(paymentDateDelta);
		selectedPayment.setPaymentDateGenerator(dateGenerator);
		selectedPayment.setPaymentDeltaType(paymentDeltaType);
		selectedPayment.setSecondaryPaymentDateDelta(secondaryPaymentDateDelta);
		setupPaymentSummaryView();
	}
	
	public void setDocumentCreationDate(Date date){
		documentCreationDate = date;
		if(showingSummary){
			paymentSummary.setPaymentDueDate(CalcUtils.calculatePaymentDueDate(documentCreationDate, selectedPayment));
		}
	}
	
	
	private void setupPaymentView(){
		setupLoader();
		ServerFacade.INSTANCE.getPaymentService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<PaymentTypeDTO>>() {

			@Override
			public void onSuccess(List<PaymentTypeDTO> result) {
				Collections.sort(result, SharedComparators.PAYMENT_COMPARATOR);
				
				paymentList = new ValidatedListBox(GlobalBundle.INSTANCE.validatedWidget(), I18N.INSTANCE.notEmptyValidationError());
				paymentList.addStyleName(style.paymentList());
				paymentList.addChangeHandler(new ChangeHandler() {
					
					@Override
					public void onChange(ChangeEvent event) {
						paymentList.validate();
						if(paymentList.isValid()) {
							String value = paymentList.getSelectedItemValue();
							Long id = value.isEmpty() ? null : Long.parseLong(paymentList.getSelectedItemValue());
							init(id==null ? PaidInFull.instance() : payments.get(id));
							handler.onPaymentSelected(selectedPayment);
						}
					}
				});

				payments = new HashMap<Long, PaymentTypeDTO>();
				
				for (PaymentTypeDTO p : result) {
					payments.put(p.getId(), p);
					paymentList.addItem(p.getName(), p.getId().toString());
				}
				paymentList.addItem(PaidInFull.instance().getName(), "");
				
				container.setWidget(paymentList);
				showingSummary = false;
			}
		});
	}
	
	private void setupPaymentSummaryView(){
		setupPaymentSummaryView(null);
	}
	
	private void setupPaymentSummaryView(Date paymentDueDate){
		paymentSummary.setPaymentName(selectedPayment.getName());
		switch (selectedPayment.getPaymentDateGenerator()) {
		case CUSTOM:
			paymentSummary.setManual(paymentDueDate);
			break;

		default:
			if(paymentDueDate != null) {
				paymentSummary.setPaymentDueDate(paymentDueDate);
			} else {
				paymentSummary.setPaymentDueDate(CalcUtils.calculatePaymentDueDate(documentCreationDate, selectedPayment));
			}
			break;
		}
		
		container.setWidget(paymentSummary);
		showingSummary = true;
	}
	

	@Override
	public void onResetClicked() {
		handler.onPaymentClear();
		init();
	}
	
	private void setupLoader(){
		if(loader == null){
			loader = new Image(ImageResources.INSTANCE.loader().getSafeUri().asString());
		}
		container.setWidget(loader);
	}
	
	
	public boolean isValid(){
		if(!showingSummary){
			paymentList.validate();
			return false;
		}
		
		boolean result = showingSummary;
		if(result && PaymentDateType.CUSTOM.equals(selectedPayment.getPaymentDateGenerator())){
			result &= paymentSummary.getPaymentDueDate() != null;
		}
		return result;
	}
	
	public Date getPaymentDueDate(){
		return paymentSummary.getPaymentDueDate();
	}
	
	public PaymentTypeDTO getSelectedPayment() {
		return selectedPayment;
	}

	public void reset() {
		showingSummary = false;
		setupLoader();
	}
	
}
