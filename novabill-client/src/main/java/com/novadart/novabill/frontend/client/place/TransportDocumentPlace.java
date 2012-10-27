package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentPlace extends Place {

	@Prefix(value=HistoryPrefix.TRANSPORT_DOCUMENT)
	public static class Tokenizer implements PlaceTokenizer<TransportDocumentPlace> {

		@Override
		public TransportDocumentPlace getPlace(String token) {
			Long transportDocumentId = -1L;
			try {
				transportDocumentId = Long.parseLong(token);
			} catch (NumberFormatException e) {
			}

			TransportDocumentPlace ip = new TransportDocumentPlace();
			ip.setTransportDocumentId(transportDocumentId);
			return ip;
		}

		@Override
		public String getToken(TransportDocumentPlace place) {
			return String.valueOf(place.getTransportDocumentId());
		}

	}

	private Long transportDocumentId;
	private ClientDTO client;
	private TransportDocumentDTO transportDocumentToClone;
	private Long transportDocumentProgressiveId;

	public TransportDocumentPlace() {
		setTransportDocumentId(0L);
	}

	public void setDataForNewTransportDocument(ClientDTO client, Long transportDocumentProgressiveId) {
		this.client = client;
		this.transportDocumentProgressiveId = transportDocumentProgressiveId;
	}

	public void setDataForNewTransportDocument(ClientDTO client, Long transportDocumentProgressiveId, TransportDocumentDTO transportDocumentToClone) {
		this.client = client;
		this.transportDocumentToClone = transportDocumentToClone;
		this.transportDocumentProgressiveId = transportDocumentProgressiveId;
	}

	public ClientDTO getClient() {
		return client;
	}

	public void setClient(ClientDTO client) {
		this.client = client;
	}

	public Long getTransportDocumentId() {
		return transportDocumentId;
	}

	public void setTransportDocumentId(Long transportDocumentId) {
		this.transportDocumentId = transportDocumentId;
	}

	public TransportDocumentDTO getTransportDocumentToClone() {
		return transportDocumentToClone;
	}

	public void setTransportDocumentToClone(TransportDocumentDTO transportDocumentToClone) {
		this.transportDocumentToClone = transportDocumentToClone;
	}

	public Long getTransportDocumentProgressiveId() {
		return transportDocumentProgressiveId;
	}

	public void setTransportDocumentProgressiveId(
			Long transportDocumentProgressiveId) {
		this.transportDocumentProgressiveId = transportDocumentProgressiveId;
	}

}
