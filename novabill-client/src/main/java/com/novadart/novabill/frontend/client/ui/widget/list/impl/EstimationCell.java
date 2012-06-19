package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.dialog.SelectClientDialog;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationCell extends QuickViewCell<EstimationDTO> {

	private Presenter presenter;

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
		sb.appendEscaped(I18N.INSTANCE.open());
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
		if(presenter != null){
			EstimationPlace ep = new EstimationPlace();
			ep.setEstimationId(estimation.getId());
			presenter.goTo(ep);
		}
	}
	
	private void onCloneClicked(final EstimationDTO estimation) {
		SelectClientDialog dia = new SelectClientDialog(new SelectClientDialog.Handler() {
			
			@Override
			public void onClientSelected(ClientDTO client) {
				EstimationPlace ep = new EstimationPlace();
				ep.setDataForNewEstimation(client, estimation);
				presenter.goTo(ep);
			}
		});
		dia.showCentered();
	}

	public void onPdfClicked(EstimationDTO estimation) {
		if(estimation.getId() == null){
			return;
		}
		PDFUtils.generateEstimationPdf(estimation.getId());
	}

	public void onConvertToInvoiceClicked(final EstimationDTO estimation) {
		ServerFacade.invoice.getNextInvoiceDocumentID(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				if(result == null){
					return;
				}
				InvoicePlace ip = new InvoicePlace();
				ip.setDataForNewInvoice(result, estimation);
				presenter.goTo(ip);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}

	public void onDeleteClicked(EstimationDTO estimation) {
		if(Notification.showYesNoRequest(I18N.INSTANCE.confirmEstimationDeletion())){
			ServerFacade.estimation.remove(estimation.getId(), new WrappedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					DataWatcher.getInstance().fireEstimationEvent();
				}

				@Override
				public void onException(Throwable caught) {
					Notification.showYesNoRequest(I18N.INSTANCE.errorServerCommunication());		
				}
			});
		}

	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
