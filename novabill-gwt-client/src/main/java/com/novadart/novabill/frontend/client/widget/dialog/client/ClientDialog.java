package com.novadart.novabill.frontend.client.widget.dialog.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.validation.TextLengthValidation;
import com.novadart.gwtshared.client.validation.VatIdValidation;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.validation.AlternativeSsnVatIdValidation;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.*;

public class ClientDialog extends Dialog implements HasUILocking {

	private static ClientDialogUiBinder uiBinder = GWT
			.create(ClientDialogUiBinder.class);

	interface ClientDialogUiBinder extends UiBinder<Widget, ClientDialog> {
	}

	public interface Style extends CssResource {
		String wrapper();
		String title();
		String title2();
		String rightBox();
		String gridWrapper();
		String label();
		String value();
		String buttonContainer();
		String submit();
	}

	@UiField InlineNotification inlineNotification;
	@UiField InlineNotification incompleteClientAlert;

	@UiField Label clientDialogTitle;

	@UiField(provided=true) com.novadart.gwtshared.client.validation.widget.ValidatedTextArea companyName;
	@UiField(provided=true) ValidatedTextBox address;
	@UiField(provided=true) ValidatedTextBox city;
	@UiField(provided=true) ValidatedTextBox province;
	@UiField(provided=true) ValidatedListBox country;
	@UiField(provided=true) ValidatedTextBox postcode;
	@UiField(provided=true) ValidatedTextBox phone;
	@UiField(provided=true) ValidatedTextBox mobile;
	@UiField(provided=true) ValidatedTextBox fax;
	@UiField(provided=true) ValidatedTextBox email;
	@UiField(provided=true) ValidatedTextBox web;
	@UiField(provided=true) ValidatedTextBox vatID;
	@UiField(provided=true) ValidatedTextBox ssn;

	@UiField(provided=true) ValidatedTextBox contactMobile;
	@UiField(provided=true) ValidatedTextBox contactFax;
	@UiField(provided=true) ValidatedTextBox contactEmail;
	@UiField(provided=true) ValidatedTextBox contactPhone;
	@UiField(provided=true) ValidatedTextBox contactName;
	@UiField(provided=true) ValidatedTextBox contactSurname;

	@UiField ListBox selectDefaultDocIdClass;
	@UiField ListBox selectSplitPayment;
	@UiField ListBox selectDefaultPayment;
	@UiField ListBox selectDefaultPriceList;

	@UiField ValidatedTextArea note;

	@UiField(provided=true) LoaderButton ok;
	@UiField Button cancel;
	@UiField Style s;

	private final Map<String, PaymentTypeDTO> paymentTypes = new HashMap<String, PaymentTypeDTO>();
	private final Map<String, PriceListDTO> priceLists = new HashMap<String, PriceListDTO>();

	private AlternativeSsnVatIdValidation ssnOrVatIdValidation = new AlternativeSsnVatIdValidation(); 

	private ClientDTO client = null;
	private final Long businessId;
	private final boolean incompleteClient;
	private final AsyncCallback<ClientDTO> callback;

	public ClientDialog(Long businessId, final JavaScriptObject callback){
		this(businessId, new AsyncCallback<ClientDTO>() {

			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(ClientDTO result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}
	
	public ClientDialog(Long businessId, AsyncCallback<ClientDTO> callback) {
		this(businessId, false, callback);
	}
	
	
	public ClientDialog(Long businessId, boolean incompleteClient, AsyncCallback<ClientDTO> callback) {
		super(GlobalBundle.INSTANCE.dialog(), false);
		GlobalBundle.INSTANCE.dialog().ensureInjected();

		this.businessId = businessId;
		this.callback = callback;
		this.incompleteClient = incompleteClient;

		companyName = new com.novadart.gwtshared.client.validation.widget.ValidatedTextArea(
				GlobalBundle.INSTANCE.validatedWidget(), new TextLengthValidation(255) {
			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(getMaxLength());
			}
		}, ValidationKit.NOT_EMPTY);

		vatID =  new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.VAT_ID);
		vatID.setShowMessageOnError(true);
		ssn =  new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.SSN_OR_VAT_ID);
		ssn.setShowMessageOnError(true);
		ssnOrVatIdValidation.addWidget(vatID);
		ssnOrVatIdValidation.addWidget(ssn);

		postcode = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);

