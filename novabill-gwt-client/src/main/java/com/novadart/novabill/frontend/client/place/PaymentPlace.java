package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class PaymentPlace extends Place {

	@Prefix(value=HistoryPrefix.PAYMENT)
	public static class Tokenizer implements PlaceTokenizer<PaymentPlace>{

		@Override
		public PaymentPlace getPlace(String token) {
			return new PaymentPlace();
		}

		@Override
		public String getToken(PaymentPlace place) {
			return "";
		}
		
	}
	
}
