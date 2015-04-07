package com.novadart.novabill.frontend.client.place.transportdocument;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.frontend.client.place.HistoryPrefix;
import com.novadart.novabill.frontend.client.place.HistoryUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class TransportDocumentPlace extends Place {

	public static String ARG_ID = "id";
	public static String ARG_CLONE = "clone";
	public static String ARG_CLIENT = "client";

	@Prefix(value=HistoryPrefix.TRANSPORT_DOCUMENT)
	public static class Tokenizer implements PlaceTokenizer<TransportDocumentPlace> {

		@Override
		public TransportDocumentPlace getPlace(String token) {
			Map<String, String> args = HistoryUtils.parseArguments(token);

			//NOTE order is important!
			if(args.containsKey(ARG_ID)){

				Long transportDocumentId = null;
				try{
					transportDocumentId = Long.parseLong(args.get(ARG_ID));
				} catch(NumberFormatException e){
					return null;
				}

				ModifyTransportDocumentPlace p = new ModifyTransportDocumentPlace();
				p.setTransportDocumentId(transportDocumentId);
				return p;

			}  else if(args.containsKey(ARG_CLONE)) {

				Long transportDocumentId = null;
				Long clientId = null;
				try{
					transportDocumentId = Long.parseLong(args.get(ARG_CLONE));
					clientId = Long.parseLong(args.get(ARG_CLIENT));
				} catch(NumberFormatException e){
					return null;
				}

				CloneTransportDocumentPlace p = new CloneTransportDocumentPlace();
				p.setTransportDocumentId(transportDocumentId);
				p.setClientId(clientId);
				return p;


			} else if(args.containsKey(ARG_CLIENT)) {

				Long clientId = null;
				try{
					clientId = Long.parseLong(args.get(ARG_CLIENT));
				} catch(NumberFormatException e){
					return null;
				}

				NewTransportDocumentPlace p = new NewTransportDocumentPlace();
				p.setClientId(clientId);
				return p;
			}

			return null;

		}

		@Override
		public String getToken(TransportDocumentPlace place) {
			if (place instanceof ModifyTransportDocumentPlace) {
				ModifyTransportDocumentPlace p = (ModifyTransportDocumentPlace) place;
				return HistoryUtils.serialize(ARG_ID, p.getTransportDocumentId());
			}
			
			if (place instanceof CloneTransportDocumentPlace) {
				CloneTransportDocumentPlace p = (CloneTransportDocumentPlace) place;
				Map<String, String> args = new HashMap<String, String>();
				args.put(ARG_CLONE, String.valueOf(p.getTransportDocumentId()));
				args.put(ARG_CLIENT, String.valueOf(p.getClientId()));
				return HistoryUtils.serialize(args);
			}
			
			if (place instanceof NewTransportDocumentPlace) {
				NewTransportDocumentPlace p = (NewTransportDocumentPlace) place;
				return HistoryUtils.serialize(ARG_CLIENT, p.getClientId());
			}
			
			return null;
		}

	}

}
