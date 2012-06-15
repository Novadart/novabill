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
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceCell extends QuickViewCell<InvoiceDTO> {
	
	private Presenter presenter;
	
	
	@Override
	protected void renderVisible(
			com.google.gwt.cell.client.Cell.Context context, InvoiceDTO value,
			SafeHtmlBuilder sb) {
		
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
	protected void renderDetails(
			com.google.gwt.cell.client.Cell.Context context, InvoiceDTO value,
			SafeHtmlBuilder sb) {
		
		sb.appendHtmlConstant("<div class='total'>");
		sb.appendEscaped(I18N.INSTANCE.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='tools'>");
		sb.appendHtmlConstant("<span class='openInvoice'>");
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
	protected void onClick(InvoiceDTO value, EventTarget eventTarget) {
		if(isPdf(eventTarget)){
			onPdfClicked(value);
		} else if(isDelete(eventTarget)){
			onDeleteClicked(value);
		} else if(isOpenInvoice(eventTarget)){
			onOpenInvoiceClicked(value);
		} 
	}
	
	private boolean isOpenInvoice(EventTarget et){
		if(SpanElement.is(et)){
			SpanElement delete = et.cast();
			return "openInvoice".equals(delete.getClassName());
			
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

	private void onOpenInvoiceClicked(InvoiceDTO invoice) {
		if(presenter != null){
			InvoicePlace ip = new InvoicePlace();
			ip.setInvoiceId(invoice.getId());
			presenter.goTo(ip);
		}
	}
	
	private void onPdfClicked(InvoiceDTO invoice) {
		if(invoice.getId() == null){
			return;
		}
		PDFUtils.generateInvoicePdf(invoice.getId());
	}

	private void onDeleteClicked(InvoiceDTO invoice) {
		if(Notification.showYesNoRequest(I18N.INSTANCE.confirmInvoiceDeletion())){
			ServerFacade.invoice.remove(invoice.getId(), new WrappedAsyncCallback<Void>() {
				
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
	
}
