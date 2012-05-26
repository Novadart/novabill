package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class EstimationPlace extends Place {
	
	@Prefix(value=HistoryPrefix.ESTIMATION)
	public static class Tokenizer implements PlaceTokenizer<EstimationPlace> {

		@Override
		public EstimationPlace getPlace(String token) {
			Long estimationId = -1L;
			try {
				estimationId = Long.parseLong(token);
			} catch (NumberFormatException e) {
			}
			
			EstimationPlace ip = new EstimationPlace();
			ip.setEstimationId(estimationId);
			return ip;
		}

		@Override
		public String getToken(EstimationPlace place) {
			return String.valueOf(place.getEstimationId());
		}
		
	}

	private Long estimationId;
	private ClientDTO client;
	
	public EstimationPlace() {
		setEstimationId(0L);
	}

	public void setDataForNewInvoice(ClientDTO client) {
		this.client = client;
	}
	
	public ClientDTO getClient() {
		return client;
	}

	public Long getEstimationId() {
		return estimationId;
	}

	public void setEstimationId(Long estimationId) {
		this.estimationId = estimationId;
	}

}
