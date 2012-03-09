package com.novadart.novabill.frontend.client.ui.west;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientCell extends AbstractCell<ClientDTO> {

	public interface Handler {
		public void onClientSelected(ClientDTO client);
	}

	private Handler handler;
	
	public ClientCell() {
		super("click");
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void render(Cell.Context context, ClientDTO value, SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='client'>");
		sb.appendHtmlConstant("<div class='main'>");

		sb.appendHtmlConstant("<span class='name'>");
		sb.appendEscaped(value.getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='address'>");
		sb.appendEscaped(value.getAddress());
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='address-2'>");
		sb.appendEscaped(value.getPostcode() + " " 
				+ value.getCity() + " (" + value.getProvince() +") " + value.getCountry());
		sb.appendHtmlConstant("</div>");


		boolean hasPhone = value.getPhone()!=null && !value.getPhone().isEmpty();
		boolean hasFax = value.getFax()!=null && !value.getFax().isEmpty();
		sb.appendHtmlConstant("<div class='address-3'>");
		sb.appendEscaped( ( (hasPhone?"Tel. "+value.getPhone():"") 
				+ (hasFax?" Fax "+value.getFax():"").trim() 
				+ " " + I18N.get.vatID()+" "+value.getVatID() ).trim() );
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("</div>");

	}
	
	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, ClientDTO value, NativeEvent event,
			ValueUpdater<ClientDTO> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		
		if(value == null){
			return;
		}
		
		handler.onClientSelected(value);
	}

}
