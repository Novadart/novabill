package com.novadart.novabill.frontend.client.view.widget.list.impl;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.ClientUpdateEvent;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.creditnote.FromInvoiceCreditNotePlace;
import com.novadart.novabill.frontend.client.place.invoice.CloneInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.ModifyInvoicePlace;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.frontend.client.view.View.Presenter;
import com.novadart.novabill.frontend.client.view.widget.dialog.SelectClientDialog;
import com.novadart.novabill.frontend.client.view.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.view.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceCell extends QuickViewCell<InvoiceDTO> {

	private Presenter presenter;
	private EventBus eventBus;

	@Override
	protected void renderVisible(
			com.google.gwt.cell.client.Cell.Context context, InvoiceDTO value,
			SafeHtmlBuilder sb) {
		
		if(Configuration.isPremium()) {
			sb.appendHtmlConstant("<div class='main "+(value.getPayed() ? "invoice-payed" : "invoice-not-payed")+"'>");
		} else {
			sb.appendHtmlConstant("<div class='main '>");
		}
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
	protected void renderDetails(
			com.google.gwt.cell.client.Cell.Context context, InvoiceDTO value,
			SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='upper'>");
		sb.appendHtmlConstant("<span class='total'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</span>");
		if(Configuration.isPremium()) {
			sb.appendHtmlConstant("<span class='payed payed-"+value.getPayed()+"'>");
			sb.appendEscaped(value.getPayed() ? I18N.INSTANCE.payed() : I18N.INSTANCE.notPayed());
			sb.appendHtmlConstant("</span>");
		}
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='tools'>");
		sb.appendHtmlConstant("<span class='button openInvoice'>");
		sb.appendEscaped(I18N.INSTANCE.modify());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='clone'>");
		sb.appendEscaped(I18N.INSTANCE.clone());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='creditNote'>");
		sb.appendEscaped(I18N.INSTANCE.creditNote());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='downloadAsPDF'>");
		sb.appendEscaped("PDF");
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='delete'>");
		sb.appendEscaped(I18N.INSTANCE.delete());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");
	}


	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	protected void onClick(InvoiceDTO value, EventTarget eventTarget) {
		if(isPdf(eventTarget)){
			onPdfClicked(value);
		} else if(isDelete(eventTarget)){
			onDeleteClicked(value);
		} else if(isOpenInvoice(eventTarget)){
			onOpenInvoiceClicked(value);
		} else if(isPayedSwitch(eventTarget)){
			onPayedSwitchClicked(value);
		} else if(isClone(eventTarget)) {
			onCloneClicked(value);
		} else if(isCreditNote(eventTarget)) {
			onCreditNoteClicked(value);
		}
	}

	private boolean isOpenInvoice(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement open = et.cast();
			return open.getClassName().contains("openInvoice");

		} else {
			return false;
		}
	}

	private boolean isPdf(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement pdf = et.cast();
			return "downloadAsPDF".equals(pdf.getClassName());

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
	
	private boolean isCreditNote(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement pdf = et.cast();
			return "creditNote".equals(pdf.getClassName());

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

	private boolean isPayedSwitch(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement payed = et.cast();
			return payed.getClassName().contains("payed");

		} else {
			return false;
		}
	}

	private void onOpenInvoiceClicked(InvoiceDTO invoice) {
		ModifyInvoicePlace p = new ModifyInvoicePlace();
		p.setInvoiceId(invoice.getId());
		presenter.goTo(p);
	}

	private void onCloneClicked(final InvoiceDTO invoice) {
		SelectClientDialog dia = new SelectClientDialog(new SelectClientDialog.Handler() {
			
			@Override
			public void onClientSelected(ClientDTO client) {
				CloneInvoicePlace cip = new CloneInvoicePlace();
				cip.setClientId(invoice.getClient().getId());
				cip.setInvoiceId(invoice.getId());
				presenter.goTo(cip);
			}
		});
		dia.setEventBus(eventBus);
		dia.showCentered();
	}
	
	private void onCreditNoteClicked(final InvoiceDTO invoice) {
		FromInvoiceCreditNotePlace p = new FromInvoiceCreditNotePlace();
		p.setInvoiceId(invoice.getId());
		presenter.goTo(p);
	}

	private void onPdfClicked(InvoiceDTO invoice) {
		if(invoice.getId() == null){
			return;
		}
		PDFUtils.generateInvoicePdf(invoice.getId());
	}

	private void onDeleteClicked(final InvoiceDTO invoice) {
		if(Notification.showYesNoRequest(I18N.INSTANCE.confirmInvoiceDeletion())){
			ServerFacade.invoice.remove(Configuration.getBusinessId(), invoice.getClient().getId(), invoice.getId(), new WrappedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					eventBus.fireEvent(new DocumentDeleteEvent(invoice));
				}

				@Override
				public void onException(Throwable caught) {
					Notification.showYesNoRequest(I18N.INSTANCE.errorServerCommunication());		
				}
			});
		}

	}

	private void onPayedSwitchClicked(final InvoiceDTO invoice) {
		ServerFacade.invoice.setPayed(Configuration.getBusinessId(), invoice.getClient().getId(), invoice.getId(), !invoice.getPayed(), new WrappedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new ClientUpdateEvent(invoice.getClient()));
			}

			@Override
			public void onException(Throwable caught) {

			}
		});
	}

}
