package com.novadart.novabill.frontend.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.west.BusinessActivity;
import com.novadart.novabill.frontend.client.activity.west.ClientActivity;
import com.novadart.novabill.frontend.client.activity.west.CreditNoteActivity;
import com.novadart.novabill.frontend.client.activity.west.EstimationActivity;
import com.novadart.novabill.frontend.client.activity.west.HomeActivity;
import com.novadart.novabill.frontend.client.activity.west.InvoiceActivity;
import com.novadart.novabill.frontend.client.activity.west.PaymentActivity;
import com.novadart.novabill.frontend.client.activity.west.TransportDocumentActivity;
import com.novadart.novabill.frontend.client.place.BusinessPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.PaymentPlace;
import com.novadart.novabill.frontend.client.place.creditnote.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.estimation.EstimationPlace;
import com.novadart.novabill.frontend.client.place.invoice.InvoicePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.TransportDocumentPlace;

public class WestActivityMapper implements ActivityMapper {

private final ClientFactory clientFactory;
	
	public WestActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public Activity getActivity(Place place) {
		if(place instanceof HomePlace){
			
			return new HomeActivity(clientFactory);
		
		} else if(place instanceof InvoicePlace) {
			
			return new InvoiceActivity((InvoicePlace)place, clientFactory);
		
		} else if(place instanceof EstimationPlace) {
			
			return new EstimationActivity((EstimationPlace)place, clientFactory);
		
		} else if(place instanceof CreditNotePlace) {
			
			return new CreditNoteActivity((CreditNotePlace)place, clientFactory);
		
		} else if(place instanceof TransportDocumentPlace) {
			
			return new TransportDocumentActivity((TransportDocumentPlace)place, clientFactory);
		
		} else if(place instanceof ClientPlace) {
		
			return new ClientActivity((ClientPlace)place, clientFactory);
	
		} else if(place instanceof BusinessPlace) {
		
			return new BusinessActivity(clientFactory);
	
		} else if(place instanceof PaymentPlace) {
		
			return new PaymentActivity(clientFactory);
	
		} else {
		
			return new HomeActivity(clientFactory);
	
		}
	}

}