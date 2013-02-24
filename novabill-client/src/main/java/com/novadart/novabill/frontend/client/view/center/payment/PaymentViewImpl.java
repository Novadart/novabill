package com.novadart.novabill.frontend.client.view.center.payment;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.novadart.novabill.frontend.client.i18n.I18N;
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
	@UiField(provided=true) CellList<PaymentTypeDTO> payments;
	@UiField HTML description;
	
	private final SingleSelectionModel<PaymentTypeDTO> selectedPayment = new SingleSelectionModel<PaymentTypeDTO>(); 
	
	private Presenter presenter;
	
	public PaymentViewImpl() {
		
		payments = new CellList<PaymentTypeDTO>(new AbstractCell<PaymentTypeDTO>() {

			@Override
			public void render(Cell.Context context, PaymentTypeDTO value, SafeHtmlBuilder sb) {
				sb.appendEscaped(value.getName());
			}
		});
		payments.setSelectionModel(selectedPayment);
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocked(boolean value) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public Style getStyle() {
		return style;
	}
	
	@Override
	public CellList<PaymentTypeDTO> getPayments() {
		return payments;
	}
	
	@Override
	public HTML getDescription() {
		return description;
	}

}
