package com.novadart.novabill.frontend.client.presenter.center;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

public abstract class DocumentPresenter<V extends DocumentView<?>> extends AbstractPresenter<V> implements DocumentView.Presenter {
	
	private static final DateTimeFormat YEAR_FORMAT = DateTimeFormat.getFormat("yyyy");
	
	private ClientDTO client;
	private final JavaScriptObject callback;
	
	public DocumentPresenter(PlaceController placeController, EventBus eventBus, V view, JavaScriptObject callback) {
		super(placeController, eventBus, view);
		this.callback = callback;				
	}
	
	@Override
	public void go(AcceptsOneWidget panel) {
		super.go(panel);
	}
	
	protected String getHumanReadable(Field field){
		switch (field) {
		case accountingDocumentDate: return I18N.INSTANCE.date();
		case accountingDocumentItems_description: return I18N.INSTANCE.nameDescription();
		case accountingDocumentItems_price: return I18N.INSTANCE.price();
		case accountingDocumentItems_quantity: return I18N.INSTANCE.quantity();
		case accountingDocumentItems_tax: return I18N.INSTANCE.vat();
		case accountingDocumentItems_total: return I18N.INSTANCE.totalAfterTaxesForItem();
		case accountingDocumentItems_totalBeforeTax: return I18N.INSTANCE.totalBeforeTaxesForItem();
		case accountingDocumentItems_totalTax: return I18N.INSTANCE.totalTaxForItem();
		case accountingDocumentItems_unitOfMeasure: return I18N.INSTANCE.unityOfMeasure();
		case accountingDocumentYear: return null;
		case toEndpoint_street:
		case fromEndpoint_street:
		case address: return I18N.INSTANCE.address();
		case toEndpoint_city:
		case fromEndpoint_city:
		case city: return I18N.INSTANCE.city();
		case email:
		case contact_email: return I18N.INSTANCE.email();
		case fax:
		case contact_fax: return I18N.INSTANCE.fax();
		case name:
		case contact_firstName: return I18N.INSTANCE.name();
		case contact_lastName: return I18N.INSTANCE.surname();
		case mobile:
		case contact_mobile: return I18N.INSTANCE.mobile();
		case phone:
		case contact_phone: return I18N.INSTANCE.phone();
		case toEndpoint_country:
		case fromEndpoint_country:
		case country: return I18N.INSTANCE.country();
		case documentID: return null;
		case toEndpoint_companyName:
		case fromEndpoint_companyName: return I18N.INSTANCE.companyName();
		case postcode:
		case toEndpoint_postcode:
		case fromEndpoint_postcode: return I18N.INSTANCE.postcode();
		case province:
		case toEndpoint_province:
		case fromEndpoint_province: return I18N.INSTANCE.province();
		case limitations: return I18N.INSTANCE.limitations();
		case note: return I18N.INSTANCE.note();
		case numberOfPackages: return I18N.INSTANCE.numberOfPackages();
		case payed: return I18N.INSTANCE.payed();
		case paymentDateDelta: return I18N.INSTANCE.paymentDelay();
		case paymentDateGenerator: return I18N.INSTANCE.payment();
		case paymentDueDate: return I18N.INSTANCE.dueDate();
		case paymentNote: return I18N.INSTANCE.paymentNote();
		case paymentTypeName: return I18N.INSTANCE.payment();
		case ssn: return I18N.INSTANCE.ssn();
		case total: return I18N.INSTANCE.totalAfterTaxes();
		case totalBeforeTax: return I18N.INSTANCE.totalBeforeTaxes();
		case totalTax: return I18N.INSTANCE.totalTax();
		case tradeZone: return I18N.INSTANCE.tradeZone();
		case transportationResponsibility: return I18N.INSTANCE.transportationResponsibility();
		case transporter: return I18N.INSTANCE.transporter();
		case transportStartDate: return I18N.INSTANCE.transportStartDate();
		case validTill: return I18N.INSTANCE.validTill();
		case vatID: return I18N.INSTANCE.vatID();
		case web: return I18N.INSTANCE.web();
		case defaultPaymentNote: return I18N.INSTANCE.paymentNote();

		default:
			return null;
		}
	}
	
	protected void handleServerValidationException(ValidationException ex){
		if(ex.getErrors().size() > 0){
			ErrorObject eo = ex.getErrors().get(0);
			
			switch(eo.getErrorCode()){
			case INVALID_DOCUMENT_ID:
				StringBuilder sb = new StringBuilder();
				List<Long> gaps = eo.getGaps();

				if(gaps.size() > 1) {
					for (int i=0; i<gaps.size()-1; i++) {
						sb.append(gaps.get(i) +", ");
					}
					sb.append(gaps.get(gaps.size()-1));
				} else {
					sb.append(gaps.get(0));
				}
					
				getView().getNumber().showErrorMessage(I18NM.get.invalidDocumentIdError(sb.toString()));
				break;
			
			default:
				Notification.showMessage(I18NM.get.errorCheckField(getHumanReadable(eo.getField())));
				break;
			}
		}
	}
	
	protected ClientDTO getClient() {
		return client;
	}
	
	protected void setClient(ClientDTO client) {
		this.client = client;
		getView().getItemInsertionForm().setClientId(client.getId());
	}
	
	public static DateTimeFormat getYearFormat() {
		return YEAR_FORMAT;
	}
	
	protected JavaScriptObject getCallback() {
		return callback;
	}
}
