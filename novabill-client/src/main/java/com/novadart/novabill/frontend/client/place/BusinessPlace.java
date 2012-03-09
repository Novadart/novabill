package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BusinessPlace extends Place {
	
	public static class Tokenizer implements PlaceTokenizer<BusinessPlace>{

		@Override
		public BusinessPlace getPlace(String token) {
			return null;
		}

		@Override
		public String getToken(BusinessPlace place) {
			return "";
		}
		
	}
}