		address = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		city = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		country = LocaleWidgets.createCountryListBox("");
		country.setSelectedItemByValue(Configuration.getBusiness().getCountry());
		
		phone = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		mobile = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		fax = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactPhone = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactMobile = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactFax = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);

		web = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactName = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactSurname = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);

		email = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.OPTIONAL_EMAIL);
		contactEmail = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.OPTIONAL_EMAIL);

		province = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);


		ok = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());

		setWidget(uiBinder.createAndBindUi(this));
		addStyleName(GlobalBundle.INSTANCE.globalCss().panel());

		ok.addStyleName(s.submit());
		ok.getButton().addStyleName("btn green");
		
		if(incompleteClient){
			incompleteClientAlert.showMessage(I18N.INSTANCE.incompleteClientAlert());
		}
	}

	private String renameDefaultPriceList(String name){
		return name.equalsIgnoreCase("::default") ? "LISTINO BASE" :  name;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();

		selectDefaultDocIdClass.setEnabled(false);
		ServerFacade.INSTANCE.getDocumentIdClassGwtService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<DocumentIDClassDTO>>() {
			@Override
			public void onSuccess(List<DocumentIDClassDTO> documentIDClassDTOs) {

				selectDefaultDocIdClass.addItem(I18N.INSTANCE.invoiceDefaultNumberClass(), (String) null);
				Long defaultDocIdClass = null;
				if (client != null) {
					defaultDocIdClass = client.getDefaultDocumentIDClassID();
				}

				int selectedIndex = 0;
				DocumentIDClassDTO dcd;
				for (int i = 0; i < documentIDClassDTOs.size(); i++) {
					dcd = documentIDClassDTOs.get(i);
					selectDefaultDocIdClass.addItem(dcd.getSuffix(), String.valueOf(dcd.getId()));
					if (defaultDocIdClass != null && dcd.getId().equals(defaultDocIdClass)) {
						selectedIndex = i + 1;
					}
				}
				selectDefaultDocIdClass.setSelectedIndex(selectedIndex);
				selectDefaultDocIdClass.setEnabled(true);
			}
		});

		selectSplitPayment.setSelectedIndex(client.isSplitPaymentClient() ? 1 : 0);

		selectDefaultPayment.setEnabled(false);
		ServerFacade.INSTANCE.getPaymentService().getAll(businessId, 
				new ManagedAsyncCallback<List<PaymentTypeDTO>>() {

			@Override
			public void onSuccess(List<PaymentTypeDTO> result) {
				Collections.sort(result, SharedComparators.PAYMENT_COMPARATOR);

				selectDefaultPayment.addItem("");

				Long defaultPaymentId = null;
				if(client != null && client.getDefaultPaymentTypeID() != null) {
					defaultPaymentId = client.getDefaultPaymentTypeID();
				}

				Integer indexOfDefaultPayment = null; 
				PaymentTypeDTO p;
				for (int i=0; i<result.size(); i++) {
					p = result.get(i);
					paymentTypes.put(String.valueOf(p.getId()), p);
					selectDefaultPayment.addItem(p.getName(), String.valueOf(p.getId()));

					indexOfDefaultPayment = indexOfDefaultPayment == null 
							? (	defaultPaymentId == null 
							? null 
									: (defaultPaymentId.equals(p.getId()) ? i : null) ) 
									: indexOfDefaultPayment;
				}

				// select the payment type
				if(indexOfDefaultPayment != null) {
					// +1 because the first element in list is empty
					selectDefaultPayment.setSelectedIndex(indexOfDefaultPayment+1);
				}

				selectDefaultPayment.setEnabled(true);

			}
		});
		
		
		selectDefaultPriceList.setEnabled(false);
		
		ServerFacade.INSTANCE.getPriceListGwtService().getAll(businessId, new ManagedAsyncCallback<List<PriceListDTO>>() {

			@Override
			public void onSuccess(List<PriceListDTO> result) {
				Collections.sort(result, SharedComparators.PRICE_LIST_COMPARATOR);

				Long defaultPriceListId = null;
				if(client != null && client.getDefaultPriceListID() != null) {
					defaultPriceListId = client.getDefaultPriceListID();
				}

				int indexOfDefaultPriceList = 0; 
				PriceListDTO p;
				for (int i=0; i<result.size(); i++) {
					p = result.get(i);
					priceLists.put(String.valueOf(p.getId()), p);
					selectDefaultPriceList.addItem(renameDefaultPriceList(p.getName()), String.valueOf(p.getId()));

					indexOfDefaultPriceList = defaultPriceListId == null ? 0 : (defaultPriceListId.equals(p.getId()) ? i : indexOfDefaultPriceList); 
				}

				// select the payment type
				selectDefaultPriceList.setSelectedIndex(indexOfDefaultPriceList);
				selectDefaultPriceList.setEnabled(true);				
			}
		});
		
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}

	public void setClient(ClientDTO client) {
		this.client = client;
		clientDialogTitle.setText(I18N.INSTANCE.modifyClientTitle());
		companyName.setText(client.getName());
		address.setText(client.getAddress());
		city.setText(client.getCity());
		province.setText(client.getProvince());
		country.setSelectedItemByValue(client.getCountry()==null ? Configuration.getBusiness().getCountry() : client.getCountry());
		setVatIdSsnValidation(country.getSelectedItemValue().equalsIgnoreCase("IT"));
		postcode.setText(client.getPostcode());
		phone.setText(client.getPhone());
		mobile.setText(client.getMobile());
		fax.setText(client.getFax());
		email.setText(client.getEmail());
		web.setText(client.getWeb());
		vatID.setText(client.getVatID());
		ssn.setText(client.getSsn());
		note.setText(client.getNote());

		ContactDTO ct = client.getContact();
		contactEmail.setText(ct.getEmail());
		contactFax.setText(ct.getFax());
		contactMobile.setText(ct.getMobile());
		contactName.setText(ct.getFirstName());
		contactPhone.setText(ct.getPhone());
		contactSurname.setText(ct.getLastName());

		ok.setText(I18N.INSTANCE.saveModifications());
	}


	@UiHandler("ok")
	void onOkClicked(ClickEvent e){
		if(!validate()){
			return;
		}

		ContactDTO contact;
		final ClientDTO client;

		if(this.client == null){
			client = new ClientDTO();
			contact = new ContactDTO();
			client.setContact(contact);
		} else {
			client = this.client;
			contact = this.client.getContact();
		}

		client.setAddress(address.getText());
		client.setCity(city.getText());
		client.setCountry(country.getSelectedItemValue());
		client.setEmail(email.getText());
		client.setFax(fax.getText());
		client.setMobile(mobile.getText());
		client.setName(companyName.getText());
		client.setPhone(phone.getText());
		client.setPostcode(postcode.getText());
		client.setProvince(province.getText());

		client.setDefaultDocumentIDClassID(
				selectDefaultDocIdClass.getSelectedIndex() == 0
						? null
						: Long.valueOf(selectDefaultDocIdClass.getValue(selectDefaultDocIdClass.getSelectedIndex()))
		);

		client.setSplitPaymentClient(selectSplitPayment.isItemSelected(1));

		if(selectDefaultPayment.getSelectedIndex() > 0){
			PaymentTypeDTO payment = paymentTypes.get(selectDefaultPayment.getValue(selectDefaultPayment.getSelectedIndex()));
			client.setDefaultPaymentTypeID(payment.getId());
		}
		
		
		if(selectDefaultPriceList.getSelectedIndex() > 0){
			PriceListDTO priceList = priceLists.get(selectDefaultPriceList.getValue(selectDefaultPriceList.getSelectedIndex()));
			client.setDefaultPriceListID(priceList.getId());
		}

		
		if(country.getSelectedItemValue().equalsIgnoreCase("IT")){
			
			if(VatIdValidation.isVatId(ssn.getText())) {
				String ssnValue = ssn.getText().trim();
				client.setSsn(ssnValue.isEmpty() || ssnValue.startsWith("IT") ? ssnValue : "IT"+ssnValue );
			} else {
				client.setSsn(ssn.getText());
			}
			
			String vatIDValue = vatID.getText().trim();
			client.setVatID(vatIDValue.isEmpty() || vatIDValue.startsWith("IT") ? vatIDValue : "IT"+vatIDValue );
			
		} else {
			
			client.setSsn(ssn.getText());
			client.setVatID(vatID.getText());
			
		}
		
		client.setWeb(web.getText());
		client.setNote(note.getText());

		contact.setEmail(contactEmail.getText());
		contact.setFax(contactFax.getText());
		contact.setFirstName(contactName.getText());
		contact.setLastName(contactSurname.getText());
		contact.setMobile(contactMobile.getText());
		contact.setPhone(contactPhone.getText());

		ok.showLoader(true);
		setLocked(true);
		if(this.client == null) {
			ServerFacade.INSTANCE.getClientService().add(businessId, client, new ManagedAsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					ok.showLoader(false);
					hide();
					callback.onSuccess(client);
					setLocked(false);
				}

				@Override
				public void onFailure(Throwable caught) {
					ok.showLoader(false);
					super.onFailure(caught);
					setLocked(false);
				}
			});
		} else {

			ServerFacade.INSTANCE.getClientService().update(businessId, client, new ManagedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					ok.showLoader(false);
					hide();
					callback.onSuccess(client);
					setLocked(false);
				}

				@Override
				public void onFailure(Throwable caught) {
					ok.showLoader(false);
					super.onFailure(caught);
					setLocked(false);
				}
			});

		}
	}

	@UiHandler("cancel")
	void onCancelClicked(ClickEvent e){
		hide();
		if(incompleteClient){
			callback.onFailure(new IncompleteClientException());
		}
	}

	@UiHandler("country")
	void onCountryChange(ChangeEvent event){
		switchValidationByCountry(country.getSelectedItemValue());
	}

	private void switchValidationByCountry(String country){
		boolean isIT = "IT".equalsIgnoreCase(country);
		setVatIdSsnValidation(isIT);
	}

	private void setVatIdSsnValidation(boolean activate){
		if(activate){
			ssn.setValidationBundle(ValidationKit.SSN_OR_VAT_ID);
			vatID.setValidationBundle(ValidationKit.VAT_ID);
		} else {
			ssn.setValidationBundle(ValidationKit.NOT_EMPTY);
			vatID.setValidationBundle(ValidationKit.NOT_EMPTY);
		}
	}


	private boolean validate(){
		boolean isValid = true;
		inlineNotification.hide();

		ssnOrVatIdValidation.validate();
		if(!ssnOrVatIdValidation.isValid()){
			inlineNotification.showMessage(ssnOrVatIdValidation.getErrorMessage());
			isValid = false;
		}

		for (ValidatedWidget<?> tb: new ValidatedWidget[]{companyName, 
				postcode, phone, mobile, fax, email, address, city, province, web,
				contactEmail, contactFax, contactMobile, contactName, contactPhone,
				contactSurname, note}){
			tb.validate();
			isValid = isValid && tb.isValid();
		}

		country.validate();
		isValid = isValid && country.isValid();
		return isValid;
	}

	@Override
	public void setLocked(boolean value) {
		companyName.setEnabled(!value);
		address.setEnabled(!value);
		city.setEnabled(!value);
		province.setEnabled(!value);
		country.setEnabled(!value);
		postcode.setEnabled(!value);
		phone.setEnabled(!value);
		mobile.setEnabled(!value);
		fax.setEnabled(!value);
		email.setEnabled(!value);
		web.setEnabled(!value);
		vatID.setEnabled(!value);
		ssn.setEnabled(!value);
		contactMobile.setEnabled(!value);
		contactFax.setEnabled(!value);
		contactEmail.setEnabled(!value);
		contactPhone.setEnabled(!value);
		contactName.setEnabled(!value);
		contactSurname.setEnabled(!value);
		cancel.setEnabled(!value);
		selectDefaultDocIdClass.setEnabled(!value);
		selectDefaultPayment.setEnabled(!value);
		selectDefaultPriceList.setEnabled(!value);
		selectSplitPayment.setEnabled(!value);
		note.setEnabled(!value);
	}

}
