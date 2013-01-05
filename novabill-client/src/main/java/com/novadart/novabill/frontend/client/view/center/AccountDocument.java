package com.novadart.novabill.frontend.client.view.center;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.util.WidgetUtils;
import com.novadart.novabill.shared.client.validation.Field;

public abstract class AccountDocument extends Composite {

	@Override
	protected void onLoad() {
		super.onLoad();
		WidgetUtils.setElementHeightToFillSpace(getBody(), getElement(), getNonBodyElements());
	}
	
	protected abstract Element[] getNonBodyElements();
	
	protected abstract Element getBody();
	
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
		case paymentDueDate: return I18N.INSTANCE.dueDate();
		case paymentNote: return I18N.INSTANCE.paymentNote();
		case paymentType: return I18N.INSTANCE.payment();
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
		
		default:
			return null;
		}
	}
}
