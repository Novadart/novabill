package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.ClientUpdateEvent;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.creditnote.FromInvoiceCreditNotePlace;
import com.novadart.novabill.frontend.client.place.invoice.CloneInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.ModifyInvoicePlace;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.frontend.client.widget.dialog.selectclient.SelectClientDialog;
import com.novadart.novabill.frontend.client.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListBundle;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceCellImpl extends QuickViewCell<InvoiceDTO> implements InvoiceCell {

	private Presenter presenter;
	private EventBus eventBus;

	@Override
	protected void renderVisible(
			com.google.gwt.cell.client.Cell.Context context, InvoiceDTO value,
			SafeHtmlBuilder sb) {
//
//		if(Configuration.isPremium()) {
//			sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().main()+" "+(value.getPayed() ? "invoice-payed" : "invoice-not-payed")+"'>");
//		} else {
//			sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().main()+" '>");
//		}
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().id()+"'>");
		sb.append(value.getDocumentID());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().date()+"'>");
		sb.appendEscaped(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(value.getAccountingDocumentDate()));
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().name()+"'>");
		sb.appendEscaped(value.getClient().getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");
	}

	@Override
	protected void renderDetails(
			com.google.gwt.cell.client.Cell.Context context, InvoiceDTO value,
			SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().upper()+"'>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().total()+"'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</span>");
//		if(Configuration.isPremium()) {
//			sb.appendHtmlConstant("<span class='payed payed-"+value.getPayed()+"'>");
//			sb.appendEscaped(value.getPayed() ? I18N.INSTANCE.payed() : I18N.INSTANCE.notPayed());
//			sb.appendHtmlConstant("</span>");
//		}
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().tools()+"'>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().openInvoice()+"'>");
		sb.appendEscaped(I18N.INSTANCE.modify());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().clone()+"'>");
		sb.appendEscaped(I18N.INSTANCE.clone());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().creditNote()+"'>");
		sb.appendEscaped(I18N.INSTANCE.creditNote());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().downloadAsPDF()+"'>");
		sb.appendEscaped(I18N.INSTANCE.download());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().delete()+"'>");
		sb.appendEscaped(I18N.INSTANCE.delete());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");
	}


	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.InvoiceCell#setPresenter(com.novadart.novabill.frontend.client.presenter.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.InvoiceCell#setEventBus(com.google.web.bindery.event.shared.EventBus)
	 */
	@Override
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
			return open.getClassName().contains(QuickViewListBundle.INSTANCE.quickViewListCss().openInvoice());

		} else {
			return false;
		}
	}

	private boolean isPdf(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement pdf = et.cast();
			return QuickViewListBundle.INSTANCE.quickViewListCss().downloadAsPDF().equals(pdf.getClassName());

		} else {
			return false;
		}
	}

	private boolean isDelete(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement delete = et.cast();
			return QuickViewListBundle.INSTANCE.quickViewListCss().delete().equals(delete.getClassName());

		} else {
			return false;
		}
	}

	private boolean isCreditNote(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement pdf = et.cast();
			return QuickViewListBundle.INSTANCE.quickViewListCss().creditNote().equals(pdf.getClassName());

		} else {
			return false;
		}
	}

	private boolean isClone(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement img = et.cast();
			return QuickViewListBundle.INSTANCE.quickViewListCss().clone().equals(img.getClassName());

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
				cip.setClientId(client.getId());
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

	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.InvoiceCell#onPdfClicked(com.novadart.novabill.shared.client.dto.InvoiceDTO)
	 */
	@Override
	public void onPdfClicked(InvoiceDTO invoice) {
		if(invoice.getId() == null){
			return;
		}
		PDFUtils.generateInvoicePdf(invoice.getId());
	}

	private void onDeleteClicked(final InvoiceDTO invoice) {
		Notification.showConfirm(I18N.INSTANCE.confirmInvoiceDeletion(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ServerFacade.INSTANCE.getInvoiceService().remove(Configuration.getBusinessId(), invoice.getClient().getId(), invoice.getId(), new ManagedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							eventBus.fireEvent(new DocumentDeleteEvent(invoice));
						}

					});
				}
			}
		});

	}

	private void onPayedSwitchClicked(final InvoiceDTO invoice) {
		ServerFacade.INSTANCE.getInvoiceService().setPayed(Configuration.getBusinessId(), invoice.getClient().getId(), invoice.getId(), !invoice.getPayed(), new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new ClientUpdateEvent(invoice.getClient()));
			}

		});
	}

}
