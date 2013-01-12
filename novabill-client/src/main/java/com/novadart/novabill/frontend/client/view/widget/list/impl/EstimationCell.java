package com.novadart.novabill.frontend.client.view.widget.list.impl;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.estimation.CloneEstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.ModifyEstimationPlace;
import com.novadart.novabill.frontend.client.place.invoice.FromEstimationInvoicePlace;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.frontend.client.view.View.Presenter;
import com.novadart.novabill.frontend.client.view.widget.dialog.SelectClientDialog;
import com.novadart.novabill.frontend.client.view.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.view.widget.notification.Notification;
import com.novadart.novabill.frontend.client.view.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationCell extends QuickViewCell<EstimationDTO> {

	private Presenter presenter;
	private EventBus eventBus;

	@Override
	protected void renderDetails(
			com.google.gwt.cell.client.Cell.Context context,
			EstimationDTO value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<div class='upper'>");
		sb.appendHtmlConstant("<span class='total'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='convertToInvoice'>");
		sb.appendEscaped(I18N.INSTANCE.convertToInvoice());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='tools'>");
		sb.appendHtmlConstant("<span class='button openEstimation'>");
		sb.appendEscaped(I18N.INSTANCE.modify());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='clone'>");
		sb.appendEscaped(I18N.INSTANCE.clone());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='downloadAsPDF'>");
		sb.appendEscaped("PDF");
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='delete'>");
		sb.appendEscaped(I18N.INSTANCE.delete());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");
	}

	@Override
	protected void renderVisible(
			com.google.gwt.cell.client.Cell.Context context,
			EstimationDTO value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<div class='main'>");
		sb.appendHtmlConstant("<span class='id'>");
		sb.append(value.getDocumentID());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='date'>");
		sb.appendEscaped(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(value.getAccountingDocumentDate()));
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='name'>");
		sb.appendEscaped(value.getClient().getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");

	}

	@Override
	protected void onClick(EstimationDTO value, EventTarget eventTarget) {
		if(isPdf(eventTarget)){
			onPdfClicked(value);
		} else if(isDelete(eventTarget)){
			onDeleteClicked(value);
		} else if(isOpenEstimation(eventTarget)){
			onOpenEstimationClicked(value);
		} else if(isConvertToInvoice(eventTarget)){
			onConvertToInvoiceClicked(value);
		} else if(isClone(eventTarget)){
			onCloneClicked(value);
		}
	}

	private boolean isOpenEstimation(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement open = et.cast();
			return open.getClassName().contains("openEstimation");

		} else {
			return false;
		}
	}

	private boolean isPdf(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement img = et.cast();
			return "downloadAsPDF".equals(img.getClassName());

		} else {
			return false;
		}
	}

	private boolean isClone(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement img = et.cast();
			return "clone".equals(img.getClassName());

		} else {
			return false;
		}
	}

	private boolean isDelete(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement delete = et.cast();
			return "delete".equals(delete.getClassName());

		} else {
			return false;
		}
	}

	private boolean isConvertToInvoice(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement delete = et.cast();
			return "convertToInvoice".equals(delete.getClassName());

		} else {
			return false;
		}
	}

	public void onOpenEstimationClicked(EstimationDTO estimation) {
		ModifyEstimationPlace ep = new ModifyEstimationPlace();
		ep.setEstimationId(estimation.getId());
		presenter.goTo(ep);
	}

	private void onCloneClicked(final EstimationDTO estimation) {
		SelectClientDialog dia = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(ClientDTO client) {
				CloneEstimationPlace ep = new CloneEstimationPlace();
				ep.setClientId(client.getId());
				ep.setEstimationId(estimation.getId());
				presenter.goTo(ep);
			}
		});
		dia.setEventBus(eventBus);
		dia.showCentered();
	}

	public void onPdfClicked(EstimationDTO estimation) {
		if(estimation.getId() == null){
			return;
		}
		PDFUtils.generateEstimationPdf(estimation.getId());
	}

	public void onConvertToInvoiceClicked(final EstimationDTO estimation) {
		FromEstimationInvoicePlace p = new FromEstimationInvoicePlace();
		p.setEstimationId(estimation.getId());
		presenter.goTo(p);
	}

	public void onDeleteClicked(final EstimationDTO estimation) {
		Notification.showConfirm(I18N.INSTANCE.confirmEstimationDeletion(), new NotificationCallback<Boolean>() {
			
			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ServerFacade.estimation.remove(Configuration.getBusinessId(), estimation.getClient().getId(), estimation.getId(), new WrappedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							eventBus.fireEvent(new DocumentDeleteEvent(estimation));
						}

						@Override
						public void onException(Throwable caught) {
							Notification.showMessage(I18N.INSTANCE.errorServerCommunication());		
						}
					});
				}
			}
		});

	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

}
