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
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.place.TransportDocumentPlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.dialog.SelectClientDialog;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentCell extends QuickViewCell<TransportDocumentDTO> {

	private Presenter presenter;

	@Override
	protected void renderDetails(
			com.google.gwt.cell.client.Cell.Context context,
			TransportDocumentDTO value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<div class='upper'>");
		sb.appendHtmlConstant("<span class='total'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='createInvoice'>");
		sb.appendEscaped(I18N.INSTANCE.createInvoice());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='tools'>");
		sb.appendHtmlConstant("<span class='button openTransportDocument'>");
		sb.appendEscaped(I18N.INSTANCE.open());
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
			TransportDocumentDTO value, SafeHtmlBuilder sb) {
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
	protected void onClick(TransportDocumentDTO value, EventTarget eventTarget) {
		if(isPdf(eventTarget)){
			onPdfClicked(value);
		} else if(isDelete(eventTarget)){
			onDeleteClicked(value);
		} else if(isOpenTransportDocument(eventTarget)){
			onOpenTransportDocumentClicked(value);
		} else if(isClone(eventTarget)){
			onCloneClicked(value);
		} else if (isCreateInvoice(eventTarget)){
			onCreateInvoiceClicked(value);
		}
	}

	private boolean isCreateInvoice(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement open = et.cast();
			return open.getClassName().contains("createInvoice");

		} else {
			return false;
		}
	}
	
	private boolean isOpenTransportDocument(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement open = et.cast();
			return open.getClassName().contains("openTransportDocument");

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

	public void onOpenTransportDocumentClicked(TransportDocumentDTO transportDocument) {
		if(presenter != null){
			TransportDocumentPlace tdp = new TransportDocumentPlace();
			tdp.setTransportDocumentId(transportDocument.getId());
			presenter.goTo(tdp);
		}
	}
	
	private void onCloneClicked(final TransportDocumentDTO transportDocument) {
		SelectClientDialog dia = new SelectClientDialog(new SelectClientDialog.Handler() {
			
			@Override
			public void onClientSelected(ClientDTO client) {
				TransportDocumentPlace tdp = new TransportDocumentPlace();
				tdp.setDataForNewTransportDocument(client, transportDocument);
				presenter.goTo(tdp);
			}
		});
		dia.showCentered();
	}

	public void onPdfClicked(TransportDocumentDTO transportDocument) {
		if(transportDocument.getId() == null){
			return;
		}
		PDFUtils.generateTransportDocumentPdf(transportDocument.getId());
	}

	public void onCreateInvoiceClicked(final TransportDocumentDTO transportDocument) {
		if(transportDocument.getId() == null){
			return;
		}
		if(presenter != null){
			ServerFacade.invoice.getNextInvoiceDocumentID(new WrappedAsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					if(result == null){
						return;
					}
					InvoicePlace ip = new InvoicePlace();
					ip.setDataForNewInvoice(result, transportDocument);
					presenter.goTo(ip);
				}

				@Override
				public void onException(Throwable caught) {
					
				}
			});
		}
	}

	public void onDeleteClicked(TransportDocumentDTO transportDocument) {
		if(Notification.showYesNoRequest(I18N.INSTANCE.confirmTransportDocumentDeletion())){
			ServerFacade.transportDocument.remove(transportDocument.getId(), new WrappedAsyncCallback<Void>() {

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
