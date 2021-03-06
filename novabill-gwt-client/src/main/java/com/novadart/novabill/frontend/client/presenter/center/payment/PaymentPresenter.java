package com.novadart.novabill.frontend.client.presenter.center.payment;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.payment.PaymentView;
import com.novadart.novabill.frontend.client.widget.dialog.payment.PaymentDialog;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentPresenter extends AbstractPresenter<PaymentView> implements PaymentView.Presenter {


	private final ListDataProvider<PaymentTypeDTO> paymentData = new ListDataProvider<PaymentTypeDTO>();
	private final SingleSelectionModel<PaymentTypeDTO> selectedPayment = new SingleSelectionModel<PaymentTypeDTO>();


	public PaymentPresenter(PlaceController placeController, EventBus eventBus, PaymentView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(PaymentView view) {
		view.setPresenter(this);
	}

	@Override
	public void go(AcceptsOneWidget panel) {

		bind();
		panel.setWidget(getView());
	}

	private void bind(){
//		getEventBus().addHandler(PaymentAddEvent.TYPE, new PaymentAddHandler() {
//
//			@Override
//			public void onPaymentAdd(PaymentAddEvent event) {
//				reloadPayments();
//			}
//		});
//
//		getEventBus().addHandler(PaymentDeleteEvent.TYPE, new PaymentDeleteHandler() {
//
//			@Override
//			public void onPaymentDelete(PaymentDeleteEvent event) {
//				reloadPayments();
//			}
//		});
//		
//		getEventBus().addHandler(PaymentUpdateEvent.TYPE, new PaymentUpdateHandler() {
//
//			@Override
//			public void onPaymentUpdate(PaymentUpdateEvent event) {
//				reloadPayments();
//			}
//		});
		
		paymentData.addDataDisplay(getView().getPayments());
	}

	private void reloadPayments(){
		ServerFacade.INSTANCE.getPaymentService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<PaymentTypeDTO>>() {

			@Override
			public void onSuccess(List<PaymentTypeDTO> result) {
				Collections.sort(result, SharedComparators.PAYMENT_COMPARATOR);
				paymentData.setList(result);
				paymentData.refresh();
			}
		});
	}
	
	@Override
	public void onAddPaymentClicked() {
		final PaymentDialog pd = new PaymentDialog(new PaymentDialog.Handler() {
			
			@Override
			public void onPaymentAdd(PaymentTypeDTO payment) {
				Window.Location.reload();
//				getEventBus().fireEvent(new PaymentAddEvent(payment));
			}
			
			@Override
			public void onPaymentUpdate(PaymentTypeDTO payment) { }
		});
		pd.showCentered();
	}
	
	
	public void onPaymentDelete(final PaymentTypeDTO payment){
		resetDescription();
		Notification.showConfirm(I18N.INSTANCE.paymentDeletionConfirm(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean result) {
				if(result){
					ServerFacade.INSTANCE.getPaymentService().remove(Configuration.getBusinessId(), payment.getId(), new ManagedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							Window.Location.reload();
//							getEventBus().fireEvent(new PaymentDeleteEvent(payment));
						}
					});
				}
			}
		});
	}
	
	@Override
	public void onPaymentEdit(PaymentTypeDTO value) {
		resetDescription();
		selectedPayment.clear();
		final PaymentDialog pd = new PaymentDialog(new PaymentDialog.Handler() {
			
			@Override
			public void onPaymentAdd(final PaymentTypeDTO payment) {}
			
			@Override
			public void onPaymentUpdate(PaymentTypeDTO payment) {
				Window.Location.reload();
//				getEventBus().fireEvent(new PaymentUpdateEvent(payment));
			}
		}, value);
		pd.showCentered();
	}

	@Override
	public void onLoad() {
		resetDescription();
		
		getView().getPayments().setSelectionModel(selectedPayment);

		selectedPayment.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if(selectedPayment.getSelectedObject() != null) { //can be null when the selection is reseted before showing the dialog 
					updateDescription(selectedPayment.getSelectedObject());
				}
			}
		});
		reloadPayments();
	}
	
	
	private void updateDescription(PaymentTypeDTO payment){
		SafeHtmlBuilder shb = new SafeHtmlBuilder();

		shb.appendHtmlConstant("<table>");

		shb.appendHtmlConstant("<tr><td><div class='"+getView().getStyle().label()+"'>");
		shb.appendEscaped(I18N.INSTANCE.name());
		shb.appendHtmlConstant("</div></td>");
		shb.appendHtmlConstant("<td><div class='"+getView().getStyle().value()+"'>");
		shb.appendEscaped(payment.getName());
		shb.appendHtmlConstant("</div></td></tr>");

		shb.appendHtmlConstant("<tr><td><div class='"+getView().getStyle().label()+"'>");
		shb.appendEscaped(I18N.INSTANCE.paymentNote());
		shb.appendHtmlConstant("</div></td>");
		shb.appendHtmlConstant("<td><div class='"+getView().getStyle().value()+"'>");
		shb.appendEscaped(payment.getDefaultPaymentNote());
		shb.appendHtmlConstant("</div></td></tr>");

		shb.appendHtmlConstant("<tr><td><div class='"+getView().getStyle().label()+"'>");
		shb.appendEscaped(I18N.INSTANCE.dateGeneration());
		shb.appendHtmlConstant("</div></td>");
		shb.appendHtmlConstant("<td><div class='"+getView().getStyle().value()+"'>");
		switch (payment.getPaymentDateGenerator()) {
		case CUSTOM:
			shb.appendEscaped(I18N.INSTANCE.dateGenerationManual());
			break;

		case END_OF_MONTH:
			shb.appendEscaped(I18N.INSTANCE.dateGenerationEndOfMonth());
			break;

		case IMMEDIATE:
			shb.appendEscaped(I18N.INSTANCE.dateGenerationImmediate());
			break;
		}
		shb.appendHtmlConstant("</div></td></tr>");

		if(!PaymentDateType.CUSTOM.equals(payment.getPaymentDateGenerator())) {
			shb.appendHtmlConstant("<tr><td><div class='"+getView().getStyle().label()+"'>");
			shb.appendEscaped(I18N.INSTANCE.paymentDelay());
			shb.appendHtmlConstant("</div></td>");
			shb.appendHtmlConstant("<td><div class='"+getView().getStyle().value()+"'>");
			shb.appendEscaped(payment.getPaymentDateDelta()==0 
					? I18N.INSTANCE.immediate() : (payment.getPaymentDateDelta() + " " 
			+ (PaymentDeltaType.COMMERCIAL_MONTH.equals(payment.getPaymentDeltaType()) ? I18N.INSTANCE.commercialMonths() : I18N.INSTANCE.days())) );
			shb.appendHtmlConstant("</div></td></tr>");
		}

		if(PaymentDateType.END_OF_MONTH.equals(payment.getPaymentDateGenerator())) {
			shb.appendHtmlConstant("<tr><td><div class='"+getView().getStyle().label()+"'>");
			shb.appendEscaped(I18N.INSTANCE.paymentDelay2());
			shb.appendHtmlConstant("</div></td>");
			shb.appendHtmlConstant("<td><div class='"+getView().getStyle().value()+"'>");
			shb.appendEscaped(payment.getSecondaryPaymentDateDelta() + " "+I18N.INSTANCE.days());
			shb.appendHtmlConstant("</div></td></tr>");
		}
		
		if(!PaymentDateType.CUSTOM.equals(payment.getPaymentDateGenerator())) {
			Date today = new Date();
			
			shb.appendHtmlConstant("<tr><td>&nbsp;</td><td>&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td></tr>");
			
			shb.appendHtmlConstant("<tr><td colspan='2'><div>");
			shb.appendEscaped(I18N.INSTANCE.example());
			shb.appendHtmlConstant(":</div>");
			shb.appendHtmlConstant("<table>");
			shb.appendHtmlConstant("<tr><td>"+I18N.INSTANCE.date()+"</td><td>&nbsp;&nbsp;&nbsp;<i>"+DocumentUtils.DOCUMENT_DATE_FORMAT.format(today)+"</i></td></tr>");
			shb.appendHtmlConstant("<tr><td>"+I18N.INSTANCE.dueDate()+"</td><td>&nbsp;&nbsp;&nbsp;<i>"+DocumentUtils.DOCUMENT_DATE_FORMAT.format(CalcUtils.calculatePaymentDueDate(today, payment))+"</i></td></tr>");
			shb.appendHtmlConstant("</table>");
			shb.appendHtmlConstant("</div></td></tr>");
		}
		
		shb.appendHtmlConstant("</table>");

		getView().getDescription().setHTML(shb.toSafeHtml());
	}
	
	private void resetDescription(){
		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		shb.appendHtmlConstant("<div class='"+getView().getStyle().noPaymentSelected()+"'>");
		shb.appendEscaped(I18N.INSTANCE.noPaymentSelected());
		shb.appendHtmlConstant("</div>");
		getView().getDescription().setHTML(shb.toSafeHtml());
	}


}
