package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;


public class HomePlace extends Place {

	@Prefix(value=HistoryPrefix.HOME)
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
