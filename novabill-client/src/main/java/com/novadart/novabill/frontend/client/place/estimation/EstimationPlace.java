package com.novadart.novabill.frontend.client.place.estimation;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.frontend.client.place.HistoryPrefix;
import com.novadart.novabill.frontend.client.place.HistoryUtils;

public abstract class EstimationPlace extends Place {

	public static String ARG_ID = "id";
	public static String ARG_CLONE = "clone";
	public static String ARG_CLIENT = "client";

	@Prefix(value=HistoryPrefix.ESTIMATION)
	public static class Tokenizer implements PlaceTokenizer<EstimationPlace> {

		@Override
		public EstimationPlace getPlace(String token) {
			Map<String, String> args = HistoryUtils.parseArguments(token);

			//NOTE order is important!
			if(args.containsKey(ARG_ID)){

				Long estimationId = null;
				try{
					estimationId = Long.parseLong(args.get(ARG_ID));
				} catch(NumberFormatException e){
					return null;
				}

				ModifyEstimationPlace p = new ModifyEstimationPlace();
				p.setEstimationId(estimationId);
				return p;

			}  else if(args.containsKey(ARG_CLONE)) {

				Long estimationId = null;
				Long clientId = null;
				try{
					estimationId = Long.parseLong(args.get(ARG_CLONE));
					clientId = Long.parseLong(args.get(ARG_CLIENT));
				} catch(NumberFormatException e){
					return null;
				}

				CloneEstimationPlace p = new CloneEstimationPlace();
				p.setClientId(clientId);
				p.setEstimationId(estimationId);
				return p;


			} else if(args.containsKey(ARG_CLIENT)) {

				Long clientId = null;
				try{
					clientId = Long.parseLong(args.get(ARG_CLIENT));
				} catch(NumberFormatException e){
					return null;
				}

				NewEstimationPlace p = new NewEstimationPlace();
				p.setClientId(clientId);
				return p;
			} 

			return null;

		}

		@Override
		public String getToken(EstimationPlace place) {
			if (place instanceof ModifyEstimationPlace) {
				ModifyEstimationPlace p = (ModifyEstimationPlace) place;
				return HistoryUtils.serialize(ARG_ID, p.getEstimationId());
			}

			if (place instanceof CloneEstimationPlace) {
				CloneEstimationPlace p = (CloneEstimationPlace) place;
				Map<String, String> args = new HashMap<String, String>();
				args.put(ARG_CLONE, String.valueOf(p.getEstimationId()));
				args.put(ARG_CLIENT, String.valueOf(p.getClientId()));
				return HistoryUtils.serialize(args);
			}

			if (place instanceof NewEstimationPlace) {
				NewEstimationPlace p = (NewEstimationPlace) place;
				return HistoryUtils.serialize(ARG_CLIENT, p.getClientId());
			}

			return null;
		}

	}

}
