package com.novadart.novabill.frontend.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.center.BusinessActivity;
import com.novadart.novabill.frontend.client.activity.center.ClientActivity;
import com.novadart.novabill.frontend.client.activity.center.HomeActivity;
import com.novadart.novabill.frontend.client.activity.center.InvoiceActivity;
import com.novadart.novabill.frontend.client.place.BusinessPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;

public class CenterActivityMapper implements ActivityMapper {

	private final ClientFactory clientFactory;

	public CenterActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		if(place instanceof HomePlace){

			return new HomeActivity(clientFactory);

		} else if(place instanceof BusinessPlace){

			return new BusinessActivity(clientFactory);

		} else if(place instanceof InvoicePlace || place instanceof EstimationPlace){

			return new InvoiceActivity(place, clientFactory);

		} else if(place instanceof ClientPlace){

			return new ClientActivity((ClientPlace)place, clientFactory);

		}
		return null;
	}

}
