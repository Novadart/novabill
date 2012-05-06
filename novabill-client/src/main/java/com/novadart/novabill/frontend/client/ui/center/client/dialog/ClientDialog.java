package com.novadart.novabill.frontend.client.ui.center.client.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.widget.dialog.Dialog;
import com.novadart.novabill.frontend.client.ui.widget.validation.EmailValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NotEmptyValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.PostcodeValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.SsnOrVatIdValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.VatIdValidation;
import com.novadart.novabill.shared.client.data.Province;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientDialog extends Dialog {

	private static ClientDialogUiBinder uiBinder = GWT
			.create(ClientDialogUiBinder.class);

	interface ClientDialogUiBinder extends UiBinder<Widget, ClientDialog> {
	}

	private static final ClientDialog instance = new ClientDialog();

	public static ClientDialog getInstance(){
		instance.clearData();
		return instance;
	}

	@UiField(provided=true) ValidatedTextBox companyName;
	@UiField TextBox address;
	@UiField TextBox city;
	@UiField(provided=true) ListBox province;
	@UiField TextBox country;
	@UiField(provided=true) ValidatedTextBox postcode;
	@UiField(provided=true) ValidatedTextBox phone;
	@UiField(provided=true) ValidatedTextBox mobile;
	@UiField(provided=true) ValidatedTextBox fax;
	@UiField(provided=true) ValidatedTextBox email;
	@UiField TextBox web;
	@UiField(provided=true) ValidatedTextBox vatID;
	@UiField(provided=true) ValidatedTextBox ssn;

	@UiField Button ok;
	
	private ClientDTO client = null;

	private ClientDialog() {
		companyName = new ValidatedTextBox(new NotEmptyValidation());
		vatID =  new ValidatedTextBox(new VatIdValidation());
		ssn =  new ValidatedTextBox(new SsnOrVatIdValidation());
		postcode = new ValidatedTextBox(new PostcodeValidation());
		
		NumberValidation nv = new NumberValidation(true);
		phone = new ValidatedTextBox(nv);
		mobile = new ValidatedTextBox(nv);
		fax = new ValidatedTextBox(nv);
		
		email = new ValidatedTextBox(new EmailValidation(true));
		
		province = new ListBox();
		province.addItem("");
		for (Province p : Province.values()) {
			province.addItem(p.name());
		}
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("ClientDialog panel");
	}

	@UiFactory
	I18N getI18N(){
		return I18N.get;
	}

	public void setClient(ClientDTO client) {
		this.client = client;

		companyName.setText(client.getName());
		address.setText(client.getAddress());
		city.setText(client.getCity());
		
		String cProv = client.getProvince();
		if(cProv != null){
			Province[] provs = Province.values();
			int selIndex = 0;
			for (int i=0; i<provs.length; i++) {
				if(cProv.equalsIgnoreCase(provs[i].name())){
					selIndex = i+1;
				}
			}
			province.setSelectedIndex(selIndex);
		}
		
		country.setText(client.getCountry());
		postcode.setText(client.getPostcode());
		phone.setText(client.getPhone());
		mobile.setText(client.getMobile());
		fax.setText(client.getFax());
		email.setText(client.getEmail());
		web.setText(client.getWeb());
		vatID.setText(client.getVatID());
		ssn.setText(client.getSsn());
		
		ok.setText(I18N.get.saveModifications());
	}


	@UiHandler("ok")
	void onOkClicked(ClickEvent e){
		if(!validate()){
			return;
		}

		ClientDTO client = this.client==null ? new ClientDTO() : this.client;
		client.setAddress(address.getText());
		client.setCity(city.getText());
		client.setCountry(country.getText());
		client.setEmail(email.getText());
		client.setFax(fax.getText());
		client.setMobile(mobile.getText());
		client.setName(companyName.getText());
		client.setPhone(phone.getText());
		client.setPostcode(postcode.getText());
		client.setProvince(province.getItemText(province.getSelectedIndex()));
		client.setSsn(ssn.getText());
		client.setVatID(vatID.getText());
		client.setWeb(web.getText());

		if(this.client == null) {
			ServerFacade.client.add(client, new AuthAwareAsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					DataWatcher.getInstance().fireClientEvent();
					DataWatcher.getInstance().fireStatsEvent();
					hide();
				}

				@Override
				public void onException(Throwable caught) {
					Window.alert(I18N.get.errorServerCommunication());
				}
			});
		} else {
			
			ServerFacade.client.update(client, new AuthAwareAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					DataWatcher.getInstance().fireClientEvent();
					hide();
				}

				@Override
				public void onException(Throwable caught) {
					Window.alert(I18N.get.errorServerCommunication());
				}
			});
			
		}
	}

	@UiHandler("cancel")
	void onCancelClicked(ClickEvent e){
		hide();
	}

	private void clearData(){
		client = null;
		province.setSelectedIndex(0);
		for (TextBox t : new TextBox[]{address, city, country, web}) {
			t.setText("");
		}
		for (ValidatedTextBox tb: new ValidatedTextBox[]{vatID, companyName, 
				ssn, postcode, phone, mobile, fax, email}){
			tb.reset();
		}
		ok.setText(I18N.get.createClient());
	}

	private boolean validate(){
		boolean isValid = true;
		for (ValidatedTextBox tb: new ValidatedTextBox[]{vatID, companyName, 
				ssn, postcode, phone, mobile, fax, email}){
			tb.validate();
			isValid = isValid && tb.isValid();
		}
		return isValid;
	}


}
