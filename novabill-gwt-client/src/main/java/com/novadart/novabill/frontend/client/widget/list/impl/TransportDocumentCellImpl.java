package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.transportdocument.CloneTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.ModifyTransportDocumentPlace;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.frontend.client.widget.dialog.selectclient.SelectClientDialog;
import com.novadart.novabill.frontend.client.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListBundle;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentCellImpl extends QuickViewCell<TransportDocumentDTO> implements TransportDocumentCell {

	private Presenter presenter;
	private EventBus eventBus;

	@Override
	protected void renderDetails(
			com.google.gwt.cell.client.Cell.Context context,
			TransportDocumentDTO value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().upper()+"'>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().total()+"'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().createInvoice()+"'>");
		sb.appendEscaped(I18N.INSTANCE.createInvoice());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().tools()+"'>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().openTransportDocument()+"'>");
		sb.appendEscaped(I18N.INSTANCE.modify());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().downloadAsPDF()+"'>");
		sb.appendEscaped(I18N.INSTANCE.download());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().delete()+"'>");
		sb.appendEscaped(I18N.INSTANCE.delete());
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");
	}

	@Override
	protected void renderVisible(
			com.google.gwt.cell.client.Cell.Context context,
			TransportDocumentDTO value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().main()+"'>");
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
			return open.getClassName().contains(QuickViewListBundle.INSTANCE.quickViewListCss().createInvoice());

		} else {
			return false;
		}
	}
	
	private boolean isOpenTransportDocument(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement open = et.cast();
			return open.getClassName().contains(QuickViewListBundle.INSTANCE.quickViewListCss().openTransportDocument());

		} else {
			return false;
		}
	}

	private boolean isPdf(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement img = et.cast();
			return QuickViewListBundle.INSTANCE.quickViewListCss().downloadAsPDF().equals(img.getClassName());

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

	private boolean isDelete(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement delete = et.cast();
			return QuickViewListBundle.INSTANCE.quickViewListCss().delete().equals(delete.getClassName());

		} else {
			return false;
		}
	}

	private void onOpenTransportDocumentClicked(TransportDocumentDTO transportDocument) {
		ModifyTransportDocumentPlace tdp = new ModifyTransportDocumentPlace();
		tdp.setTransportDocumentId(transportDocument.getId());
		presenter.goTo(tdp);
	}
	
	private void onCloneClicked(final TransportDocumentDTO transportDocument) {
		SelectClientDialog dia = new SelectClientDialog(new SelectClientDialog.Handler() {
			
			@Override
			public void onClientSelected(final ClientDTO client) {
				CloneTransportDocumentPlace p = new CloneTransportDocumentPlace();
				p.setClientId(client.getId());
				p.setTransportDocumentId(transportDocument.getId());
				presenter.goTo(p);
			}
		});
		dia.setEventBus(eventBus);
		dia.showCentered();
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentCell#onPdfClicked(com.novadart.novabill.shared.client.dto.TransportDocumentDTO)
	 */
	@Override
	public void onPdfClicked(TransportDocumentDTO transportDocument) {
		if(transportDocument.getId() == null){
			return;
		}
		PDFUtils.generateTransportDocumentPdf(transportDocument.getId());
	}

	private void onCreateInvoiceClicked(final TransportDocumentDTO transportDocument) {
//		FromTransportDocumentInvoicePlace p = new FromTransportDocumentInvoicePlace();
//		p.setTransportDocumentId(transportDocument.getId());
//		presenter.goTo(p);
	}

	private void onDeleteClicked(final TransportDocumentDTO transportDocument) {
		Notification.showConfirm(I18N.INSTANCE.confirmTransportDocumentDeletion(), new NotificationCallback<Boolean>() {
			
			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ServerFacade.INSTANCE.getTransportdocumentService().remove(Configuration.getBusinessId(), transportDocument.getClient().getId(), transportDocument.getId(), new ManagedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							eventBus.fireEvent(new DocumentDeleteEvent(transportDocument));
						}

					});
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentCell#setPresenter(com.novadart.novabill.frontend.client.presenter.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentCell#setEventBus(com.google.web.bindery.event.shared.EventBus)
	 */
	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

}
