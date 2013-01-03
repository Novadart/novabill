package com.novadart.novabill.frontend.client.place;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ClientPlace extends Place {
	
	public static String ARG_ID = "id";
	public static String ARG_DOCS = "docs";
	
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
			Map<String, String> args = HistoryUtils.parseArguments(token);
			
			if(args.containsKey(ARG_ID)){
				
				Long clientId = null;
				try{
					clientId = Long.parseLong(args.get(ARG_ID));
				} catch(NumberFormatException e){
					return null;
				}
				
				ClientPlace p = new ClientPlace();
				p.setClientId(clientId);
				
				if(args.containsKey(ARG_DOCS)){
					int docId = 0;
					
					try{
						docId = Integer.parseInt(args.get(ARG_DOCS));
						docId = docId > DOCUMENTS.values().length || docId < 0 ? 0 : docId;
					} catch(NumberFormatException e){
						docId = 0;
					}
					p.setDocs(DOCUMENTS.values()[docId]);
				}
				
				return p;
				
			}
			
			return null;
		}

		@Override
		public String getToken(ClientPlace place) {
			Map<String, String> args = new HashMap<String, String>();
			args.put(ARG_ID, String.valueOf(place.getClientId()));
			args.put(ARG_DOCS, String.valueOf(place.getDocs().ordinal()));
			return HistoryUtils.serialize(args);
		}

	}

	private long clientId;
	private DOCUMENTS docs = DOCUMENTS.invoices;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public DOCUMENTS getDocs() {
		return docs;
	}

	public void setDocs(DOCUMENTS docs) {
		this.docs = docs;
	}
}
