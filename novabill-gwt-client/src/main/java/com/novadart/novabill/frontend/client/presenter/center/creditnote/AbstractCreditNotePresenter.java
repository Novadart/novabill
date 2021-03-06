package com.novadart.novabill.frontend.client.presenter.center.creditnote;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;

public abstract class AbstractCreditNotePresenter extends DocumentPresenter<CreditNoteView> implements CreditNoteView.Presenter {

	private CreditNoteDTO creditNote;
	
	public AbstractCreditNotePresenter(PlaceController placeController, EventBus eventBus, CreditNoteView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}
	
	protected void setCreditNote(CreditNoteDTO creditNote) {
		this.creditNote = creditNote;
	}
	
	protected CreditNoteDTO getCreditNote() {
		return creditNote;
	}

	@Override
	public void onCancelClicked() {
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if(value){
					BridgeUtils.invokeJSCallback(Boolean.FALSE, getCallback());
				}
			}
		});
	}


	protected CreditNoteDTO createCreditNote(CreditNoteDTO creditNote){
		CreditNoteDTO cn;

		if(creditNote != null){
			cn = creditNote;
		} else {
			cn = new CreditNoteDTO();
			cn.setLayoutType(Configuration.getBusiness().getSettings().getDefaultLayoutType());
			cn.setBusiness(Configuration.getBusiness());
			cn.setClient(getClient());
		}
		
		if(!getView().getSetToAddress().getValue()){
			getView().getToAddrCity().setText(getClient().getCity());
			getView().getToAddrCompanyName().setText(getClient().getName());
			getView().getToAddrPostCode().setText(getClient().getPostcode());
			getView().getToAddrProvince().setText(getClient().getProvince());
			getView().getToAddrStreetName().setText(getClient().getAddress());
			getView().getToAddrCountry().setSelectedItemByValue(getClient().getCountry());
		}

		EndpointDTO loc = new EndpointDTO();
		loc.setCompanyName(getView().getToAddrCompanyName().getText());
		loc.setCity(getView().getToAddrCity().getText());
		loc.setPostcode(getView().getToAddrPostCode().getText());
		loc.setProvince(getView().getToAddrProvince().getText());
		loc.setStreet(getView().getToAddrStreetName().getText());
		loc.setCountry(getView().getToAddrCountry().getSelectedItemValue());
		cn.setToEndpoint(loc);

		cn.setDocumentID(Long.parseLong(getView().getNumber().getText()));
		cn.setAccountingDocumentDate(DocumentUtils.createNormalizedDate(getView().getDate().getValue()));
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		cn.setItems(invItems);
		cn.setNote(getView().getNote().getText());
		CalcUtils.calculateTotals(invItems, cn);
		return cn;
	}
	
	
	protected boolean validateCreditNote(){
		getView().getDate().validate();
		getView().getNumber().validate();
		
		if(!getView().getItemInsertionForm().isValid()){
			return false;
		}
		
        boolean validation = true;

		if(getView().getSetToAddress().getValue()){
		    for (ValidatedWidget<?> vw : new ValidatedWidget<?>[]{getView().getToAddrCountry(), getView().getToAddrCity(), 
				    getView().getToAddrCompanyName(), getView().getToAddrPostCode(),	getView().getToAddrStreetName()}) {
			    vw.validate();
			    validation = validation && vw.isValid();
		    }
	    }

        if(getView().getSetToAddress().getValue() && getView().getToAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
			getView().getToAddrProvince().validate();
			validation = validation && getView().getToAddrProvince().isValid();
		}
		
		return validation && getView().getNumber().isValid() && getView().getDate().isValid();
	}

}
