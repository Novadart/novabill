package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class BusinessPlace extends Place {

	@Prefix(value=HistoryPrefix.BUSINESS)
	public static class Tokenizer implements PlaceTokenizer<BusinessPlace>{

		@Override
		public BusinessPlace getPlace(String token) {
			return new BusinessPlace();
		}

		@Override
		public String getToken(BusinessPlace place) {
			return "";
		}
		
	}
}
