package com.novadart.novabill.frontend.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.novadart.novabill.frontend.client.ClientFactory;

public class CenterActivityMapper implements ActivityMapper {

//	private final ClientFactory clientFactory;

	public CenterActivityMapper(ClientFactory clientFactory) {
//		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
//		if(place instanceof HomePlace){
//
//			return new HomeActivity(clientFactory);
//
//		} else if(place instanceof BusinessPlace){
//
//			return new BusinessActivity(clientFactory);
//
//		} else if(place instanceof PaymentPlace){
//
//			return new PaymentActivity(clientFactory);
//
//		} else if(place instanceof InvoicePlace){
//
//			return new InvoiceActivity((InvoicePlace) place, clientFactory);
//
//		} else if(place instanceof CreditNotePlace){
//
//			return new CreditNoteActivity((CreditNotePlace)place, clientFactory);
//
//		} else if(place instanceof TransportDocumentPlace){
//
//			return new TransportDocumentActivity((TransportDocumentPlace)place, clientFactory);
//
//		} else if( place instanceof EstimationPlace) {
//			
//			return new EstimationActivity((EstimationPlace)place, clientFactory);
//			
//		} else if(place instanceof ClientPlace){
//
//			return new ClientActivity((ClientPlace)place, clientFactory);
//
//		}
		return null;
	}

}
