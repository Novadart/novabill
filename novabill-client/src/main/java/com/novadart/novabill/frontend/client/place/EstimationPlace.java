package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

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
	private EstimationDTO estimationToClone;
	private Long estimationProgressiveId;
	
	public EstimationPlace() {
		setEstimationId(0L);
	}

	public void setDataForNewEstimation(ClientDTO client, Long estimationProgressiveId) {
		this.client = client;
		this.estimationProgressiveId = estimationProgressiveId;
	}
	
	public void setDataForNewEstimation(ClientDTO client, Long estimationProgressiveId, EstimationDTO estimationToClone) {
		this.client = client;
		this.estimationProgressiveId = estimationProgressiveId;
		this.setEstimationToClone(estimationToClone);
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

	public EstimationDTO getEstimationToClone() {
		return estimationToClone;
	}

	public void setEstimationToClone(EstimationDTO estimationToClone) {
		this.estimationToClone = estimationToClone;
	}

	public Long getEstimationProgressiveId() {
		return estimationProgressiveId;
	}

	public void setEstimationProgressiveId(Long estimationProgressiveId) {
		this.estimationProgressiveId = estimationProgressiveId;
	}

}
