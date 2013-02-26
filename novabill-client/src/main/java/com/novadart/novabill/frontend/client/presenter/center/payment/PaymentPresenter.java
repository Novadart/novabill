package com.novadart.novabill.frontend.client.presenter.center.payment;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.PaymentAddEvent;
import com.novadart.novabill.frontend.client.event.PaymentAddHandler;
import com.novadart.novabill.frontend.client.event.PaymentDeleteEvent;
import com.novadart.novabill.frontend.client.event.PaymentDeleteHandler;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.payment.PaymentView;
import com.novadart.novabill.frontend.client.widget.dialog.payment.PaymentDialog;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentPresenter extends AbstractPresenter<PaymentView> implements PaymentView.Presenter {


	private final ListDataProvider<PaymentTypeDTO> paymentData = new ListDataProvider<PaymentTypeDTO>();


	public PaymentPresenter(PlaceController placeController, EventBus eventBus, PaymentView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(PaymentView view) {
		view.setPresenter(this);
	}

	@Override
	public void go(AcceptsOneWidget panel) {
		MainWidget.getInstance().setStandardView();
		bind();
		panel.setWidget(getView());
	}

	private void bind(){
		getEventBus().addHandler(PaymentAddEvent.TYPE, new PaymentAddHandler() {

			@Override
			public void onPaymentAdd(PaymentAddEvent event) {
				reloadPayments();
			}
		});

		getEventBus().addHandler(PaymentDeleteEvent.TYPE, new PaymentDeleteHandler() {

			@Override
			public void onPaymentDelete(PaymentDeleteEvent event) {
				reloadPayments();
			}
		});
		
		paymentData.addDataDisplay(getView().getPayments());
	}

	private void reloadPayments(){
		ServerFacade.payment.getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<PaymentTypeDTO>>() {

			@Override
			public void onSuccess(List<PaymentTypeDTO> result) {
				paymentData.setList(result);
				paymentData.refresh();
			}
		});
	}
	
	@Override
	public void onAddPaymentClicked() {
		final PaymentDialog pd = new PaymentDialog(new PaymentDialog.Handler() {
			
			@Override
			public void onPaymentCreated(PaymentTypeDTO payment) {
				getEventBus().fireEvent(new PaymentAddEvent(payment));
			}
		});
		pd.showCentered();
	}
	
	
	public void onPaymentDelete(final PaymentTypeDTO payment){
		ServerFacade.payment.remove(Configuration.getBusinessId(), payment.getId(), new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				getEventBus().fireEvent(new PaymentDeleteEvent(payment));
			}
		});
	}

	@Override
	public void onLoad() {
		reloadPayments();
	}


}
