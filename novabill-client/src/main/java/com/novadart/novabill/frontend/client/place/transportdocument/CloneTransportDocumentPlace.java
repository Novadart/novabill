package com.novadart.novabill.frontend.client.place.transportdocument;

public class CloneTransportDocumentPlace extends TransportDocumentPlace {

	private Long transportDocumentId;
	private Long clientId;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getTransportDocumentId() {
		return transportDocumentId;
	}

	public void setTransportDocumentId(Long transportDocumentId) {
		this.transportDocumentId = transportDocumentId;
	}

}
