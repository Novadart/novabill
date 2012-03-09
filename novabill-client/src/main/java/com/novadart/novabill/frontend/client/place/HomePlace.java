package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class HomePlace extends Place {
	
	public static class Tokenizer implements PlaceTokenizer<HomePlace>{

		@Override
		public HomePlace getPlace(String token) {
			return null;
		}

		@Override
		public String getToken(HomePlace place) {
			return "";
		}
		
	}
}
