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
import com.novadart.novabill.frontend.client.place.creditnote.ModifyCreditNotePlace;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.frontend.client.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListBundle;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteCellImpl extends QuickViewCell<CreditNoteDTO> implements CreditNoteCell {

	private Presenter presenter;
	private EventBus eventBus;


	@Override
	protected void renderVisible(
			com.google.gwt.cell.client.Cell.Context context, CreditNoteDTO value,
			SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().main()+" '>");
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
			com.google.gwt.cell.client.Cell.Context context, CreditNoteDTO value,
			SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().upper()+"'>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().total()+"'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='"+QuickViewListBundle.INSTANCE.quickViewListCss().tools()+"'>");
		sb.appendHtmlConstant("<span class='"+QuickViewListBundle.INSTANCE.quickViewListCss().openCreditNote()+"'>");
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


	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteCell#setPresenter(com.novadart.novabill.frontend.client.presenter.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteCell#setEventBus(com.google.web.bindery.event.shared.EventBus)
	 */
	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	protected void onClick(CreditNoteDTO value, EventTarget eventTarget) {
		if(isPdf(eventTarget)){
			onPdfClicked(value);
		} else if(isDelete(eventTarget)){
			onDeleteClicked(value);
		} else if(isOpenCreditNote(eventTarget)){
			onOpenCreditNoteClicked(value);
		}
	}

	private boolean isOpenCreditNote(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement open = et.cast();
			return open.getClassName().contains(QuickViewListBundle.INSTANCE.quickViewListCss().openCreditNote());

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



	private void onOpenCreditNoteClicked(CreditNoteDTO creditNote) {
		ModifyCreditNotePlace p = new ModifyCreditNotePlace();
		p.setCreditNoteId(creditNote.getId());
		presenter.goTo(p);
	}


	/* (non-Javadoc)
	 * @see com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteCell#onPdfClicked(com.novadart.novabill.shared.client.dto.CreditNoteDTO)
	 */
	@Override
	public void onPdfClicked(CreditNoteDTO creditNote) {
		if(creditNote.getId() == null){
			return;
		}
		PDFUtils.generateCreditNotePdf(creditNote.getId());
	}

	private void onDeleteClicked(final CreditNoteDTO creditNote) {
		Notification.showConfirm(I18N.INSTANCE.confirmCreditNoteDeletion(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if(value){
					ServerFacade.INSTANCE.getCreditNoteService().remove(Configuration.getBusinessId(), creditNote.getClient().getId(), creditNote.getId(), new ManagedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							eventBus.fireEvent(new DocumentDeleteEvent(creditNote));
						}
						
					});
				}
			}
		});
	}

}
