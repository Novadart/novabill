package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ClientPlace extends Place {
	
	public static enum DOCUMENTS {
		invoices,
		estimations,
		creditNotes,
		transportDocuments
	}

	@Prefix(value=HistoryPrefix.CLIENT)
	public static class Tokenizer implements PlaceTokenizer<ClientPlace>{

		@Override
		public ClientPlace getPlace(String token) {
			ClientPlace place = new ClientPlace();
			long placeId = 0;
			String id = "";
			
			String[] toks = token.split("/");
			if(toks.length > 1) {
				id = toks[0];
				DOCUMENTS docs = null;
				try{
					docs = DOCUMENTS.valueOf(toks[1].toLowerCase());
				} catch (Exception e) {
					docs = DOCUMENTS.invoices;
				}
				place.setDocumentsListing(docs);				
				
			} else{
				id = token;
			}
			
			
			try {
				placeId = Integer.parseInt(id);
			} catch (NumberFormatException e) {
			}
			place.setClientId(placeId);
			return place;
		}

		@Override
		public String getToken(ClientPlace place) {
			String clientId = String.valueOf(place.getClientId());
			
			if(place.getDocumentsListing() == null){

				return clientId;
			
			} else {
				
				switch(place.getDocumentsListing()){
				case estimations:
					return HistoryPrefix.CLIENT_ESTIMATIONS.replace("{clientId}", clientId);
					
				case creditNotes:
					return HistoryPrefix.CLIENT_CREDIT_NOTES.replace("{clientId}", clientId);
					
				case transportDocuments:
					return HistoryPrefix.CLIENT_TRANSPORT_DOCUMENTS.replace("{clientId}", clientId);
					
					default:
				case invoices:
					return HistoryPrefix.CLIENT_INVOICES.replace("{clientId}", clientId);
				}
				
			}
		}

	}

	private long clientId;
	private DOCUMENTS documentsListing;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public DOCUMENTS getDocumentsListing() {
		return documentsListing;
	}

	public void setDocumentsListing(DOCUMENTS documentsListing) {
		this.documentsListing = documentsListing;
	}
}
