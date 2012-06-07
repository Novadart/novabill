package com.novadart.novabill.frontend.client.ui.west;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientWidget extends FocusPanel {

	private static ClientWidgetUiBinder uiBinder = GWT
			.create(ClientWidgetUiBinder.class);

	interface ClientWidgetUiBinder extends UiBinder<Widget, ClientWidget> {
	}

	@UiField Label name;
	@UiField Label address;
	@UiField Label address2;
	@UiField Label address3;

	private ClientDTO client;
	
	public ClientWidget() {
		setWidget(uiBinder.createAndBindUi(this));
		setStyleName("ClientWidget");
		setVisible(false);
	}

	public void setClient(ClientDTO clientDTO){
		client = clientDTO;
		
		name.setText(client.getName());
		address.setText(client.getAddress());
		address2.setText(client.getPostcode() + " " 
				+ client.getCity() + " (" + client.getProvince() +") " + client.getCountry());
		
		boolean hasPhone = client.getPhone()!=null && !client.getPhone().isEmpty();
		boolean hasFax = client.getFax()!=null && !client.getFax().isEmpty();
		address3.setText(( (hasPhone?"Tel. "+client.getPhone():"") 
				+ (hasFax?" Fax "+client.getFax():"").trim() 
				+ " " + I18N.INSTANCE.vatID()+" "+client.getVatID() ).trim());
		
		setVisible(true);
	}
	
	public void clear(){
		setVisible(false);
	}
	
	public ClientDTO getClient() {
		return client;
	}


}
