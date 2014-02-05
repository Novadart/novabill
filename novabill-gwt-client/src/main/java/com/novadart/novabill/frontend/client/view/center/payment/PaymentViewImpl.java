package com.novadart.novabill.frontend.client.view.center.payment;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
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
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentViewImpl extends Composite implements PaymentView {

	public static interface Style extends CssResource {
		String label();
		String example();
		String value();
		String deletePaymentImage();
		String noPaymentSelected();
	}

	private static PaymentViewImplUiBinder uiBinder = GWT
			.create(PaymentViewImplUiBinder.class);

	interface PaymentViewImplUiBinder extends UiBinder<Widget, PaymentViewImpl> {
	}
	
	private abstract class ClickableImageCell extends AbstractCell<PaymentTypeDTO> {
		
		private String imageUrl;
		
		public ClickableImageCell(String imageUrl) {
			super("click");
			this.imageUrl = imageUrl;
		}

		@Override
		public void render(Cell.Context context, PaymentTypeDTO value, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<img class='"+style.deletePaymentImage()+"' src='"+this.imageUrl+"'>");
		}
		
		@Override
		public void onBrowserEvent(Cell.Context context, Element parent, PaymentTypeDTO value, NativeEvent event,
				ValueUpdater<PaymentTypeDTO> valueUpdater) {
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			onClick(value);
		}
		
		protected abstract void onClick(PaymentTypeDTO value);
		
	}

	@UiField Style style;
	@UiField(provided=true) CellTable<PaymentTypeDTO> payments;
	@UiField HTML description;

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
		payments.setColumnWidth(name, "400px");
		
		
		Column<PaymentTypeDTO, PaymentTypeDTO> edit = new Column<PaymentTypeDTO, PaymentTypeDTO>(
				new ClickableImageCell(ImageResources.INSTANCE.edit().getSafeUri().asString()) {
					
					@Override
					protected void onClick(final PaymentTypeDTO value) {
						presenter.onPaymentEdit(value);
					}
				}) {

			@Override
			public PaymentTypeDTO getValue(PaymentTypeDTO object) {
				return object;
			}
		};
		payments.addColumn(edit);
		payments.setColumnWidth(edit, "16px");
		
		Column<PaymentTypeDTO, PaymentTypeDTO> delete = new Column<PaymentTypeDTO, PaymentTypeDTO>(
				new ClickableImageCell(ImageResources.INSTANCE.delete().getSafeUri().asString()) {
					
					@Override
					protected void onClick(final PaymentTypeDTO value) {
						presenter.onPaymentDelete(value);
					}
				}) {

			@Override
			public PaymentTypeDTO getValue(PaymentTypeDTO object) {
				return object;
			}
		};
		payments.addColumn(delete);
		payments.setColumnWidth(delete, "16px");
		

		initWidget(uiBinder.createAndBindUi(this));
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
