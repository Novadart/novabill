package com.novadart.novabill.frontend.client.place.estimation;

public class CloneEstimationPlace extends EstimationPlace {

	private Long estimationId;
	private Long clientId;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getEstimationId() {
		return estimationId;
	}

	public void setEstimationId(Long estimationId) {
		this.estimationId = estimationId;
	}
	
}
