package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.dialog.SelectClientDialog;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceCell extends QuickViewCell<InvoiceDTO> {

	private Presenter presenter;


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
		if(presenter != null){
			InvoicePlace ip = new InvoicePlace();
			ip.setInvoiceId(invoice.getId());
			presenter.goTo(ip);
		}
	}

	private void onCloneClicked(final InvoiceDTO invoice) {
		ServerFacade.invoice.getNextInvoiceDocumentID(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long result) {
				if(result == null){
					return;
				}
				SelectClientDialog dia = new SelectClientDialog(new SelectClientDialog.Handler() {
					
					@Override
					public void onClientSelected(ClientDTO client) {
						InvoicePlace ip = new InvoicePlace();
						ip.setDataForNewInvoice(client, result, invoice);
						presenter.goTo(ip);
					}
				});
				dia.showCentered();
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}
	
	private void onCreditNoteClicked(final InvoiceDTO invoice) {
		ServerFacade.creditNote.getNextCreditNoteDocumentID(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				CreditNotePlace cnp = new CreditNotePlace();
				cnp.setDataForNewCreditNote(result, invoice);
				presenter.goTo(cnp);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showYesNoRequest(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}

	private void onPdfClicked(InvoiceDTO invoice) {
		if(invoice.getId() == null){
			return;
		}
		PDFUtils.generateInvoicePdf(invoice.getId());
	}

	private void onDeleteClicked(InvoiceDTO invoice) {
		if(Notification.showYesNoRequest(I18N.INSTANCE.confirmInvoiceDeletion())){
			ServerFacade.invoice.remove(Configuration.getBusinessId(), invoice.getClient().getId(), invoice.getId(), new WrappedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					DataWatcher.getInstance().fireInvoiceEvent();
					DataWatcher.getInstance().fireStatsEvent();
				}

				@Override
				public void onException(Throwable caught) {
					Notification.showYesNoRequest(I18N.INSTANCE.errorServerCommunication());		
				}
			});
		}

	}

	private void onPayedSwitchClicked(InvoiceDTO invoice) {
		ServerFacade.invoice.setPayed(Configuration.getBusinessId(), invoice.getClient().getId(), invoice.getId(), !invoice.getPayed(), new WrappedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				DataWatcher.getInstance().fireClientDataEvent();
			}

			@Override
			public void onException(Throwable caught) {

			}
		});
	}

}
