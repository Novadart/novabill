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
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

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
		sb.appendHtmlConstant("<span class='openEstimation'>");
		sb.appendEscaped(I18N.INSTANCE.openEstimation());
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
		}
	}

	private boolean isOpenEstimation(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement delete = et.cast();
			return "openEstimation".equals(delete.getClassName());

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

	public void onPdfClicked(EstimationDTO estimation) {
		if(estimation.getId() == null){
			return;
		}
		PDFUtils.generateEstimationPdf(estimation.getId());
	}

	public void onConvertToInvoiceClicked(EstimationDTO estimation) {
		ServerFacade.invoice.createFromEstimation(estimation, new WrappedAsyncCallback<InvoiceDTO>() {

			@Override
			public void onSuccess(InvoiceDTO result) {
				DataWatcher.getInstance().fireInvoiceEvent();
				DataWatcher.getInstance().fireEstimationEvent();
				DataWatcher.getInstance().fireClientDataEvent();
				DataWatcher.getInstance().fireStatsEvent();

				InvoicePlace ip = new InvoicePlace();
				ip.setInvoiceId(result.getId());
				presenter.goTo(ip);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
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
