package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.Image;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewCell;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceCell extends QuickViewCell<InvoiceDTO> {
	
	public static interface Handler {
		public void onPdfClicked(InvoiceDTO invoice);
		public void onDeleteClicked(InvoiceDTO invoice);
		public void onOpenInvoiceClicked(InvoiceDTO invoice);
	}
	
	private Handler handler;

	@Override
	protected void render(com.google.gwt.cell.client.Cell.Context context,
			InvoiceDTO value, SafeHtmlBuilder sb, boolean isSelected) {

		if(isSelected){
			sb.appendHtmlConstant("<div class='invoice clicked'>");
		} else {
			sb.appendHtmlConstant("<div class='invoice'>");
		}
		sb.appendHtmlConstant("<div class='main'>");
		sb.appendHtmlConstant("<span class='id'>");
		sb.append(value.getDocumentID());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='date'>");
		sb.appendEscaped(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(value.getInvoiceDate()));
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='name'>");
		sb.appendEscaped(value.getClient().getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");

		if(isSelected){
			sb.appendHtmlConstant("<div class='details'>");
			
			sb.appendHtmlConstant("<div class='total'>");
			sb.appendEscaped(I18N.get.totalAfterTaxesForItem()+" "+NumberFormat.getCurrencyFormat().format(value.getTotal()));
			sb.appendHtmlConstant("</div>");

			sb.appendHtmlConstant("<div class='tools'>");
			sb.appendHtmlConstant("<span class='openInvoice'>");
			sb.appendEscaped(I18N.get.openInvoice());
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("<span class='downloadAsPDF'>");
			sb.appendHtmlConstant("<img class='pdf' src='"+Image.get.pdf().getSafeUri().asString()+"'>");
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("<span class='delete'>");
			sb.appendEscaped(I18N.get.delete());
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("</div>");
			
			sb.appendHtmlConstant("</div>");
		}
		
		sb.appendHtmlConstant("</div>");
		
	}

	@Override
	protected boolean itemsAreEqual(InvoiceDTO item1, InvoiceDTO item2) {
		return item1.getId().equals(item2.getId());
	}
	
	
	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, InvoiceDTO value, NativeEvent event,
			ValueUpdater<InvoiceDTO> valueUpdater) {
		
		if(value == null){
			return;
		}
		
		if("click".equals(event.getType())){
			if(isPdf(event.getEventTarget())){
				handler.onPdfClicked(value);
			} else if(isDelete(event.getEventTarget())){
				handler.onDeleteClicked(value);
			} else if(isOpenInvoice(event.getEventTarget())){
				handler.onOpenInvoiceClicked(value);
			} else {
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}
			
		} else {
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
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
		if(ImageElement.is(et)){
			ImageElement img = et.cast();
			return "pdf".equals(img.getClassName());
			
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


	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
