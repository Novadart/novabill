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
import com.novadart.novabill.frontend.client.place.CreditNotePlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteCell extends QuickViewCell<CreditNoteDTO> {

	private Presenter presenter;


	@Override
	protected void renderVisible(
			com.google.gwt.cell.client.Cell.Context context, CreditNoteDTO value,
			SafeHtmlBuilder sb) {
		
		sb.appendHtmlConstant("<div class='main '>");
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
			com.google.gwt.cell.client.Cell.Context context, CreditNoteDTO value,
			SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='upper'>");
		sb.appendHtmlConstant("<span class='total'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</span>");
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='tools'>");
		sb.appendHtmlConstant("<span class='button openCreditNote'>");
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


	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
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
			return open.getClassName().contains("openCreditNote");

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



	private void onOpenCreditNoteClicked(CreditNoteDTO creditNote) {
		if(presenter != null){
			CreditNotePlace cnp = new CreditNotePlace();
			cnp.setCreditNoteId(creditNote.getId());
			presenter.goTo(cnp);
		}
	}


	private void onPdfClicked(CreditNoteDTO creditNote) {
		if(creditNote.getId() == null){
			return;
		}
		PDFUtils.generateCreditNotePdf(creditNote.getId());
	}

	private void onDeleteClicked(CreditNoteDTO creditNote) {
		if(Notification.showYesNoRequest(I18N.INSTANCE.confirmCreditNoteDeletion())){
			ServerFacade.creditNote.remove(creditNote.getId(), new WrappedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					DataWatcher.getInstance().fireCreditNoteEvent();
				}

				@Override
				public void onException(Throwable caught) {
					Notification.showYesNoRequest(I18N.INSTANCE.errorServerCommunication());
				}
			});
			
		}

	}

}
