package com.novadart.novabill.shared.client.dto;

import java.util.List;

public class SyncDeltaObjectsDTO {
	
	List<ClientDTO> clients;

	public List<ClientDTO> getClients() {
		return clients;
	}

	public void setClients(List<ClientDTO> clients) {
		this.clients = clients;
	}
	
}
