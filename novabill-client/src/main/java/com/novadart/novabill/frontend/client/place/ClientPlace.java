package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ClientPlace extends Place {

	@Prefix(value=HistoryPrefix.CLIENT)
	public static class Tokenizer implements PlaceTokenizer<ClientPlace>{

		@Override
		public ClientPlace getPlace(String token) {
			ClientPlace place = new ClientPlace();
			long placeId = 0;
			try {
				placeId = Integer.parseInt(token);
			} catch (NumberFormatException e) {
			}
			place.setClientId(placeId);
			return place;
		}

		@Override
		public String getToken(ClientPlace place) {
			return String.valueOf(place.getClientId());
		}

	}

	private long clientId;


	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
}
