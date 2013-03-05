package com.novadart.novabill.frontend.client.view.center.payment;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentViewImpl extends Composite implements PaymentView {

	public static interface Style extends CssResource {
		String label();
		String value();
	}

	private static PaymentViewImplUiBinder uiBinder = GWT
			.create(PaymentViewImplUiBinder.class);

	interface PaymentViewImplUiBinder extends UiBinder<Widget, PaymentViewImpl> {
	}

	@UiField Style style;
	@UiField(provided=true) CellTable<PaymentTypeDTO> payments;
	@UiField HTML description;

	private final SingleSelectionModel<PaymentTypeDTO> selectedPayment = new SingleSelectionModel<PaymentTypeDTO>();


	private Presenter presenter;

	public PaymentViewImpl() {

		payments = new CellTable<PaymentTypeDTO>();

		TextColumn<PaymentTypeDTO> name =  new TextColumn<PaymentTypeDTO>() {

			@Override
			public String getValue(PaymentTypeDTO object) {
				return object.getName();
			}
		};
		name.setSortable(true);
		payments.addColumn(name, I18N.INSTANCE.payment());
		payments.setColumnWidth(name, "300px");
		
		Column<PaymentTypeDTO, PaymentTypeDTO> delete = new Column<PaymentTypeDTO, PaymentTypeDTO>(new ActionCell<PaymentTypeDTO>(I18N.INSTANCE.delete(), new ActionCell.Delegate<PaymentTypeDTO>() {

			@Override
			public void execute(final PaymentTypeDTO object) {
				Notification.showConfirm(I18N.INSTANCE.deletionConfirm(), new NotificationCallback<Boolean>() {

					@Override
					public void onNotificationClosed(Boolean value) {
						if(value){
							presenter.onPaymentDelete(object);
							resetDescription();
						}
					}
				});
			}
		})) {

			@Override
			public PaymentTypeDTO getValue(PaymentTypeDTO object) {
				return object;
			}
		};
		payments.addColumn(delete);
		

		payments.setSelectionModel(selectedPayment);

		selectedPayment.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				updateDescription(selectedPayment.getSelectedObject());
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
	}

	private void updateDescription(PaymentTypeDTO payment){
		SafeHtmlBuilder shb = new SafeHtmlBuilder();

		shb.appendHtmlConstant("<table>");

		shb.appendHtmlConstant("<tr><td><div class='"+style.label()+"'>");
		shb.appendEscaped(I18N.INSTANCE.name());
		shb.appendHtmlConstant("</div></td>");
		shb.appendHtmlConstant("<td><div class='"+style.value()+"'>");
		shb.appendEscaped(payment.getName());
		shb.appendHtmlConstant("</div></td></tr>");

		shb.appendHtmlConstant("<tr><td><div class='"+style.label()+"'>");
		shb.appendEscaped(I18N.INSTANCE.paymentNote());
		shb.appendHtmlConstant("</div></td>");
		shb.appendHtmlConstant("<td><div class='"+style.value()+"'>");
		shb.appendEscaped(payment.getDefaultPaymentNote());
		shb.appendHtmlConstant("</div></td></tr>");

		shb.appendHtmlConstant("<tr><td><div class='"+style.label()+"'>");
		shb.appendEscaped(I18N.INSTANCE.dateGeneration());
		shb.appendHtmlConstant("</div></td>");
		shb.appendHtmlConstant("<td><div class='"+style.value()+"'>");
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
			shb.appendHtmlConstant("<tr><td><div class='"+style.label()+"'>");
			shb.appendEscaped(I18N.INSTANCE.paymentDelay());
			shb.appendHtmlConstant("</div></td>");
			shb.appendHtmlConstant("<td><div class='"+style.value()+"'>");
			shb.appendEscaped(payment.getPaymentDateDelta()+ " "+I18N.INSTANCE.days());
			shb.appendHtmlConstant("</div></td></tr>");
		}

		shb.appendHtmlConstant("</table>");

		description.setHTML(shb.toSafeHtml());
	}
	
	private void resetDescription(){
		description.setHTML("");
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

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void reset() {
	}

	@Override
	public void setLocked(boolean value) {

	}

	@UiHandler("newPayment")
	void onAddPaymentClicked(ClickEvent e){
		presenter.onAddPaymentClicked();
	}

	@Override
	public Style getStyle() {
		return style;
	}

	@Override
	public CellTable<PaymentTypeDTO> getPayments() {
		return payments;
	}

	@Override
	public HTML getDescription() {
		return description;
	}

}
